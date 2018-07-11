package me.leiho.elasticsearch.service;

import me.leiho.elasticsearch.dto.HotelInfoDTO;
import me.leiho.elasticsearch.vo.req.SearchHotelInfoByCoordinatesAndRangeReq;
import me.leiho.elasticsearch.vo.req.SearchHotelInfoByKeyWordReq;
import org.elasticsearch.action.search.SearchResponse;

import java.util.Map;

public interface ESService {
    String importOneCityHotelInfo(String cityID,String cityName);
    String importAllChinaHotelInfo();
    SearchResponse searchHotelInfoByKeyWord(SearchHotelInfoByKeyWordReq req);
    SearchResponse searchHotelInfoByCoordinatesAndRange(SearchHotelInfoByCoordinatesAndRangeReq req);
    Map<String, Object> updateHotelInfoByID(HotelInfoDTO record);
    Map<String, Object> selectOneHotelInfoByID(String id);
    String updateAllChinaHotelPriceLevel();
    String importOneCityHotelPriceLevel(String cityID,String cityName);
}
