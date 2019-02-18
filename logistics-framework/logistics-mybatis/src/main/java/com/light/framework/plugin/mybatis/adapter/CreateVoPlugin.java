package com.light.framework.plugin.mybatis.adapter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.DefaultJavaFormatter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.internal.util.JavaBeansUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CreateVoPlugin extends PluginAdapter {
    private static final String suffix = "Vo";
    private static String modelExtName;
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private Set<FullyQualifiedJavaType> importTypes;
    private List<Field> files;
    private String mapperName;
    private FullyQualifiedJavaType modelType;

    public CreateVoPlugin() {
    }

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        String voObjectName = introspectedTable.getTableConfigurationProperty("voObjectName");
        if (voObjectName != null) {
            modelExtName = this.context.getJavaModelGeneratorConfiguration().getTargetPackage() + ".vo." + voObjectName;
            this.mapperName = this.getMapperName(this.context, introspectedTable.getMyBatis3JavaMapperType());
            this.logger.info("初始化：\n modelExtName = " + modelExtName + "\n mapperName = " + this.mapperName);
        }
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        String voObjectName = introspectedTable.getTableConfigurationProperty("voObjectName");
        if (voObjectName == null) {
            return true;
        } else {
            this.importTypes = topLevelClass.getImportedTypes();
            this.files = topLevelClass.getFields();
            this.modelType = topLevelClass.getType();
            if (this.files != null) {
                List<String> filesName = (List)this.files.stream().map(Field::getName).collect(Collectors.toList());
                Method buildVoMethod = new Method();
                buildVoMethod.setVisibility(JavaVisibility.PUBLIC);
                buildVoMethod.setReturnType(new FullyQualifiedJavaType(modelExtName));
                buildVoMethod.setName("buildVo");
                StringBuffer buffer = (new StringBuffer("return new ")).append(buildVoMethod.getReturnType().getShortName()).append("(").append(StringUtils.join(filesName, ", ")).append(");");
                buildVoMethod.addBodyLine(buffer.toString());
                topLevelClass.addMethod(buildVoMethod);
                topLevelClass.addImportedType(buildVoMethod.getReturnType());
                Parameter param = new Parameter(new FullyQualifiedJavaType(modelExtName), "vo");
                List<String> methodNames = (List)this.files.stream().map((e) -> {
                    return param.getName().concat(".").concat(JavaBeansUtil.getGetterMethodName(e.getName(), e.getType())).concat("()");
                }).collect(Collectors.toList());
                Method ofMethod = new Method();
                ofMethod.setVisibility(JavaVisibility.PUBLIC);
                ofMethod.setStatic(true);
                ofMethod.setReturnType(this.modelType);
                ofMethod.setName("of");
                ofMethod.addParameter(param);
                StringBuffer buffer1 = (new StringBuffer("return new ")).append(this.modelType.getShortName()).append("(").append(StringUtils.join(methodNames, ", ")).append(");");
                ofMethod.addBodyLine(buffer1.toString());
                topLevelClass.addMethod(ofMethod);
            }

            return true;
        }
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        String voObjectName = introspectedTable.getTableConfigurationProperty("voObjectName");
        if (voObjectName == null) {
            return null;
        } else {
            List<GeneratedJavaFile> answer = new ArrayList();
            File file = this.getModelExtFile();
            if (file.exists()) {
                file.delete();
            }

            answer.add(this.createExtModel(introspectedTable));
            this.logger.info(modelExtName + " -> 生成");
            return answer;
        }
    }

    public GeneratedJavaFile createExtModel(IntrospectedTable introspectedTable) {
        String mapperNamePackageName = (new FullyQualifiedJavaType(this.mapperName)).getPackageName();
        String rootClass = this.context.getJavaModelGeneratorConfiguration().getProperty("rootClass");
        if (rootClass != null) {
            rootClass = (new FullyQualifiedJavaType(rootClass)).getPackageName();
        }

        FullyQualifiedJavaType type = new FullyQualifiedJavaType(modelExtName);
        TopLevelClass topLevelClass = new TopLevelClass(type);
        Iterator var6 = this.importTypes.iterator();

        while(true) {
            FullyQualifiedJavaType importType;
            do {
                do {
                    if (!var6.hasNext()) {
                        topLevelClass.addSuperInterface(new FullyQualifiedJavaType("java.io.Serializable"));
                        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
                        this.addTypeAnno(topLevelClass, NoArgsConstructor.class);
                        this.addTypeAnno(topLevelClass, AllArgsConstructor.class);
                        this.addTypeAnno(topLevelClass, Data.class);
                        this.addTypeAnno(topLevelClass, Builder.class);
                        Field nfile;
                        if (this.files != null) {
                            for(var6 = this.files.iterator(); var6.hasNext(); topLevelClass.addField(nfile)) {
                                Field file = (Field)var6.next();
                                nfile = new Field(file.getName(), file.getType());
                                nfile.setVisibility(file.getVisibility());
                                List<String> docs = file.getJavaDocLines();
                                if (docs != null) {
                                    Iterator var10 = docs.iterator();

                                    while(var10.hasNext()) {
                                        String doc = (String)var10.next();
                                        nfile.addJavaDocLine(doc);
                                    }
                                }
                            }
                        }

                        GeneratedJavaFile javaFile = new GeneratedJavaFile(topLevelClass, this.context.getJavaModelGeneratorConfiguration().getTargetProject(), new DefaultJavaFormatter());
                        return javaFile;
                    }

                    importType = (FullyQualifiedJavaType)var6.next();
                } while(importType.getPackageName().startsWith("javax.persistence"));
            } while(rootClass != null && importType.getPackageName().equals(rootClass));

            if (!importType.getPackageName().equals(mapperNamePackageName) && !importType.getShortName().equals("MapperInterface") && !importType.getShortName().equals("UUIdGenId") && !importType.getShortName().equals("KeySql")) {
                topLevelClass.addImportedType(importType);
            }
        }
    }

    public File getModelExtFile() {
        String targetProject = this.context.getJavaModelGeneratorConfiguration().getTargetProject() + "/vo";
        String fileName = targetProject + "/" + modelExtName.replace('.', '/') + ".java";
        return new File(fileName);
    }

    public String getMapperName(Context context, String mapperFileName) {
        String classRemovePrefix = context.getProperty("classRemovePrefix");
        if (classRemovePrefix != null && !classRemovePrefix.equals("")) {
            mapperFileName = mapperFileName.replace(classRemovePrefix, "");
        }

        return mapperFileName;
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return false;
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return false;
    }

    private void addTypeAnno(TopLevelClass topLevelClass, Class annoclass) {
        topLevelClass.addImportedType(annoclass.getName());
        topLevelClass.addAnnotation("@".concat(annoclass.getSimpleName()));
    }
}
