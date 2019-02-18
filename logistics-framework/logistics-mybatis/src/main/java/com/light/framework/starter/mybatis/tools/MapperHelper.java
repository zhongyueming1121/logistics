package com.light.framework.starter.mybatis.tools;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.light.framework.starter.base.sql.BaseEntity;
import com.light.framework.starter.base.sql.MapperInterface;
import com.light.framework.starter.base.sql.PageBo;
import com.light.framework.starter.mybatis.auto.configuration.MybatisApplicationContext;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZHL on 17/4/6.
 */
public class MapperHelper {
    static Logger logger = LoggerFactory.getLogger(MapperHelper.class);

    public static <Entity extends BaseEntity> BaseMapper getMapperInstanceByEntity(Entity entity, SqlSession sqlSession) {
        Assert.notNull(entity, "mybatis 实体对象不能为空");
        return getMapperInstanceByEntityClass(entity.getClass(), sqlSession);
    }

    public static <Entity extends BaseEntity> BaseMapper getMapperInstanceByEntityClass(Class<Entity> entityClass, SqlSession sqlSession) {
        MapperInterface mapperInterface = entityClass.getAnnotation(MapperInterface.class);
        Assert.notNull(mapperInterface, "实体 ==> ".concat(entityClass.getSimpleName()).concat("未添加注解 @MapperInterface"));
        String mclassName = mapperInterface.value();
        Assert.hasLength(mclassName, "实体 ==> ".concat(entityClass.getSimpleName()).concat("未添加注解 @MapperInterface值"));
        Class mclass = null;
        try {
            mclass = Class.forName(mclassName);
        } catch (ClassNotFoundException e) {
            logger.error("实体 ==> {}根据@MapperInterface的值{}未找到类", entityClass.getSimpleName(), mclassName, e);
            return null;
        }
        Assert.notNull(mclassName, "实体 ==> ".concat(entityClass.getSimpleName()).concat("未找到 Mapper类"));
        Assert.isTrue(BaseMapper.class.isAssignableFrom(mclass), "实体 ==> ".concat(entityClass.getSimpleName())
                .concat("注解 @MapperInterface值未继承或实现 ").concat(BaseMapper.class.getName()));
        return getMapperInstanceByMapperClass(mclass, sqlSession);
    }

    /**首先从 ioc 中获取对象，如果获取不到再通过sqlSession重新生成代理对象。
     * @param mapperClass
     * @param sqlSession
     * @param <T>
     * @return
     */
    public static <T extends BaseMapper> T getMapperInstanceByMapperClass(Class<T> mapperClass, SqlSession sqlSession) {
        T mapper = MybatisApplicationContext.getBean(mapperClass);
        if (mapper != null) {
            return mapper;
        }
        mapper = sqlSession.getMapper(mapperClass);
        Assert.notNull(mapper, "未找到mybatis mapper对象");
        Assert.isTrue(mapper instanceof BaseMapper, mapperClass.getName().concat(" ->此mapper未继承BaseMapper"));
        return mapper;
    }

    /**
     * 转换分页对象
     *
     * @param pageInfo
     * @param pageNum
     * @param <E>
     * @return
     */
    public static <E> PageBo<E> toPageBo(Page<E> pageInfo, int pageNum) {
        if (pageInfo == null) {
            return null;
        }
        List<E> list = new ArrayList<E>();
        if (pageInfo.getSize() > 0) {
            list.addAll(pageInfo.getRecords());
        }
        PageBo<E> pageBo = new PageBo<E>(pageInfo.getTotal(), pageNum, list);
        pageBo.setPage(pageInfo.getCurrent());
        pageBo.setPageSize(pageInfo.getSize());
        pageBo.setSize(list.size());
        return pageBo;
    }
}
