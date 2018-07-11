package me.leiho.elasticsearch.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.leiho.elasticsearch.dto.HotelInfoDTO;
import me.leiho.elasticsearch.dto.LocationDTO;
import me.leiho.elasticsearch.entity.City;
import me.leiho.elasticsearch.entity.PfChannelRoomPrice;
import me.leiho.elasticsearch.entity.PfHotel;
import me.leiho.elasticsearch.entity.PfHotelFacility;
import me.leiho.elasticsearch.exception.BookException;
import me.leiho.elasticsearch.exception.ESServiceException;
import me.leiho.elasticsearch.exception.ParamException;
import me.leiho.elasticsearch.mapper.CityMapper;
import me.leiho.elasticsearch.mapper.PfChannelRoomPriceMapper;
import me.leiho.elasticsearch.mapper.PfHotelFacilityMapper;
import me.leiho.elasticsearch.mapper.PfHotelMapper;
import me.leiho.elasticsearch.service.ESService;
import me.leiho.elasticsearch.util.ESClientConnect;
import me.leiho.elasticsearch.vo.req.SearchHotelInfoByCoordinatesAndRangeReq;
import me.leiho.elasticsearch.vo.req.SearchHotelInfoByKeyWordReq;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ESServiceImpl implements ESService {

    private static final String clusterName = "elasticsearch";
    private static final String INDEX = "hotel_info_dev";
    private static final String TYPE = "for_search";
    private static final String ES_SERVICE_ERROR = "Elasticsearch服务异常";

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CityMapper cityMapper;
    @Autowired
    private PfHotelMapper pfHotelMapper;
    @Autowired
    private PfHotelFacilityMapper pfHotelFacilityMapper;
    @Autowired
    private PfChannelRoomPriceMapper pfChannelRoomPriceMapper;
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Value("${elasticsearch.url}")
    private String HOST;
    @Value("${elasticsearch.port}")
    private Integer PORT;

    DecimalFormat df = new DecimalFormat("######0.00");

    @Override
    public String importOneCityHotelInfo(String cityID,String cityName){
        BulkRequest bulkRequest = new BulkRequest();
        List<PfHotel> hotels = pfHotelMapper.selectList(new EntityWrapper<PfHotel>().eq("MARK_FOR_DELETE", 0).eq("CITY",cityID));
        Double start = (double)new Date().getTime();
        hotels.forEach(h -> insertOneHotelInfo(h,cityName,bulkRequest));
        try {
            restHighLevelClient.bulk(bulkRequest);
        } catch(ElasticsearchException |java.io.IOException e) {
            throw new BookException(ES_SERVICE_ERROR,e);
        }
        Double over = (double)new Date().getTime();
        return "城市编号为"+cityID+"的"+cityName+"导入"+hotels.size()+"条数据,耗时"+df.format((over-start)/1000)+"秒";
    }

    @Override
    public String importAllChinaHotelInfo() {
        List<City> cities = cityMapper.selectList(new EntityWrapper<City>().eq("del",0).eq("country_id",104));
        Double start = (double)new Date().getTime();
        cities.forEach(c -> log.info(importOneCityHotelInfo(c.getCityId(),c.getCityCn())));
        Double over = (double)new Date().getTime();
        String result = "总共耗时"+df.format((over-start)/1000)+"秒";
        log.info(result);
        return result;
    }

    @Override
    public SearchResponse searchHotelInfoByKeyWord(SearchHotelInfoByKeyWordReq req) {
        if (StringUtils.isBlank(req.getCityId())||StringUtils.isBlank(req.getCityId())||null==req.getPage()||null==req.getSize()){
            throw new ParamException("参数不能为空");
        }
        Integer from = 0;
        if (req.getPage() > 0 && req.getSize() >0){
            from = (req.getPage() - 1)*req.getSize();
        }else {
            throw new ParamException("分页信息填写错误");
        }
        List<City> cities = cityMapper.selectList(new EntityWrapper<City>().eq("city_id",req.getCityId()).eq("del",0).eq("country_id",104));
        if (cities.isEmpty()){
            throw new ParamException("城市编号填写错误");
        }
        TransportClient client = new ESClientConnect(clusterName, HOST, 9300).getClient();
        QueryBuilder queryBuilder =
                QueryBuilders
                        .boolQuery()
                        .must(QueryBuilders.termQuery("city", req.getCityId()))
                        .must(QueryBuilders.multiMatchQuery(req.getKeyWord(),"hotelName","addr","busiZone","area","intro;").analyzer("ik_max_word"));
        SearchRequestBuilder searchRequestBuilder =
                client.prepareSearch(INDEX)
                        .setTypes(TYPE)
//                        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                        .setSearchType(SearchType.QUERY_THEN_FETCH)
                        .setQuery(queryBuilder)
                        .setFrom(from)
                        .setSize(req.getSize());
        return searchRequestBuilder.get();
    }

    @Override
    public SearchResponse searchHotelInfoByCoordinatesAndRange(SearchHotelInfoByCoordinatesAndRangeReq req) {
        if (null==req.getLat()||null==req.getLon()||null==req.getRange()||null==req.getPage()||null==req.getSize()){
            throw new ParamException("参数不能为空");
        }
        Integer from = 0;
        if (req.getPage() > 0 && req.getSize() >0){
            from = (req.getPage() - 1)*req.getSize();
        }else {
            throw new ParamException("分页信息填写错误");
        }
        TransportClient client = new ESClientConnect(clusterName, HOST, 9300).getClient();
        GeoDistanceQueryBuilder queryBuilder =
                QueryBuilders.geoDistanceQuery("location").point(req.getLat(),req.getLon()).distance(req.getRange(),DistanceUnit.KILOMETERS);
        SearchRequestBuilder searchRequestBuilder =
                client.prepareSearch(INDEX)
                        .setTypes(TYPE)
//                        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                        .setSearchType(SearchType.QUERY_THEN_FETCH)
                        .setQuery(queryBuilder)
                        .setFrom(from)
                        .setSize(req.getSize());
        return searchRequestBuilder.get();
    }

    @Override
    public Map<String, Object> updateHotelInfoByID(HotelInfoDTO record) {
        UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, record.getId()).fetchSource(true);
        Map<String, Object> sourceAsMap;
        String recordJson;
        try {
            recordJson = objectMapper.writeValueAsString(record);
            updateRequest.doc(recordJson, XContentType.JSON);
            UpdateResponse updateResponse = restHighLevelClient.update(updateRequest);
            sourceAsMap = updateResponse.getGetResult().sourceAsMap();
        } catch (JsonProcessingException e) {
            throw new ParamException("JSON转换异常");
        } catch (IOException e) {
            throw new ESServiceException(ES_SERVICE_ERROR);
        }
        return sourceAsMap;
    }

    @Override
    public Map<String, Object> selectOneHotelInfoByID(String id) {
        GetRequest getRequest = new GetRequest(INDEX, TYPE, id);
        GetResponse getResponse = null;
        try {
            getResponse = restHighLevelClient.get(getRequest);
        } catch (IOException e) {
            throw new ESServiceException(ES_SERVICE_ERROR);
        }
        Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
        if (sourceAsMap==null){
            throw new ESServiceException("编号不存在,查询失败");
        }
        return sourceAsMap;
    }

    @Override
    public String updateAllChinaHotelPriceLevel() {
        List<City> cities = cityMapper.selectList(new EntityWrapper<City>().eq("del",0).eq("country_id",104));
        Double start = (double)new Date().getTime();
        cities.forEach(c -> log.info(importOneCityHotelPriceLevel(c.getCityId(),c.getCityCn())));
        Double over = (double)new Date().getTime();
        String result = "总共耗时"+df.format((over-start)/1000)+"秒";
        log.info(result);
        return result;
    }

    public String importOneCityHotelPriceLevel(String cityID,String cityName){
        BulkRequest bulkRequest = new BulkRequest();
        //在参考价格表获取PfChannelRoomPrice,并将其传入bulkRequest
        List<PfChannelRoomPrice> prices = pfChannelRoomPriceMapper.selectList(new EntityWrapper<PfChannelRoomPrice>().eq("MARK_FOR_DELETE", 0).eq("CITY_ID",cityID).isNotNull("HOTEL_ID"));
        Double start = (double)new Date().getTime();
        prices.forEach(p -> insertOneHotelPriceLevel(p,cityName,bulkRequest));
        try {
            restHighLevelClient.bulk(bulkRequest);
        } catch(ElasticsearchException |java.io.IOException e) {
            throw new BookException(ES_SERVICE_ERROR,e);
        }
        Double over = (double)new Date().getTime();
        return "城市编号为"+cityID+"的"+cityName+"更新"+prices.size()+"条数据,耗时"+df.format((over-start)/1000)+"秒";
    }

    private void insertOneHotelPriceLevel(PfChannelRoomPrice pfChannelRoomPrice,String cityName,BulkRequest bulkRequest){
        Map<String, Object> dataMap = objectMapper.convertValue(getHotelInfoWithNewPriceLevel(pfChannelRoomPrice,cityName), Map.class);
        IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, pfChannelRoomPrice.getHotelId()).source(dataMap);
        bulkRequest.add(indexRequest);
    }

    private HotelInfoDTO getHotelInfoWithNewPriceLevel(PfChannelRoomPrice pfChannelRoomPrice,String cityName){
        //通过PfChannelRoomPrice的hotel_id取得PfHotel
        String hotelId = pfChannelRoomPrice.getHotelId().trim();
        HotelInfoDTO hotelInfoDTO = new HotelInfoDTO();
        PfHotel pfHotel = pfHotelMapper.selectById(hotelId);

        BeanUtils.copyProperties(pfHotel,hotelInfoDTO);
        hotelInfoDTO.setCityName(cityName);
        //通过hotelId取得酒店设施
        List<PfHotelFacility> facilityList = pfHotelFacilityMapper.selectList(new EntityWrapper<PfHotelFacility>().eq("HOTEL_ID",hotelId).eq("MARK_FOR_DELETE",0));
        StringBuilder facilities = new StringBuilder();
        facilities.append("HOTWATER");
        if (facilityList != null&&facilityList.size()!=0){
            for (PfHotelFacility facility:facilityList){
                if ((facility.getNameCn().toLowerCase().contains("wifi")||facility.getNameCn().toLowerCase().contains("上网")||facility.getNameCn().toLowerCase().contains("宽带")||facility.getNameCn().toLowerCase().contains("有线")||facility.getNameCn().toLowerCase().contains("无线")||facility.getNameCn().toLowerCase().contains("电脑"))&&!facility.getNameCn().toLowerCase().contains("无感知WIFI")){
                    facilities.append(",WIFI");
                }else if (facility.getNameCn().contains("空调")&&!facility.getNameCn().contains("无空调")){
                    facilities.append(",AIRCONDITION");
                }else if (facility.getNameCn().contains("停车")&&!facility.getNameCn().contains("无停车")){
                    facilities.append(",PARK");
                }else if (facility.getNameCn().contains("餐厅")){
                    facilities.append(",RESTAURANT");
                }
            }
            hotelInfoDTO.setFacilities(facilities.toString());
        }
        hotelInfoDTO.setPriceLevel(pfChannelRoomPrice.getPrice());
        return hotelInfoDTO;
    }

    private void insertOneHotelInfo(PfHotel pfHotel,String cityName,BulkRequest bulkRequest){
        Map<String, Object> dataMap = objectMapper.convertValue(getHotelInfo(pfHotel,cityName), Map.class);
        IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, pfHotel.getId()).source(dataMap);
        bulkRequest.add(indexRequest);
    }

    private HotelInfoDTO getHotelInfo(PfHotel pfHotel,String cityName){
        HotelInfoDTO hotelInfoDTO = new HotelInfoDTO();
        BeanUtils.copyProperties(pfHotel,hotelInfoDTO);
        hotelInfoDTO.setCityName(cityName);
        if (pfHotel.getLatitude()!=null&&pfHotel.getLatitude()!=null){
            LocationDTO location = new LocationDTO();
            location.setLat(Double.parseDouble(pfHotel.getLatitude()));
            location.setLon(Double.parseDouble(pfHotel.getLongitude()));
            hotelInfoDTO.setLocation(location);
        }
        String hotelId = pfHotel.getId();
        //通过hotelId取得酒店设施
        List<PfHotelFacility> facilityList = pfHotelFacilityMapper.selectList(new EntityWrapper<PfHotelFacility>().eq("HOTEL_ID",hotelId).eq("MARK_FOR_DELETE",0));
        StringBuilder facilities = new StringBuilder();
        facilities.append("HOTWATER");
        if (facilityList != null&&facilityList.size()!=0){
            for (PfHotelFacility facility:facilityList){
                if ((facility.getNameCn().toLowerCase().contains("wifi")||facility.getNameCn().toLowerCase().contains("上网")||facility.getNameCn().toLowerCase().contains("宽带")||facility.getNameCn().toLowerCase().contains("有线")||facility.getNameCn().toLowerCase().contains("无线")||facility.getNameCn().toLowerCase().contains("电脑"))&&!facility.getNameCn().toLowerCase().contains("无感知WIFI")){
                    facilities.append(",WIFI");
                }else if (facility.getNameCn().contains("空调")&&!facility.getNameCn().contains("无空调")){
                    facilities.append(",AIRCONDITION");
                }else if (facility.getNameCn().contains("停车")&&!facility.getNameCn().contains("无停车")){
                    facilities.append(",PARK");
                }else if (facility.getNameCn().contains("餐厅")){
                    facilities.append(",RESTAURANT");
                }
            }
            hotelInfoDTO.setFacilities(facilities.toString());
        }
        PfChannelRoomPrice filter = new PfChannelRoomPrice();
        filter.setHotelId(hotelId);
        filter.setMarkForDelete(0);
        PfChannelRoomPrice pfChannelRoomPrice = pfChannelRoomPriceMapper.selectOne(filter);
        if (pfChannelRoomPrice != null){
            hotelInfoDTO.setPriceLevel(pfChannelRoomPrice.getPrice());
        }
        return hotelInfoDTO;
    }

}
