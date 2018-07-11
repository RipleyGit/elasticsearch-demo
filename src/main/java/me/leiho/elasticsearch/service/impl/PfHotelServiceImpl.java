package me.leiho.elasticsearch.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import me.leiho.elasticsearch.entity.PfHotel;
import me.leiho.elasticsearch.mapper.PfHotelMapper;
import me.leiho.elasticsearch.service.PfHotelService;
import me.leiho.elasticsearch.vo.req.QueryOnePageHotelReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 腾住酒店表 服务实现类
 * </p>
 *
 * @author 萧大俠
 * @since 2018-06-28
 */
@Service
public class PfHotelServiceImpl extends ServiceImpl<PfHotelMapper, PfHotel> implements PfHotelService {

    @Autowired
    private PfHotelMapper pfHotelMapper;
    @Override
    public List<PfHotel> getOnePageHotel(QueryOnePageHotelReq req) {
        return pfHotelMapper.selectPage(new Page<>(req.getIndex(),req.getSize()),new EntityWrapper<PfHotel>().eq("city",req.getCity()).eq("star",req.getStar()));
    }
}
