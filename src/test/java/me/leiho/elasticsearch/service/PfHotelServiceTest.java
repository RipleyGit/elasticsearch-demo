package me.leiho.elasticsearch.service;

import me.leiho.elasticsearch.ESApplication;
import me.leiho.elasticsearch.vo.req.QueryOnePageHotelReq;
import lombok.extern.slf4j.Slf4j;
import me.leiho.elasticsearch.ESApplication;
import me.leiho.elasticsearch.vo.req.QueryOnePageHotelReq;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ESApplication.class)
@WebAppConfiguration
public class PfHotelServiceTest {
    @Autowired
    private PfHotelService pfHotelService;

    @Test
    public void getOnePageHotel() {
        QueryOnePageHotelReq req = new QueryOnePageHotelReq();
        req.setIndex(1);
        req.setSize(10);
        req.setOrderBy("");
        req.setCity("757");
        req.setStar("1");
        req.setKeyWord("");
        System.out.println(pfHotelService.getOnePageHotel(req));
    }
}