package com.light.framework.starter.mybatis.dao;

import com.baomidou.mybatisplus.enums.SqlMethod;
import com.baomidou.mybatisplus.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageRowBounds;
import com.light.framework.starter.base.sql.BaseEntity;
import com.light.framework.starter.base.sql.PageBo;
import com.light.framework.starter.mybatis.mapper.PlusEntityWrapper;
import com.light.framework.starter.mybatis.tools.MapperHelper;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * mybatis 操作curd
 * Created by ZHL on 17/4/6.
 */
@Service
public class MybatisDaoImpl implements MybatisDao {
    private static MybatisDaoImpl mybatisDao;
    private SqlSessionTemplate defaultSqlSession;

    public MybatisDaoImpl(SqlSessionTemplate sqlSession) {
        this.defaultSqlSession = sqlSession;
        if(mybatisDao == null){
            mybatisDao = getMybatisDao();
        }
    }

    public MybatisDaoImpl getMybatisDao() {
        return this;
    }

    private SqlSession getSqlSession() {
        return defaultSqlSession;
    }


    public static MybatisDaoImpl run() {
        Assert.notNull(mybatisDao, "mybatisDao对象没有注入");
        return mybatisDao;
    }

    public static <T extends BaseMapper> SqlStatementOption of(Class<T> mapperClass) {
        return run().getSqlStatementOption(mapperClass);
    }

    public <T extends BaseMapper> SqlStatementOption getSqlStatementOption(Class<T> mapperClass) {
        return new SqlStatementOptionImpl(mapperClass);
    }

    @Override
    public <T extends BaseMapper> T getMapper(Class<T> tClass) {
        return MapperHelper.getMapperInstanceByMapperClass(tClass, getSqlSession());
    }

    @Override
    public BaseMapper getMapper(BaseEntity var1) {
        return MapperHelper.getMapperInstanceByEntity(var1, getSqlSession());
    }

    /**
     * ---------使用sql语句查询-------start----------------
     */
    protected <E> List<E> selectListBySql(String statementId) {
        return getSqlSession().selectList(statementId);
    }

    protected <E> List<E> selectListBySql(String statementId, Map<String, Object> parameterMap) {
        return getSqlSession().selectList(statementId, parameterMap);
    }

    protected <K, V> Map<K, V> selectMapBySql(String statementId, Map<String, Object> parameterMap, String mapKey) {
        return getSqlSession().selectMap(statementId, parameterMap, mapKey);
    }

    protected <K, V> Map<K, V> selectMapBySql(String statementId, String mapKey) {
        return getSqlSession().selectMap(statementId, mapKey);
    }

    protected <T> T selectOneBySql(String statementId) {
        List<T> list = getSqlSession().selectList(statementId);
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    protected <T> T selectOneBySql(String statementId, Map<String, Object> parameterMap) {
        List<T> list = getSqlSession().selectList(statementId, parameterMap);
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    protected int insertBySql(String statementId, Object parameterMap) {
        return getSqlSession().insert(statementId, parameterMap);
    }

    protected int deleteBySql(String statementId, Map<String, Object> parameterMap) {
        return getSqlSession().delete(statementId, parameterMap);
    }

    protected int updateBySql(String statementId, Map<String, Object> parameterMap) {
        return getSqlSession().update(statementId, parameterMap);
    }

    @Override
    public <T extends BaseEntity> Integer delete(Wrapper<T> wrapper) {
        Assert.isTrue(wrapper instanceof PlusEntityWrapper, "wrapper需要PlusEntityWrapper类型");
        Class<T> tcalss = ((PlusEntityWrapper) wrapper).getEntityClass();
        return this.delete(tcalss, wrapper);
    }

    /**
     * ---------使用sql语句查询-------end------------------
     */

    /**
     * ---------INSERT-------end------------------
     */
    @Override
    public Integer insert(BaseEntity entity) {
        return getMapper(entity).insert(entity);
    }

    @Override
    public Integer insertAllColumn(BaseEntity entity) {
        return getMapper(entity).insertAllColumn(entity);
    }

    @Override
    public <T extends BaseEntity> boolean insertBatch(List<T> entityList) {
        return this.insertBatch(entityList, 30);
    }

    /**
     * 批量插入数据
     *
     * @param entityList
     * @param batchSize  按每batchSize条为一组进行预插入
     * @param <T>
     * @return
     */
    @Override
    public <T extends BaseEntity> boolean insertBatch(List<T> entityList, int batchSize) {
        Assert.notEmpty(entityList, "插入数据不能为空");
        Class tclass = entityList.get(0).getClass();
        try {
            SqlSession batchSqlSession = SqlHelper.sqlSessionBatch(tclass);
            int size = entityList.size();
            String sqlStatement = SqlHelper.table(tclass).getSqlStatement(SqlMethod.INSERT_ONE.getMethod());
            for (int i = 0; i < size; i++) {
                batchSqlSession.insert(sqlStatement, entityList.get(i));
                if (i >= 1 && i % batchSize == 0) {
                    batchSqlSession.flushStatements();
                }
            }
            batchSqlSession.flushStatements();
        } catch (Throwable e) {
            throw new MybatisPlusException("Error: Cannot execute insertBatch Method. Cause", e);
        }
        return true;
    }

    /**
     * ---------UPDATE-------end------------------
     */
    @Override
    public Integer updateById(BaseEntity entity) {
        return getMapper(entity).updateById(entity);
    }

    @Override
    public Integer updateAllColumnById(BaseEntity entity) {
        return getMapper(entity).updateAllColumnById(entity);
    }

    @Override
    public <T extends BaseEntity> Integer update(BaseEntity entity, Wrapper<T> wrapper) {
        return getMapper(entity).update(entity, wrapper);
    }

    @Override
    public <T extends BaseEntity> Integer updateForSet(Class<T> tClass, String setStr, Wrapper<T> wrapper) {
        return MapperHelper.getMapperInstanceByEntityClass(tClass, getSqlSession()).updateForSet(setStr, wrapper);
    }

    @Override
    public <T extends BaseEntity> Integer updateForSet(String setStr, Wrapper<T> wrapper) {
        Assert.isTrue(wrapper instanceof PlusEntityWrapper, "wrapper需要PlusEntityWrapper类型");
        Class<T> tcalss = ((PlusEntityWrapper) wrapper).getEntityClass();
        return this.updateForSet(tcalss, setStr, wrapper);
    }

    @Override
    public <T extends BaseEntity> boolean updateBatchById(List<T> entityList) {
        return updateBatchById(entityList, 30);
    }

    @Override
    public <T extends BaseEntity> boolean updateBatchById(List<T> entityList, int batchSize) {
        return updateBatchById(entityList, batchSize, true);
    }

    @Override
    public <T extends BaseEntity> boolean updateAllColumnBatchById(List<T> entityList) {
        return updateAllColumnBatchById(entityList, 30);
    }

    @Override
    public <T extends BaseEntity> boolean updateAllColumnBatchById(List<T> entityList, int batchSize) {
        return updateBatchById(entityList, batchSize, false);
    }

    /**
     * 根据主键ID进行批量修改
     *
     * @param entityList 实体对象列表
     * @param batchSize  批量刷新个数
     * @param selective  是否滤掉空字段
     * @return boolean
     */
    private <T extends BaseEntity> boolean updateBatchById(List<T> entityList, int batchSize, boolean selective) {
        Assert.notEmpty(entityList, "插入数据不能为空");
        Class tclass = entityList.get(0).getClass();
        try {
            SqlSession batchSqlSession = SqlHelper.sqlSessionBatch(tclass);
            int size = entityList.size();
            SqlMethod sqlMethod = selective ? SqlMethod.UPDATE_BY_ID : SqlMethod.UPDATE_ALL_COLUMN_BY_ID;
            String sqlStatement = SqlHelper.table(tclass).getSqlStatement(sqlMethod.getMethod());
            for (int i = 0; i < size; i++) {
                MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<T>();
                param.put("et", entityList.get(i));
                batchSqlSession.update(sqlStatement, param);
                if (i >= 1 && i % batchSize == 0) {
                    batchSqlSession.flushStatements();
                }
            }
            batchSqlSession.flushStatements();
        } catch (Throwable e) {
            throw new MybatisPlusException("Error: Cannot execute updateBatchById Method. Cause", e);
        }
        return true;
    }

    /**
     * ---------SELECT-------end------------------
     */
    @Override
    public <T extends BaseEntity> T selectById(Class<T> tClass, Serializable id) {
        return (T) MapperHelper.getMapperInstanceByEntityClass(tClass, getSqlSession()).selectById(id);
    }

    @Override
    public <T extends BaseEntity> List<T> selectBatchIds(Class<T> tClass, Collection<? extends Serializable> idList) {
        return MapperHelper.getMapperInstanceByEntityClass(tClass, getSqlSession()).selectBatchIds(idList);
    }

    @Override
    public <T extends BaseEntity> T selectOne(T entity) {
        return (T) getMapper(entity).selectOne(entity);
    }

    @Override
    public <T extends BaseEntity> Integer selectCount(Class<T> tClass, Wrapper<T> wrapper) {
        return MapperHelper.getMapperInstanceByEntityClass(tClass, getSqlSession()).selectCount(wrapper);
    }

    @Override
    public <T extends BaseEntity> List<T> selectList(Class<T> tClass, Wrapper<T> wrapper) {
        return MapperHelper.getMapperInstanceByEntityClass(tClass, getSqlSession()).selectList(wrapper);
    }

    @Override
    public <T extends BaseEntity> List<Map<String, Object>> selectMaps(Class<T> tClass, Wrapper<T> wrapper) {
        return MapperHelper.getMapperInstanceByEntityClass(tClass, getSqlSession()).selectMaps(wrapper);
    }

    @Override
    public <T extends BaseEntity> Integer selectAllCount(Class<T> tClass) {
        return this.selectCount(tClass, null);
    }

    @Override
    public <T extends BaseEntity> List<T> selectAllList(Class<T> tClass) {
        return this.selectList(tClass, null);
    }

    @Override
    public <T extends BaseEntity> List<T> selectList(T entity) {
        EntityWrapper<T> wrapper = new EntityWrapper(entity);
        return getMapper(entity).selectList(wrapper);
    }

    @Override
    public <T extends BaseEntity> Integer selectCount(Wrapper<T> wrapper) {
        Assert.isTrue(wrapper instanceof PlusEntityWrapper, "wrapper需要PlusEntityWrapper类型");
        Class<T> tcalss = ((PlusEntityWrapper) wrapper).getEntityClass();
        return this.selectCount(tcalss, wrapper);
    }

    @Override
    public <T extends BaseEntity> List<T> selectList(Wrapper<T> wrapper) {
        Assert.isTrue(wrapper instanceof PlusEntityWrapper, "wrapper需要PlusEntityWrapper类型");
        Class<T> tcalss = ((PlusEntityWrapper) wrapper).getEntityClass();
        return this.selectList(tcalss, wrapper);
    }

    @Override
    public <T extends BaseEntity> T selectOne(Wrapper<T> wrapper) {
        List<T> list = selectList(wrapper);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    @Override
    public <T extends BaseEntity> List<Map<String, Object>> selectMaps(Wrapper<T> wrapper) {
        Assert.isTrue(wrapper instanceof PlusEntityWrapper, "wrapper需要PlusEntityWrapper类型");
        Class<T> tcalss = ((PlusEntityWrapper) wrapper).getEntityClass();
        return this.selectMaps(tcalss, wrapper);
    }

    /**
     * ---------DELETE-------end------------------
     */
    @Override
    public <T extends BaseEntity> Integer deleteById(Class<T> tClass, Serializable id) {
        return MapperHelper.getMapperInstanceByEntityClass(tClass, getSqlSession()).deleteById(id);
    }

    @Override
    public <T extends BaseEntity> Integer delete(Class<T> tClass, Wrapper<T> wrapper) {
        return MapperHelper.getMapperInstanceByEntityClass(tClass, getSqlSession()).delete(wrapper);
    }

    @Override
    public <T extends BaseEntity> Integer delete(T entity) {
        EntityWrapper<T> wrapper = new EntityWrapper(entity);
        return getMapper(entity).delete(wrapper);
    }

    @Override
    public <T extends BaseEntity> Integer deleteBatchIds(Class<T> tClass, Collection<? extends Serializable> idList) {
        return MapperHelper.getMapperInstanceByEntityClass(tClass, getSqlSession()).deleteBatchIds(idList);
    }

    /**
     * ---------使用page查询-------start------------------
     */
    protected <E> List<E> selectPageBySql(String statementId, int page, int pageSize) {
        return getSqlSession().selectList(statementId, new RowBounds(page, pageSize));
    }

    protected <E> List<E> selectPageBySql(String statementId, Map<String, Object> parameterMap, int page, int pageSize) {
        return getSqlSession().selectList(statementId, parameterMap, new RowBounds(page, pageSize));
    }

    protected <K, V> Map<K, V> selectMapPageBySql(String statementId, Map<String, Object> parameterMap, String mapKey, int page, int pageSize) {
        return getSqlSession().selectMap(statementId, parameterMap, mapKey, new RowBounds(page, pageSize));
    }

    protected <T> PageBo<T> selectPageBySql(String statementId, String statementCountId, int page, int pageSize) {
        RowBounds var2 = new RowBounds(page, pageSize);
        int count = selectOneBySql(statementCountId);
        Page<T> pageInfo = null;
        if (count > 0) {
            List<T> list = selectPageBySql(statementId, page, pageSize);
            pageInfo = new Page();
            pageInfo.setRecords(list);
            pageInfo.setTotal(count);
        } else {
            pageInfo = new Page();
            pageInfo.setTotal(0);
        }
        pageInfo.setCurrent(var2.getOffset());//当前页
        pageInfo.setSize(var2.getLimit());
        long pages = (pageInfo.getTotal() + pageInfo.getSize() - 1) / pageInfo.getSize();
        return MapperHelper.toPageBo(pageInfo, Long.valueOf(pages).intValue());
    }

    protected <T> PageBo<T> selectPageBySql(String statementId, String statementCountId, Map<String, Object> parameterMap, int page, int pageSize) {
        RowBounds rowBounds = new RowBounds(page, pageSize);
        int count = selectOneBySql(statementCountId, parameterMap);
        Page<T> pageInfo = null;
        if (count > 0) {
            List<T> list = selectPageBySql(statementId, parameterMap, page, pageSize);
            pageInfo = new Page();
            pageInfo.setRecords(list);
            pageInfo.setTotal(count);
        } else {
            pageInfo = new Page();
            pageInfo.setTotal(0);
        }
        pageInfo.setCurrent(rowBounds.getOffset());
        pageInfo.setSize(rowBounds.getLimit());
        long pages = (pageInfo.getTotal() + pageInfo.getSize() - 1) / pageInfo.getSize();
        return MapperHelper.toPageBo(pageInfo, Long.valueOf(pages).intValue());
    }

    @Override
    public <T extends BaseEntity> List<T> selectPage(Class<T> tClass, RowBounds rowBounds, Wrapper<T> wrapper) {
        return MapperHelper.getMapperInstanceByEntityClass(tClass, getSqlSession()).selectPage(rowBounds, wrapper);
    }

    @Override
    public <T extends BaseEntity> List<Map<String, Object>> selectMapsPage(Class<T> tClass, RowBounds rowBounds, Wrapper<T> wrapper) {
        return MapperHelper.getMapperInstanceByEntityClass(tClass, getSqlSession()).selectMapsPage(rowBounds, wrapper);
    }

    @Override
    public <T extends BaseEntity> List<T> selectPage(Wrapper<T> wrapper, int page, int pageSize) {
        Assert.isTrue(wrapper instanceof PlusEntityWrapper, "wrapper需要PlusEntityWrapper类型");
        Class<T> tcalss = ((PlusEntityWrapper) wrapper).getEntityClass();
        RowBounds rowBounds = new RowBounds(page, pageSize);
        return this.selectPage(tcalss, rowBounds, wrapper);
    }

    @Override
    public <T extends BaseEntity> List<Map<String, Object>> selectMapsPage(Wrapper<T> wrapper, int page, int pageSize) {
        Assert.isTrue(wrapper instanceof PlusEntityWrapper, "wrapper需要PlusEntityWrapper类型");
        Class<T> tcalss = ((PlusEntityWrapper) wrapper).getEntityClass();
        RowBounds rowBounds = new RowBounds(page, pageSize);
        return this.selectMapsPage(tcalss, rowBounds, wrapper);
    }

    @Override
    public <T extends BaseEntity> PageBo<T> selectPageBo(Wrapper<T> wrapper, int page, int pageSize) {
        PageRowBounds pageRowBounds = new PageRowBounds(page, pageSize);
        Assert.isTrue(wrapper instanceof PlusEntityWrapper, "wrapper需要PlusEntityWrapper类型");
        Class<T> tcalss = ((PlusEntityWrapper) wrapper).getEntityClass();
        List<T> list = this.selectPage(tcalss, pageRowBounds, wrapper);
        long count = pageRowBounds.getTotal();
        Page<T> pageInfo = new Page<T>(page / pageSize + 1, pageSize);
        if (count > 0) {
            pageInfo.setTotal(count);
            pageInfo.setRecords(list);
        } else {
            pageInfo.setTotal(0);
        }
        long pages = (pageInfo.getTotal() + pageInfo.getSize() - 1) / pageInfo.getSize();
        return MapperHelper.toPageBo(pageInfo, Long.valueOf(pages).intValue());
    }

}
