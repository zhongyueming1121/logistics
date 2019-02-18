package com.light.framework.starter.mybatis.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.light.framework.starter.base.sql.PageBo;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 根据 xml中定义的查询 ID操作记录
 */
public class SqlStatementOptionImpl<T extends BaseMapper> implements SqlStatementOption {
    private String prefix = "";

    public SqlStatementOptionImpl(Class<T> mapper) {
        prefix = mapper.getName().concat(".");
    }


    /**-------------------------------------
     * ---------INSERT----------------------
     * -------------------------------------
     * */
    /**
     * 插入记录
     *
     * @param statementId
     * @param parameterMap
     * @return
     */
    @Override
    public int insertBySql(String statementId, Object parameterMap) {
        return MybatisDaoImpl.run().insertBySql(prefix.concat(statementId), parameterMap);
    }

    /**
     * 批量插入
     *
     * @param statementId
     * @param parameters
     * @return
     */
    @Override
    public int insertBatchBySql(String statementId, List<Object> parameters) {
        int conut = 0;
        for (Object parameter : parameters) {
            conut += this.insertBySql(statementId, parameter);
        }
        return conut;
    }
    /**-------------------------------------
     * ---------UPDATE----------------------
     * -------------------------------------
     * */
    /**
     * 更新记录
     *
     * @param statementId
     * @param parameterMap
     * @return
     */
    @Override
    public int updateBySql(String statementId, Map<String, Object> parameterMap) {
        return MybatisDaoImpl.run().updateBySql(prefix.concat(statementId), parameterMap);
    }

    /**
     * 批量更新记录
     *
     * @param statementId
     * @param parameters
     * @return
     */
    @Override
    public int updateBySql(String statementId, List<Map<String, Object>> parameters) {
        int conut = 0;

        Map parameterMap;
        for (Iterator i$ = parameters.iterator(); i$.hasNext(); conut += this.updateBySql(statementId, parameterMap)) {
            parameterMap = (Map) i$.next();
        }
        return conut;
    }


    /**-------------------------------------
     * ---------SELECT----------------------
     * -------------------------------------
     * */
    /**
     * 简单查询
     *
     * @param statementId
     * @return
     */
    @Override
    public <E> List<E> selectListBySql(String statementId) {
        return MybatisDaoImpl.run().selectListBySql(prefix.concat(statementId));
    }

    /**
     * 带条件查询
     *
     * @param statementId
     * @param parameterMap
     * @param <E>
     * @return
     */
    @Override
    public <E> List<E> selectListBySql(String statementId, Map<String, Object> parameterMap) {
        return MybatisDaoImpl.run().selectListBySql(prefix.concat(statementId), parameterMap);
    }

    /**
     * 带条件查询
     *
     * @param statementId
     * @param parameterMap
     * @param mapKey       {@link MybatisDaoImpl 的doc解释}
     * @param <K>
     * @param <V>
     * @return
     */
    @Override
    public <K, V> Map<K, V> selectMapBySql(String statementId, Map<String, Object> parameterMap, String mapKey) {
        return MybatisDaoImpl.run().selectMapBySql(prefix.concat(statementId), parameterMap, mapKey);
    }

    /**
     * 查询记录返回 Map类型
     *
     * @param statementId
     * @param mapKey      {@link MybatisDaoImpl 的doc解释}
     * @param <K>
     * @param <V>
     * @return
     */
    @Override
    public <K, V> Map<K, V> selectMapBySql(String statementId, String mapKey) {
        return MybatisDaoImpl.run().selectMapBySql(prefix.concat(statementId), mapKey);
    }

    /**
     * 查询一条记录
     *
     * @param statementId
     * @param <E>
     * @return
     */
    @Override
    public <E> E selectOneBySql(String statementId) {
        List<E> list = MybatisDaoImpl.run().selectOneBySql(prefix.concat(statementId));
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    /**
     * 带条件查询一条记录
     *
     * @param statementId
     * @param parameterMap
     * @param <E>
     * @return
     */
    @Override
    public <E> E selectOneBySql(String statementId, Map<String, Object> parameterMap) {
        List<E> list = MybatisDaoImpl.run().selectOneBySql(prefix.concat(statementId), parameterMap);
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    /**
     * 不带条件查询，返回的不是一个分页对象，不会显示总记录数
     *
     * @param statementId
     * @param page
     * @param pageSize
     * @param <E>
     * @return
     */
    @Override
    public <E> List<E> selectPageBySql(String statementId, int page, int pageSize) {
        return MybatisDaoImpl.run().selectPageBySql(prefix.concat(statementId), page, pageSize);
    }

    /**
     * 带条件查询，返回的不是一个分页对象，不会显示总记录数
     *
     * @param statementId
     * @param parameterMap
     * @param page
     * @param pageSize
     * @param <E>
     * @return
     */
    @Override
    public <E> List<E> selectPageBySql(String statementId, Map<String, Object> parameterMap, int page, int pageSize) {
        return MybatisDaoImpl.run().selectPageBySql(prefix.concat(statementId), parameterMap, page, pageSize);
    }

    /**
     * 带条件查询，返回的不是一个分页对象，不会显示总记录数，返回的记录用 map 进行组装
     *
     * @param statementId
     * @param parameterMap
     * @param mapKey       {@link MybatisDaoImpl 的doc解释}
     * @param page
     * @param pageSize
     * @param <K>
     * @param <V>
     * @return
     */
    @Override
    public <K, V> Map<K, V> selectMapPageBySql(String statementId, Map<String, Object> parameterMap, String mapKey, int page, int pageSize) {
        return MybatisDaoImpl.run().selectMapPageBySql(prefix.concat(statementId), parameterMap, mapKey, page, pageSize);
    }

    /**-------------------------------------
     * ---------DELETE----------------------
     * -------------------------------------
     * */
    /**
     * 删除记录
     *
     * @param statementId
     * @param parameterMap
     * @return
     */
    @Override
    public int deleteBySql(String statementId, Map<String, Object> parameterMap) {
        return MybatisDaoImpl.run().deleteBySql(prefix.concat(statementId), parameterMap);
    }

    /**
     * 批量删除记录
     *
     * @param statementId
     * @param parameters
     * @return
     */
    @Override
    public int deleteBySql(String statementId, List<Map<String, Object>> parameters) {
        int conut = 0;

        Map parameterMap;
        for (Iterator i$ = parameters.iterator(); i$.hasNext(); conut += this.deleteBySql(statementId, parameterMap)) {
            parameterMap = (Map) i$.next();
        }

        return conut;
    }

    /**------------------------------------------
     * ---------使用page查询----------------------
     * ------------------------------------------
     * */
    /**
     * 分页查询 不带条件
     *
     * @param statementId
     * @param statementCountId
     * @param page
     * @param pageSize
     * @param <E>
     * @return
     */
    @Override
    public <E> PageBo<E> selectPageBySql(String statementId, String statementCountId, int page, int pageSize) {
        return MybatisDaoImpl.run().selectPageBySql(prefix.concat(statementId), prefix.concat(statementCountId), page, pageSize);
    }

    /**
     * 分页查询 带条件
     *
     * @param statementId
     * @param statementCountId
     * @param parameterMap
     * @param page
     * @param pageSize
     * @param <E>
     * @return
     */
    @Override
    public <E> PageBo<E> selectPageBySql(String statementId, String statementCountId, Map<String, Object> parameterMap, int page, int pageSize) {
        return MybatisDaoImpl.run().selectPageBySql(prefix.concat(statementId), prefix.concat(statementCountId), parameterMap, page, pageSize);
    }
}
