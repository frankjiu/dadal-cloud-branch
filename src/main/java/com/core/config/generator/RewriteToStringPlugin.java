package com.core.config.generator;

import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

/**
 * Mybatis generator 自定义 Entity.toString 插件
 *
 * @Author 刘仁奎
 */
public class RewriteToStringPlugin extends PluginAdapter {

    public RewriteToStringPlugin() {
    }

    public boolean validate(List<String> warnings) {
        return true;
    }

    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        this.generateToString(introspectedTable, topLevelClass);
        return true;
    }

    public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        this.generateToString(introspectedTable, topLevelClass);
        return true;
    }

    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        this.generateToString(introspectedTable, topLevelClass);
        return true;
    }

    /**
     * Java 类 toString 实现方法
     */
    private void generateToString(IntrospectedTable introspectedTable, TopLevelClass topLevelClass) {
        /*Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.setName("toString");
        if (introspectedTable.isJava5Targeted()) {
            method.addAnnotation("@Override");
        }

        // 添加方法注释
        this.context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);

        // 下面两句 method.addBodyLine 使用其中之一即可

        // 使用自定义toString工具类
        // method.addBodyLine("return ToStringUtils.toSimpleString(this);");

        //使用commons-lang3的工具类
        //添加包
        topLevelClass.addImportedType("org.apache.commons.lang3.builder.ToStringBuilder");
        topLevelClass.addImportedType("org.apache.commons.lang3.builder.ToStringStyle");
        method.addBodyLine("return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);");

        //添加方法
        topLevelClass.addMethod(method);*/
    }

    /**
     * 生成mapper.xml,文件内容会被清空再写入
     */
    @Override
    public boolean sqlMapGenerated(GeneratedXmlFile sqlMap, IntrospectedTable introspectedTable) {
        sqlMap.setMergeable(false);
        return super.sqlMapGenerated(sqlMap, introspectedTable);
    }
}