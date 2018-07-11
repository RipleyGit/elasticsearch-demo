package me.leiho.elasticsearch.service;

import com.baomidou.mybatisplus.service.IService;
import me.leiho.elasticsearch.entity.PfHotel;
import me.leiho.elasticsearch.vo.req.QueryOnePageHotelReq;

import java.util.List;

/**
 * <p>
 * 腾住酒店表 服务类
 * </p>
 *
 * @author 萧大俠
 * @since 2018-06-28
 */
public interface PfHotelService extends IService<PfHotel> {
    List<PfHotel> getOnePageHotel(QueryOnePageHotelReq req);
}
