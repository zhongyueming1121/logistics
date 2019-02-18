package com.light.framework.starter.mybatis.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.light.framework.starter.base.sql.BaseEntity;
import com.light.framework.starter.base.sql.PageBo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by ZHL on 17/4/6.
 */
public interface MybatisDao {

    <T extends BaseMapper> T getMapper(Class<T> tClass);

    <T extends BaseMapper> T getMapper(BaseEntity var1);
    /**-------------------------------------
     * ---------INSERT----------------------
     * -------------------------------------
     * */
    /**
     * <p>
     * 插入一条记录 实体字段属性值为 null 的不会插入到数据库表中
     * </p>
     *
     * @param entity 实体对象
     * @return int
     */
    Integer insert(BaseEntity entity);

    /**
     * <p>
     * 插入一条记录 实体字段属性值为 null 的也会插入到数据库表中
     * </p>
     *
     * @param entity 实体对象
     * @return int
     */
    Integer insertAllColumn(BaseEntity entity);

    /**
     * 批量插入数据 默认30条为一组
     *
     * @param entityList
     * @param <T>
     * @return
     */
    <T extends BaseEntity> boolean insertBatch(List<T> entityList);

    /**
     * 批量插入数据
     *
     * @param entityList
     * @param batchSize  按每batchSize为一组进行预插入
     * @param <T>
     * @return
     */
    <T extends BaseEntity> boolean insertBatch(List<T> entityList, int batchSize);
    /**-------------------------------------
     * ---------UPDATE----------------------
     * -------------------------------------
     * */

    /**
     * <p>
     * 根据 ID 修改 实体字段属性值为 null 的不会更新到数据库表中
     * </p>
     *
     * @param entity 实体对象
     * @return int
     */
    Integer updateById(@Param("et") BaseEntity entity);

    /**
     * <p>
     * 根据 ID 修改 实体字段属性值为 null 的会更新到数据库表中
     * </p>
     *
     * @param entity 实体对象
     * @return int
     */
    Integer updateAllColumnById(@Param("et") BaseEntity entity);

    /**
     * <p>
     * 根据 whereEntity 条件，更新记录
     * </p>
     *
     * @param entity  实体对象
     * @param wrapper 实体对象封装操作类（可以为 null）
     * @return
     */
    <T extends BaseEntity> Integer update(@Param("et") BaseEntity entity, @Param("ew") Wrapper<T> wrapper);

    /**
     * <p>
     * 根据 whereEntity 条件，更新记录 更新的字段根据setStr来拼接
     * </p>
     * 例子：将所有记录的 name 字段设置为  我要测试
     * updateForSet("name = '我要测试'", null);
     * 例子：更新多个字段
     * updateForSet("name = '我要测试', sex=1", null);
     *
     * @param tClass
     * @param setStr  set字符串
     * @param wrapper 实体对象封装操作类（可以为 null）
     * @return
     */
    <T extends BaseEntity> Integer updateForSet(Class<T> tClass, @Param("setStr") String setStr, @Param("ew") Wrapper<T> wrapper);

    /**
     * wrapper需要是cn.kingnet.framework.starter.mybatis.mapper.PlusEntityWrapper
     *
     * @param setStr
     * @param wrapper
     * @param <T>
     * @return
     */
    <T extends BaseEntity> Integer updateForSet(@Param("setStr") String setStr, @Param("ew") Wrapper<T> wrapper);

    /**
     * <p>
     * 根据ID 批量更新
     * </p>
     *
     * @param entityList 实体对象列表
     * @return boolean
     */
    <T extends BaseEntity> boolean updateBatchById(List<T> entityList);

    /**
     * <p>
     * 根据ID 批量更新
     * </p>
     *
     * @param entityList 实体对象列表
     * @param batchSize  更新批次数量
     * @return boolean
     */
    <T extends BaseEntity> boolean updateBatchById(List<T> entityList, int batchSize);

    /**
     * <p>
     * 根据ID 批量更新全部字段
     * </p>
     *
     * @param entityList 实体对象列表
     * @return boolean
     */
    <T extends BaseEntity> boolean updateAllColumnBatchById(List<T> entityList);

    /**
     * <p>
     * 根据ID 批量更新全部字段
     * </p>
     *
     * @param entityList 实体对象列表
     * @param batchSize  更新批次数量
     * @return boolean
     */
    <T extends BaseEntity> boolean updateAllColumnBatchById(List<T> entityList, int batchSize);
    /**-------------------------------------
     * ---------SELECT----------------------
     * -------------------------------------
     * */
    /**
     * <p>
     * 根据 ID 查询
     * </p>
     *
     * @param id 主键ID
     * @return T
     */
    <T extends BaseEntity> T selectById(Class<T> tClass, Serializable id);

    /**
     * <p>
     * 查询（根据ID 批量查询）
     * </p>
     *
     * @param idList 主键ID列表
     * @return List<T>
     */
    <T extends BaseEntity> List<T> selectBatchIds(Class<T> tClass, @Param("coll") Collection<? extends Serializable> idList);

    /**
     * <p>
     * 根据 entity 条件，查询一条记录
     * </p>
     *
     * @param entity 实体对象
     * @return T
     */
    <T extends BaseEntity> T selectOne(@Param("ew") T entity);

    /**
     * <p>
     * 根据 Wrapper 条件，查询总记录数
     * </p>
     *
     * @param wrapper 实体对象
     * @return int
     */
    <T extends BaseEntity> Integer selectCount(Class<T> tClass, @Param("ew") Wrapper<T> wrapper);

    /**
     * wrapper需要是cn.kingnet.framework.starter.mybatis.mapper.PlusEntityWrapper
     *
     * @param wrapper
     * @param <T>
     * @return
     */
    <T extends BaseEntity> Integer selectCount(@Param("ew") Wrapper<T> wrapper);

    /**
     * <p>
     * 根据 entity 条件，查询全部记录
     * </p>
     *
     * @param tClass
     * @param wrapper 实体对象封装操作类（可以为 null）
     * @return List<T>
     */
    <T extends BaseEntity> List<T> selectList(Class<T> tClass, @Param("ew") Wrapper<T> wrapper);

    /**
     * wrapper需要是cn.kingnet.framework.starter.mybatis.mapper.PlusEntityWrapper
     *
     * @param wrapper
     * @param <T>
     * @return
     */
    <T extends BaseEntity> List<T> selectList(@Param("ew") Wrapper<T> wrapper);

    /**
     * wrapper需要是cn.kingnet.framework.starter.mybatis.mapper.PlusEntityWrapper
     *
     * @param wrapper
     * @param <T>
     * @return
     */
    <T extends BaseEntity> T selectOne(@Param("ew") Wrapper<T> wrapper);

    /**
     * <p>
     * 根据 Wrapper 条件，查询总记录数
     * </p>
     *
     * @param tClass
     * @return int
     */
    <T extends BaseEntity> Integer selectAllCount(Class<T> tClass);

    /**
     * <p>
     * 根据 entity 条件，查询全部记录
     * </p>
     *
     * @param tClass
     * @return List<T>
     */
    <T extends BaseEntity> List<T> selectAllList(Class<T> tClass);

    /**
     * <p>
     * 根据 entity 条件，查询全部记录
     * </p>
     *
     * @param entity
     * @return List<T>
     */
    <T extends BaseEntity> List<T> selectList(T entity);

    /**
     * <p>
     * 根据 Wrapper 条件，查询全部记录
     * </p>
     *
     * @param wrapper 实体对象封装操作类（可以为 null）
     * @return List<T>
     */
    <T extends BaseEntity> List<Map<String, Object>> selectMaps(Class<T> tClass, @Param("ew") Wrapper<T> wrapper);

    /**
     * wrapper需要是cn.kingnet.framework.starter.mybatis.mapper.PlusEntityWrapper
     *
     * @param wrapper
     * @param <T>
     * @return
     */
    <T extends BaseEntity> List<Map<String, Object>> selectMaps(@Param("ew") Wrapper<T> wrapper);
    /**-------------------------------------
     * ---------DELETE----------------------
     * -------------------------------------
     * */
    /**
     * <p>
     * 根据 ID 删除
     * </p>
     *
     * @param id 主键ID
     * @return int
     */
    <T extends BaseEntity> Integer deleteById(Class<T> tClass, Serializable id);

    /**
     * <p>
     * 根据 entity 条件，删除记录
     * </p>
     *
     * @param wrapper 实体对象封装操作类（可以为 null）
     * @return int
     */
    <T extends BaseEntity> Integer delete(Class<T> tClass, @Param("ew") Wrapper<T> wrapper);

    /**
     * wrapper需要是cn.kingnet.framework.starter.mybatis.mapper.PlusEntityWrapper
     *
     * @param wrapper
     * @param <T>
     * @return
     */
    <T extends BaseEntity> Integer delete(@Param("ew") Wrapper<T> wrapper);

    /**
     * <p>
     * 根据 entity 条件，删除记录
     * </p>
     *
     * @param entity
     * @return int
     */
    <T extends BaseEntity> Integer delete(T entity);


    /**
     * <p>
     * 删除（根据ID 批量删除）
     * </p>
     *
     * @param idList 主键ID列表
     * @return int
     */
    <T extends BaseEntity> Integer deleteBatchIds(Class<T> tClass, @Param("coll") Collection<? extends Serializable> idList);
    /**------------------------------------------
     * ---------使用page查询----------------------
     * ------------------------------------------
     * */
    /**
     * <p>
     * 根据 entity 条件，查询全部记录（并翻页）
     * </p>
     *
     * @param rowBounds 分页查询条件（可以为 RowBounds.DEFAULT）
     * @param wrapper   实体对象封装操作类（可以为 null）
     * @return List<T>
     */
    <T extends BaseEntity> List<T> selectPage(Class<T> tClass, RowBounds rowBounds, @Param("ew") Wrapper<T> wrapper);

    /**
     * wrapper需要是cn.kingnet.framework.starter.mybatis.mapper.PlusEntityWrapper
     *
     * @param wrapper
     * @param page     从0开始算起 数据库对应的offset    跟分页插件定义的pagehelper.offsetAsPageNum=true值有关
     * @param pageSize
     * @param <T>
     * @return
     */
    <T extends BaseEntity> List<T> selectPage(@Param("ew") Wrapper<T> wrapper, int page, int pageSize);

    /**
     * <p>
     * 根据 Wrapper 条件，查询全部记录（并翻页）
     * </p>
     *
     * @param rowBounds 分页查询条件（可以为 RowBounds.DEFAULT）
     * @param wrapper   实体对象封装操作类
     */
    <T extends BaseEntity> List<Map<String, Object>> selectMapsPage(Class<T> tClass, RowBounds rowBounds, @Param("ew") Wrapper<T> wrapper);

    /**
     * wrapper需要是cn.kingnet.framework.starter.mybatis.mapper.PlusEntityWrapper
     *
     * @param wrapper
     * @param page     从0开始算起 数据库对应的offset    跟分页插件定义的pagehelper.offsetAsPageNum=true值有关
     * @param pageSize
     * @param <T>
     * @return
     */
    <T extends BaseEntity> List<Map<String, Object>> selectMapsPage(@Param("ew") Wrapper<T> wrapper, int page, int pageSize);

    /**
     * @param wrapper
     * @param page     从0开始算起 数据库对应的offset    跟分页插件定义的pagehelper.offsetAsPageNum=true值有关
     * @param pageSize
     * @param <T>
     * @return
     */
    <T extends BaseEntity> PageBo<T> selectPageBo(Wrapper<T> wrapper, int page, int pageSize);
}
