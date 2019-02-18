package com.light.lgts.config.system;

import com.baomidou.mybatisplus.mapper.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MyMetaObjectHandler extends MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        /*Object fieldType = getFieldValByName("gmtCreate", metaObject);//mybatis-plus版本2.0.9+
        if (fieldType == null) {
            setFieldValByName("gmtCreate", new Date(System.currentTimeMillis()), metaObject);//mybatis-plus版本2.0.9+
        }*/
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        /*Object fieldType = getFieldValByName("gmtModified", metaObject);//mybatis-plus版本2.0.9+
        if (fieldType == null) {
            setFieldValByName("gmtModified", new Date(System.currentTimeMillis()), metaObject);//mybatis-plus版本2.0.9+
        }*/
    }
}