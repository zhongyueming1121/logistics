package com.light.framework.plugin.mybatis.adapter;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.internal.util.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CachePlugin extends org.mybatis.generator.plugins.CachePlugin {
  Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable)
  {
    String useCache = introspectedTable.getTableConfigurationProperty("useCache");
    if (!Boolean.valueOf(useCache)) {
      return true;
    }
    interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.CacheNamespaceRef"));
    interfaze.addAnnotation("@CacheNamespaceRef(".concat(interfaze.getType().getShortName()).concat(".class)"));
    return true;
  }

  @Override
  public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable)
  {
    String useCache = introspectedTable.getTableConfigurationProperty("useCache");
    if (!Boolean.valueOf(useCache)) {
      return true;
    }
    XmlElement element = new XmlElement("cache");
    this.context.getCommentGenerator().addComment(element);
    CacheProperty[] arr = CacheProperty.values();
    int len$ = arr.length;

    for (int i = 0; i < len$; i++) {
      CacheProperty cacheProperty = arr[i];
      addAttributeIfExists(element, introspectedTable, cacheProperty);
    }
    document.getRootElement().addElement(element);
    return true;
  }

  private void addAttributeIfExists(XmlElement element, IntrospectedTable introspectedTable, CacheProperty cacheProperty) {
    String property = introspectedTable.getTableConfigurationProperty(cacheProperty.getPropertyName());
    if (property == null) {
      property = this.properties.getProperty(cacheProperty.getPropertyName());
    }

    if (StringUtility.stringHasValue(property)) {
      Attribute attribute = new Attribute(cacheProperty.getAttributeName(), property);
      element.addAttribute(attribute);
      if (!"cache_type".equals(cacheProperty.getPropertyName())) {
        XmlElement xmlElement = new XmlElement("property");
        xmlElement.addAttribute(new Attribute("name", cacheProperty.getAttributeName()));
        xmlElement.addAttribute(new Attribute("value", property));
        element.addElement(xmlElement);
      }
    }
  }
}