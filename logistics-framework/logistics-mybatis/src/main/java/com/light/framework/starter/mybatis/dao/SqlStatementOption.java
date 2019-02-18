package com.light.framework.starter.mybatis.dao;

import com.light.framework.starter.base.sql.PageBo;

import java.util.List;
import java.util.Map;

/**
 * 根据 xml中定义的查询 ID操作记录
 */
public interface SqlStatementOption  {

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
    int insertBySql(String statementId, Object parameterMap);

    /**
     * 批量插入
     *
     * @param statementId
     * @param parameters
     * @return
     */
    int insertBatchBySql(String statementId, List<Object> parameters);
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
    int updateBySql(String statementId, Map<String, Object> parameterMap);

    /**
     * 批量更新记录
     *
     * @param statementId
     * @param parameters
     * @return
     */
    int updateBySql(String statementId, List<Map<String, Object>> parameters);


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
    <E> List<E> selectListBySql(String statementId);

    /**
     * 带条件查询
     *
     * @param statementId
     * @param parameterMap
     * @param <E>
     * @return
     */
    <E> List<E> selectListBySql(String statementId, Map<String, Object> parameterMap);

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
    <K, V> Map<K, V> selectMapBySql(String statementId, Map<String, Object> parameterMap, String mapKey);

    /**
     * 查询记录返回 Map类型
     *
     * @param statementId
     * @param mapKey      {@link MybatisDaoImpl 的doc解释}
     * @param <K>
     * @param <V>
     * @return
     */
    <K, V> Map<K, V> selectMapBySql(String statementId, String mapKey);

    /**
     * 查询一条记录
     *
     * @param statementId
     * @param <E>
     * @return
     */
    <E> E selectOneBySql(String statementId);

    /**
     * 带条件查询一条记录
     *
     * @param statementId
     * @param parameterMap
     * @param <E>
     * @return
     */
    <E> E selectOneBySql(String statementId, Map<String, Object> parameterMap);

    /**
     * 不带条件查询，返回的不是一个分页对象，不会显示总记录数
     *
     * @param statementId
     * @param page
     * @param pageSize
     * @param <E>
     * @return
     */
    <E> List<E> selectPageBySql(String statementId, int page, int pageSize);

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
    <E> List<E> selectPageBySql(String statementId, Map<String, Object> parameterMap, int page, int pageSize);

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
    <K, V> Map<K, V> selectMapPageBySql(String statementId, Map<String, Object> parameterMap, String mapKey, int page, int pageSize);

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
    int deleteBySql(String statementId, Map<String, Object> parameterMap);

    /**
     * 批量删除记录
     *
     * @param statementId
     * @param parameters
     * @return
     */
    int deleteBySql(String statementId, List<Map<String, Object>> parameters);

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
    <E> PageBo<E> selectPageBySql(String statementId, String statementCountId, int page, int pageSize);

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
    <E> PageBo<E> selectPageBySql(String statementId, String statementCountId, Map<String, Object> parameterMap, int page, int pageSize);
}
