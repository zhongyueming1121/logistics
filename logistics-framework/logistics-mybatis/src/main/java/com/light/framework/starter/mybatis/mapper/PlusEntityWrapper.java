package com.light.framework.starter.mybatis.mapper;

import com.baomidou.mybatisplus.entity.TableFieldInfo;
import com.baomidou.mybatisplus.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.light.framework.starter.mybatis.tools.Reflections;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * EntityWrapper改造
 *
 * @param <T>
 */
public class PlusEntityWrapper<T> extends EntityWrapper<T> {
    private Class<T> entityClass;

    public PlusEntityWrapper(Class<T> entityClass) {
        Assert.notNull(entityClass, "实体类型不能为空");
        this.entityClass = entityClass;
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    private <E, R> String getColumn(Fn<E, R> fn) {
        final String name = Reflections.fnToFieldName(fn);
        try {
            Optional<TableFieldInfo> fileinfo = SqlHelper.table(getEntityClass()).getFieldList().stream().filter(f -> name.equals(f.getProperty())).findFirst();
            String column = fileinfo.get().getColumn();
            return column;
        } catch (MybatisPlusException e) {
        }
        return StringUtils.camelToUnderline(name);
    }

    /**
     * 为了方便操作，只是添加左右()，这里只给一个字段条件生成
     *
     * @param fn
     * @param condition 条件符号 如=、!=、>等
     * @param value
     * @param <E>
     * @param <R>
     * @return
     */
    public <E, R> PlusEntityWrapper<T> and(Fn<E, R> fn, String condition, Object value) {
        String column = this.getColumn(fn);
        super.and(column.concat(condition).concat("{0}"), value);
        return this;
    }

    public <E, R> PlusEntityWrapper<T> andNew(Fn<E, R> fn, String condition, Object value) {
        String column = this.getColumn(fn);
        super.andNew(column.concat(condition).concat("{0}"), value);
        return this;
    }

    public <E, R> PlusEntityWrapper<T> or(Fn<E, R> fn, String condition, Object value) {
        String column = this.getColumn(fn);
        super.or(column.concat(condition).concat("{0}"), value);
        return this;
    }

    public <E, R> PlusEntityWrapper<T> orNew(Fn<E, R> fn, String condition, Object value) {
        String column = this.getColumn(fn);
        super.orNew(column.concat(condition).concat("{0}"), value);
        return this;
    }

    public <E, R> PlusEntityWrapper<T> eq(Fn<E, R> fn, Object value) {
        String column = this.getColumn(fn);
        super.eq(column, value);
        return this;
    }

    public <E, R> PlusEntityWrapper<T> ne(Fn<E, R> fn, Object value) {
        String column = this.getColumn(fn);
        super.ne(column, value);
        return this;
    }

    public <E, R> PlusEntityWrapper<T> gt(Fn<E, R> fn, Object value) {
        String column = this.getColumn(fn);
        super.gt(column, value);
        return this;
    }

    public <E, R> PlusEntityWrapper<T> ge(Fn<E, R> fn, Object value) {
        String column = this.getColumn(fn);
        super.ge(column, value);
        return this;
    }

    public <E, R> PlusEntityWrapper<T> lt(Fn<E, R> fn, Object value) {
        String column = this.getColumn(fn);
        super.lt(column, value);
        return this;
    }

    public <E, R> PlusEntityWrapper<T> le(Fn<E, R> fn, Object value) {
        String column = this.getColumn(fn);
        super.le(column, value);
        return this;
    }

    public <E, R> PlusEntityWrapper<T> like(Fn<E, R> fn, String value) {
        String column = this.getColumn(fn);
        super.like(column, value);
        return this;
    }

    public <E, R> PlusEntityWrapper<T> notLike(Fn<E, R> fn, String value) {
        String column = this.getColumn(fn);
        super.notLike(column, value);
        return this;
    }

    public <E, R> PlusEntityWrapper<T> in(Fn<E, R> fn, Object... value) {
        String column = this.getColumn(fn);
        super.in(column, value);
        return this;
    }

    public <E, R> PlusEntityWrapper<T> notIn(Fn<E, R> fn, Object... value) {
        String column = this.getColumn(fn);
        super.notIn(column, value);
        return this;
    }

    public <E, R> PlusEntityWrapper<T> isNull(Fn<E, R> fn) {
        String column = this.getColumn(fn);
        super.isNull(column);
        return this;
    }

    public <E, R> PlusEntityWrapper<T> isNotNull(Fn<E, R> fn) {
        String column = this.getColumn(fn);
        super.isNotNull(column);
        return this;
    }

    public <E, R> PlusEntityWrapper<T> groupBy(Fn<E, R> fn) {
        String column = this.getColumn(fn);
        super.groupBy(column);
        return this;
    }

    public <E, R> PlusEntityWrapper<T> orderAsc(Fn<E, R> fn) {
        String column = this.getColumn(fn);
        super.orderBy(column, true);
        return this;
    }

    public <E, R> PlusEntityWrapper<T> orderDesc(Fn<E, R> fn) {
        String column = this.getColumn(fn);
        super.orderBy(column, false);
        return this;
    }

    public <E, R> PlusEntityWrapper<T> between(Fn<E, R> fn, Object value, Object val2) {
        String column = this.getColumn(fn);
        super.between(column, value, val2);
        return this;
    }

    public <E, R> PlusEntityWrapper<T> notBetween(Fn<E, R> fn, Object value, Object val2) {
        String column = this.getColumn(fn);
        super.notBetween(column, value, val2);
        return this;
    }
}
