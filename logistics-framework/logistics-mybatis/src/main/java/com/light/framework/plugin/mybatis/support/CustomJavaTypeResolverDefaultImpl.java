package com.light.framework.plugin.mybatis.support;

import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;

public class CustomJavaTypeResolverDefaultImpl extends JavaTypeResolverDefaultImpl {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CustomJavaTypeResolverDefaultImpl.class);


    public CustomJavaTypeResolverDefaultImpl() {
        this.typeMap.remove(Integer.valueOf(-6));
        this.typeMap.put(Integer.valueOf(-6), new JdbcTypeInformation("TINYINT", new org.mybatis.generator.api.dom.java.FullyQualifiedJavaType(Integer.class
                .getName())));
    }
}