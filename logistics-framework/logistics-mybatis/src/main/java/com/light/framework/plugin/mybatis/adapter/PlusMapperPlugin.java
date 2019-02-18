package com.light.framework.plugin.mybatis.adapter;

import lombok.AllArgsConstructor;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.config.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PlusMapperPlugin extends PluginAdapter {
    Logger logger = LoggerFactory.getLogger(getClass());
    private String modelName;
    private String conditonClassName;

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        this.modelName = retrieveModel(this.context, introspectedTable.getBaseRecordType());
        this.conditonClassName = getConditonClassName(this.modelName);
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        addTypeAnnotation(topLevelClass, lombok.NoArgsConstructor.class);
        addTypeAnnotation(topLevelClass, AllArgsConstructor.class);
        addTypeAnnotation(topLevelClass, lombok.Data.class);
        addTypeAnnotation(topLevelClass, lombok.Builder.class);

        FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType());
        topLevelClass.addImportedType("com.light.framework.starter.base.sql.MapperInterface");
        topLevelClass.addAnnotation("@MapperInterface(\"".concat(type.getFullyQualifiedName()).concat("\")"));
        return true;
    }


    private void addTypeAnnotation(TopLevelClass topLevelClass, Class annoclass) {
        topLevelClass.addImportedType(annoclass.getName());
        topLevelClass.addAnnotation("@".concat(annoclass.getSimpleName()));
    }

    public GeneratedJavaFile createConditonClass(IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType entity = new FullyQualifiedJavaType(this.modelName);
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(this.conditonClassName);
        TopLevelClass topLevelClass = new TopLevelClass(type);
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        topLevelClass.setFinal(true);

        topLevelClass.addImportedType(entity);
        topLevelClass.addImportedType("com.light.framework.starter.mybatis.mapper.PlusEntityWrapper");
        topLevelClass.addImportedType("com.light.framework.starter.mybatis.mapper.Fn");

        FullyQualifiedJavaType ew = new FullyQualifiedJavaType("com.light.framework.starter.mybatis.mapper.PlusEntityWrapper".concat("<").concat(entity.getShortName()).concat(">"));

        Field field = new Field("ew", ew);
        field.setVisibility(JavaVisibility.PRIVATE);
        topLevelClass.addField(field);


        Method constructorMethod = new Method();
        constructorMethod.setConstructor(true);
        constructorMethod.setVisibility(JavaVisibility.PUBLIC);
        constructorMethod.setName(type.getShortName());
        constructorMethod.addBodyLine("this.ew = new PlusEntityWrapper".concat("(").concat(entity.getShortName().concat(".class")).concat(");"));
        topLevelClass.addMethod(constructorMethod);

        Method builderMethod = new Method();
        builderMethod.setVisibility(JavaVisibility.PUBLIC);
        builderMethod.setStatic(true);
        builderMethod.setReturnType(type);
        builderMethod.setName("builder");
        builderMethod.addBodyLine("return new ".concat(type.getShortName()).concat("();"));
        topLevelClass.addMethod(builderMethod);

        Method buildMethod = new Method();
        buildMethod.setVisibility(JavaVisibility.PUBLIC);
        buildMethod.setReturnType(ew);
        buildMethod.setName("build");
        buildMethod.addBodyLine("return this.ew;");
        topLevelClass.addMethod(buildMethod);

        Method orMethod = new Method();
        orMethod.setVisibility(JavaVisibility.PUBLIC);
        orMethod.setReturnType(type);
        orMethod.setName("or");
        orMethod.addBodyLine("this.ew.orNew();");
        orMethod.addBodyLine("return this;");
        topLevelClass.addMethod(orMethod);

        Method andMethod = new Method();
        andMethod.setVisibility(JavaVisibility.PUBLIC);
        andMethod.setReturnType(type);
        andMethod.setName("and");
        andMethod.addBodyLine("this.ew.andNew();");
        andMethod.addBodyLine("return this;");
        topLevelClass.addMethod(andMethod);

        Method isAndOrMethod = new Method();
        isAndOrMethod.setVisibility(JavaVisibility.PRIVATE);
        isAndOrMethod.setReturnType(FullyQualifiedJavaType.getBooleanPrimitiveInstance());
        isAndOrMethod.setName("isAndOr");
        isAndOrMethod.addBodyLine("return ew.originalSql() == null || \"\".equals(ew.originalSql()) ? true : ew.originalSql().endsWith(\"AND ()\") || ew.originalSql().endsWith(\"OR ()\");");
        topLevelClass.addMethod(isAndOrMethod);

        Method clearMethod = new Method();
        clearMethod.setVisibility(JavaVisibility.PUBLIC);
        clearMethod.setName("clear");
        clearMethod.addBodyLine("this.ew = null;");
        clearMethod.addBodyLine("this.ew = new PlusEntityWrapper".concat("(").concat(entity.getShortName().concat(".class")).concat(");"));
        topLevelClass.addMethod(clearMethod);

        Method setSqlSelect = new Method();
        setSqlSelect.setVisibility(JavaVisibility.PUBLIC);
        setSqlSelect.setReturnType(type);
        setSqlSelect.setName("setSqlSelect");
        setSqlSelect.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "sqlStr"));
        setSqlSelect.addBodyLine("ew.setSqlSelect(sqlStr);");
        setSqlSelect.addBodyLine("return this;");
        topLevelClass.addMethod(setSqlSelect);

        Method orderAsc = new Method();
        orderAsc.setVisibility(JavaVisibility.PUBLIC);
        orderAsc.setReturnType(type);
        orderAsc.setName("orderAsc");
        orderAsc.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "column"));
        orderAsc.addBodyLine("ew.orderBy(true, column, true);");
        orderAsc.addBodyLine("return this;");
        topLevelClass.addMethod(orderAsc);

        Method orderDesc = new Method();
        orderDesc.setVisibility(JavaVisibility.PUBLIC);
        orderDesc.setReturnType(type);
        orderDesc.setName("orderDesc");
        orderDesc.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "column"));
        orderDesc.addBodyLine("ew.orderBy(true, column, false);");
        orderDesc.addBodyLine("return this;");
        topLevelClass.addMethod(orderDesc);

        Method groupBy = new Method();
        groupBy.setVisibility(JavaVisibility.PUBLIC);
        groupBy.setReturnType(type);
        groupBy.setName("groupBy");
        groupBy.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "column"));
        groupBy.addBodyLine("ew.groupBy(column);");
        groupBy.addBodyLine("return this;");
        topLevelClass.addMethod(groupBy);

        FullyQualifiedJavaType type1 = new FullyQualifiedJavaType("Fn");
        type1.addTypeArgument(new FullyQualifiedJavaType("E"));
        type1.addTypeArgument(new FullyQualifiedJavaType("R"));
        Parameter fn = new Parameter(type1, "fn");
        FullyQualifiedJavaType returnType1 = new FullyQualifiedJavaType("<E, R>");

        Method orderAsc1 = new Method();
        orderAsc1.setVisibility(JavaVisibility.PUBLIC);
        orderAsc1.setReturnType(returnType1);
        orderAsc1.setName(type.getShortName().concat(" orderAsc"));
        orderAsc1.addParameter(fn);
        orderAsc1.addBodyLine("ew.orderAsc(fn);");
        orderAsc1.addBodyLine("return this;");
        topLevelClass.addMethod(orderAsc1);

        Method orderDesc1 = new Method();
        orderDesc1.setVisibility(JavaVisibility.PUBLIC);
        orderDesc1.setReturnType(returnType1);
        orderDesc1.setName(type.getShortName().concat(" orderDesc"));
        orderDesc1.addParameter(fn);
        orderDesc1.addBodyLine("ew.orderDesc(fn);");
        orderDesc1.addBodyLine("return this;");
        topLevelClass.addMethod(orderDesc1);

        Method groupBy1 = new Method();
        groupBy1.setVisibility(JavaVisibility.PUBLIC);
        groupBy1.setReturnType(returnType1);
        groupBy1.setName(type.getShortName().concat(" groupBy"));
        groupBy1.addParameter(fn);
        groupBy1.addBodyLine("ew.groupBy(fn);");
        groupBy1.addBodyLine("return this;");
        topLevelClass.addMethod(groupBy1);

        Method exists = new Method();
        exists.setVisibility(JavaVisibility.PUBLIC);
        exists.setReturnType(type);
        exists.setName("exists");
        exists.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "sqlStr"));
        exists.addBodyLine("ew.exists(sqlStr);");
        exists.addBodyLine("return this;");
        topLevelClass.addMethod(exists);

        Method notExists = new Method();
        notExists.setVisibility(JavaVisibility.PUBLIC);
        notExists.setReturnType(type);
        notExists.setName("notExists");
        notExists.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "sqlStr"));
        notExists.addBodyLine("ew.notExists(sqlStr);");
        notExists.addBodyLine("return this;");
        topLevelClass.addMethod(notExists);

        Method having = new Method();
        having.setVisibility(JavaVisibility.PUBLIC);
        having.setReturnType(type);
        having.setName("having");
        having.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "sqlStr"));
        having.addParameter(new Parameter(new FullyQualifiedJavaType("Object..."), "params"));
        having.addBodyLine("ew.having(sqlStr, params);");
        having.addBodyLine("return this;");
        topLevelClass.addMethod(having);

        List<IntrospectedColumn> columns = introspectedTable.getAllColumns();
        if ((columns != null) && (!columns.isEmpty())) {
            for (int i = 0; i < columns.size(); i++) {
                IntrospectedColumn column = (IntrospectedColumn) columns.get(i);
                String columnName = column.getJavaProperty();
                String columnJdbcName = column.getActualColumnName();
                createMethod(topLevelClass, columnName, columnJdbcName, "isNull", new String[0]);
                createMethod(topLevelClass, columnName, columnJdbcName, "isNotNull", new String[0]);
                createMethod(topLevelClass, columnName, columnJdbcName, "eq", new String[]{"java.lang.Object"});
                createMethod(topLevelClass, columnName, columnJdbcName, "ne", new String[]{"java.lang.Object"});
                createMethod(topLevelClass, columnName, columnJdbcName, "gt", new String[]{"java.lang.Object"});
                createMethod(topLevelClass, columnName, columnJdbcName, "ge", new String[]{"java.lang.Object"});
                createMethod(topLevelClass, columnName, columnJdbcName, "lt", new String[]{"java.lang.Object"});
                createMethod(topLevelClass, columnName, columnJdbcName, "le", new String[]{"java.lang.Object"});
                createMethod(topLevelClass, columnName, columnJdbcName, "in", new String[]{"Object..."});
                createMethod(topLevelClass, columnName, columnJdbcName, "notIn", new String[]{"Object..."});
                createMethod(topLevelClass, columnName, columnJdbcName, "between", new String[]{"java.lang.Object", "java.lang.Object"});
                createMethod(topLevelClass, columnName, columnJdbcName, "notBetween", new String[]{"java.lang.Object", "java.lang.Object"});
                createMethod(topLevelClass, columnName, columnJdbcName, "like", new String[]{"java.lang.String"});
                createMethod(topLevelClass, columnName, columnJdbcName, "notLike", new String[]{"java.lang.String"});
            }
        }


        GeneratedJavaFile javaFile = new GeneratedJavaFile(topLevelClass, this.context.getJavaModelGeneratorConfiguration().getTargetProject(), new org.mybatis.generator.api.dom.DefaultJavaFormatter());

        return javaFile;
    }


    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        List<GeneratedJavaFile> answer = new ArrayList();
        File file = getConditionClassFile();
        if (file.exists()) {
            file.delete();
        }
        answer.add(createConditonClass(introspectedTable));
        this.logger.info(this.conditonClassName + " -> 生成");
        return answer;
    }

    private void createMethod(TopLevelClass topLevelClass, String columnName, String columnJdbcName, String methodName, String... valueTypes) {
        createAndOrMethod(true, topLevelClass, columnName, columnJdbcName, methodName, valueTypes);
        createAndOrMethod(false, topLevelClass, columnName, columnJdbcName, methodName, valueTypes);
    }

    private void createAndOrMethod(boolean isAnd, TopLevelClass topLevelClass, String columnName, String columnJdbcName, String methodName, String... valueTypes) {
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType(this.conditonClassName));
        char c = columnName.charAt(0);
        columnName = Character.toUpperCase(c) + columnName.substring(1);
        String conditionMethodName = Character.toUpperCase(methodName.charAt(0)) + methodName.substring(1);
        if (isAnd) {
            method.setName("and" + columnName + conditionMethodName);
        } else {
            method.setName("or" + columnName + conditionMethodName);
        }
        List<String> lines = new ArrayList();
        String valueStr = "";
        if (valueTypes != null) {
            for (int i = 0; i < valueTypes.length; i++) {
                String valueType = valueTypes[i];
                String valueName = "value";
                if (i != 0) {
                    valueName = valueName + i;
                }
                Parameter parameter = new Parameter(new FullyQualifiedJavaType(valueType), valueName);
                method.addParameter(parameter);
                valueStr = valueStr.concat(", ").concat(valueName);
            }
        }
        if (!isAnd) {
            lines.add("if(!isAndOr()){");
            lines.add("ew.or();");
            lines.add("}");
        }
        lines.add("ew." + methodName + "(\"" + columnJdbcName + "\"" + valueStr + ");");
        lines.add("return this;");
        method.addBodyLines(lines);
        topLevelClass.addMethod(method);
    }


    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return false;
    }


    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return false;
    }


    private String retrieveModel(Context context, String modelName) {
        String classRemovePrefix = context.getProperty("classRemovePrefix");
        if ((classRemovePrefix != null) && (!classRemovePrefix.equals(""))) {
            modelName = modelName.replace(classRemovePrefix, "");
        }

        return modelName;
    }

    private String getConditonClassName(String modelName) {
        String suffix = "Condition";
        String extSuffix = this.context.getProperty("extClassSuffix");
        if ((extSuffix != null) && (!extSuffix.equals(""))) {
            suffix = extSuffix;
        }
        return modelName.concat(suffix);
    }


    private File getConditionClassFile() {
        String targetProject = this.context.getJavaModelGeneratorConfiguration().getTargetProject();

        String fileName = targetProject + "/" + this.conditonClassName.replace('.', '/') + ".java";
        return new File(fileName);
    }
}