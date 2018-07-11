package me.leiho.elasticsearch.controller;

import me.leiho.elasticsearch.dto.HotelInfoDTO;
import me.leiho.elasticsearch.service.ESService;
import me.leiho.elasticsearch.vo.req.SearchHotelInfoByCoordinatesAndRangeReq;
import me.leiho.elasticsearch.vo.req.SearchHotelInfoByKeyWordReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.leiho.elasticsearch.service.ESService;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

@Api("ES-API")
@Slf4j
@RestController
@RequestMapping("/es")
public class ESController {
    @Autowired
    private ESService esService;

    @ApiIgnore
    @ApiOperation("导入一个城市的酒店信息到ES")
    @PostMapping("/iochi")
    public String importOneCityHotelInfo(String cityID,String cityName){
        return esService.importOneCityHotelInfo(cityID,cityName);
    }
    @ApiOperation("导入全中国的酒店信息到ES")
    @PostMapping("/iachi")
    public String importAllChinaHotelInfo(){
        return esService.importAllChinaHotelInfo();
    }

    @ApiOperation("根据关键字和城市编号搜索相关酒店")
    @GetMapping("/shibk")
    public SearchResponse searchHotelInfoByKeyWord(SearchHotelInfoByKeyWordReq req){
        return esService.searchHotelInfoByKeyWord(req);
    }

    @ApiOperation("根据坐标和范围搜索相关酒店")
    @GetMapping("/shibcar")
    public SearchResponse searchHotelInfoByCoordinatesAndRange(SearchHotelInfoByCoordinatesAndRangeReq req){
        return esService.searchHotelInfoByCoordinatesAndRange(req);
    }

    @ApiOperation("修改ES的某条记录(根据ID)")
    @PostMapping("/uhibid")
    public Map<String, Object> updateHotelInfoByID(HotelInfoDTO record){
        return esService.updateHotelInfoByID(record);
    }

    @ApiOperation("根据ID查询一条记录")
    @GetMapping("/sohibid")
    public Map<String, Object> selectOneHotelInfoByID(String id){
        return esService.selectOneHotelInfoByID(id);
    }

    @ApiIgnore
    @ApiOperation("更新一个城市的酒店的参考价格(priceLevel)")
    @PostMapping("/iochpl")
    public String importOneCityHotelPriceLevel(String cityID,String cityName){
        return esService.importOneCityHotelPriceLevel(cityID,cityName);
    }

    @ApiOperation("批量更新酒店的参考价格(priceLevel)")
    @GetMapping("/uachpl")
    public String updateAllChinaHotelPriceLevel(){
        return esService.updateAllChinaHotelPriceLevel();
    }
}
