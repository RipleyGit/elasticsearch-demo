package me.leiho.elasticsearch.handler;

import com.baomidou.mybatisplus.mapper.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MPMetaObjectHandler extends MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("数据库插入");
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("数据库更新");
    }


}
