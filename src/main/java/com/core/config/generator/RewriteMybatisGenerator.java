package com.core.config.generator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.internal.DefaultCommentGenerator;
import org.mybatis.generator.internal.util.StringUtility;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-07-15
 */
public class RewriteMybatisGenerator extends DefaultCommentGenerator {

    private String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    private boolean addRemarkComments = false;
    private static final String EXAMPLE_SUFFIX = "Example";
    private static final String MAPPER_SIGN = "Mapper";
    private static final String API_MODEL_PROPERTY_FULL_CLASS_NAME = "io.swagger.annotations.ApiModelProperty";
    private static final String MAPPER_INTERFACE_PROPERTY_FULL_CLASS_NAME = "org.apache.ibatis.annotations.Mapper";

    private Properties properties;
    private Properties systemPro;
    private boolean suppressDate;
    private boolean suppressAllComments;
    private String currentDateStr;

    public RewriteMybatisGenerator() {
        super();
        properties = new Properties();
        systemPro = System.getProperties();
        suppressDate = false;
        suppressAllComments = false;
        currentDateStr = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
    }

    /**
     * 类注释
     */
    @Override
    public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        topLevelClass.addJavaDocLine("");
        topLevelClass.addJavaDocLine("/**");
        String remark = introspectedTable.getFullyQualifiedTable().toString();
        if (!StringUtils.isEmpty(remark)) {
            topLevelClass.addJavaDocLine(" * " + "MyBatis-Generator: " + remark);
        }
        topLevelClass.addJavaDocLine(" * ");
        topLevelClass.addJavaDocLine(" * " + "@author " + systemPro.getProperty("user.name"));
        topLevelClass.addJavaDocLine(" * " + "@date " + date);
        topLevelClass.addJavaDocLine(" */");
    }

    /**
     * 字段注释
     */
    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        StringBuilder sb = new StringBuilder();
        field.addJavaDocLine("/**");
        sb.append(" * ");
        sb.append(introspectedColumn.getRemarks());
        field.addJavaDocLine(sb.toString().replace("\n", " "));
        field.addJavaDocLine(" */");
        String remarks = introspectedColumn.getRemarks();
        //给model的字段添加swagger注解
        field.addJavaDocLine("@ApiModelProperty(value = \"" + remarks + "\")");
    }

    /**
     * 对model类添加swagger注解
     * 对mapper类添加mapper注解
     *
     * @param compilationUnit
     */
    @Override
    public void addJavaFileComment(CompilationUnit compilationUnit) {
        super.addJavaFileComment(compilationUnit);
        if (!compilationUnit.isJavaInterface() && !compilationUnit.getType().getFullyQualifiedName().contains(EXAMPLE_SUFFIX)) {
            compilationUnit.addImportedType(new FullyQualifiedJavaType(API_MODEL_PROPERTY_FULL_CLASS_NAME));
        }
        if (compilationUnit.isJavaInterface() && compilationUnit.getType().getFullyQualifiedName().contains(MAPPER_SIGN)) {
            compilationUnit.addImportedType(new FullyQualifiedJavaType(MAPPER_INTERFACE_PROPERTY_FULL_CLASS_NAME));
        }
    }

    @Override
    public void addConfigurationProperties(Properties properties) {
        super.addConfigurationProperties(properties);
        this.addRemarkComments = StringUtility.isTrue(properties.getProperty("addRemarkComments"));
    }

    /**
     * 方法
     */
    @Override
    public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {
        String remark = introspectedTable.getFullyQualifiedTable().toString();
        if (!StringUtils.isEmpty(remark)) {
            if (method.getName().equalsIgnoreCase("insert")) {
                method.addJavaDocLine("/**");
                method.addJavaDocLine(" * " + "插入对象 - " + remark);
                method.addJavaDocLine(" */");
            } else if (method.getName().equalsIgnoreCase("insertSelective")) {
                method.addJavaDocLine("/**");
                method.addJavaDocLine(" * " + "选择性插入 - " + remark);
                method.addJavaDocLine(" */");
            } else if (method.getName().equalsIgnoreCase("selectByPrimaryKey")) {
                method.addJavaDocLine("/**");
                method.addJavaDocLine(" * " + "根据主键查询 - " + remark);
                method.addJavaDocLine(" */");
            } else if (method.getName().equalsIgnoreCase("selectAll")) {
                method.addJavaDocLine("/**");
                method.addJavaDocLine(" * " + "查询所有 - " + remark);
                method.addJavaDocLine(" */");
            } else if (method.getName().equalsIgnoreCase("updateByPrimaryKeySelective")) {
                method.addJavaDocLine("/**");
                method.addJavaDocLine(" * " + "选择性更新 - " + remark);
                method.addJavaDocLine(" */");
            } else if (method.getName().equalsIgnoreCase("updateByPrimaryKey")) {
                method.addJavaDocLine("/**");
                method.addJavaDocLine(" * " + "根据主键更新 - " + remark);
                method.addJavaDocLine(" */");
            } else if (method.getName().equalsIgnoreCase("deleteByPrimaryKey")) {
                method.addJavaDocLine("/**");
                method.addJavaDocLine(" * " + "根据主键删除 - " + remark);
                method.addJavaDocLine(" */");
            }
        }
    }

}