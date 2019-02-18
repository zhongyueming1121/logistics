package com.light.framework.starter.mybatis.mapper;

import java.io.Serializable;
import java.util.function.Function;

public interface Fn<T, R> extends Function<T, R>, Serializable {
}
