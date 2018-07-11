package me.leiho.elasticsearch.controller;


import me.leiho.elasticsearch.entity.PfHotel;
import me.leiho.elasticsearch.service.PfHotelService;
import me.leiho.elasticsearch.vo.req.QueryOnePageHotelReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.leiho.elasticsearch.service.PfHotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 腾住酒店表 前端控制器
 * </p>
 *
 * @author 萧大俠
 * @since 2018-06-28
 */
@Api(tags = "腾住酒店信息接口")
@RestController
@RequestMapping("/pfHotel")
public class HotelController {
    @Autowired
    private PfHotelService pfHotelService;

    @ApiOperation("获取一页酒店数据")
    @PostMapping("/getOnePageHotel")
    public List<PfHotel> getOnePageHotel(QueryOnePageHotelReq req){
        return pfHotelService.getOnePageHotel(req);
    }
}

