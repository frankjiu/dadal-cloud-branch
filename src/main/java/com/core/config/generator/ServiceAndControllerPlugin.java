package com.core.config.generator;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;

import java.util.ArrayList;
import java.util.List;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

public class ServiceAndControllerPlugin extends PluginAdapter {

    private String targetProject;
    private String servicePackage;
    private String serviceImplPackage;
    private String controllerPackage;
    private String servicePreffix;
    private String serviceSuffix;
    private String recordType;
    private String modelName;
    private String serviceName;
    private String serviceImplName;
    private String controllerName;

    @Override
    public boolean validate(List<String> warnings) {
        targetProject = properties.getProperty("targetProject");
        servicePackage = properties.getProperty("servicePackage");
        serviceImplPackage = properties.getProperty("serviceImplPackage");
        servicePreffix = properties.getProperty("servicePreffix");
        servicePreffix = stringHasValue(servicePreffix) ? servicePreffix : "";
        serviceSuffix = properties.getProperty("serviceSuffix");
        serviceSuffix = stringHasValue(serviceSuffix) ? serviceSuffix : "";
        controllerPackage = properties.getProperty("controllerPackage");
        return true;
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        recordType = introspectedTable.getBaseRecordType();
        modelName = recordType.substring(recordType.lastIndexOf(".") + 1);
        new FullyQualifiedJavaType(recordType);
        serviceName = servicePackage + "." + servicePreffix + modelName + serviceSuffix;
        serviceImplName = serviceImplPackage + "." + modelName + serviceSuffix + "Impl";
        recordType.substring(0, recordType.lastIndexOf("."));
        controllerName = controllerPackage.concat(".").concat(modelName).concat("Controller");
        List<GeneratedJavaFile> answer = new ArrayList<>();
        GeneratedJavaFile gjf = generateServiceInterface(introspectedTable);
        GeneratedJavaFile gjf2 = generateServiceImpl(introspectedTable);
        GeneratedJavaFile gjf3 = generateController(introspectedTable);
        answer.add(gjf);
        answer.add(gjf2);
        answer.add(gjf3);
        return answer;
    }

    // 生成service接口
    private GeneratedJavaFile generateServiceInterface(IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType service = new FullyQualifiedJavaType(serviceName);
        Interface serviceInterface = new Interface(service);
        serviceInterface.setVisibility(JavaVisibility.PUBLIC);
        serviceInterface.addImportedType(new FullyQualifiedJavaType(recordType));
        GeneratedJavaFile generatedJavaFile = new GeneratedJavaFile(serviceInterface, targetProject, context.getJavaFormatter());
        //描述 方法名
        Method method = new Method("selectAll");
        //返回类型
        method.setReturnType(FullyQualifiedJavaType.getObjectInstance());
        //方法体，逻辑代码
        method.setVisibility(null);
        method.addJavaDocLine("");
        serviceInterface.addMethod(method);
        return generatedJavaFile;
    }

    // 生成serviceImpl实现类
    private GeneratedJavaFile generateServiceImpl(IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType service = new FullyQualifiedJavaType(serviceName);
        FullyQualifiedJavaType serviceImpl = new FullyQualifiedJavaType(serviceImplName);
        TopLevelClass clazz = new TopLevelClass(serviceImpl);
        //描述类的作用域修饰符
        clazz.setVisibility(JavaVisibility.PUBLIC);
        //描述类 引入的类
        clazz.addImportedType(service);
        //描述类 的实现接口类
        clazz.addSuperInterface(service);
        clazz.addImportedType(serviceImpl);
        clazz.addImportedType(recordType);
        clazz.addImportedType(new FullyQualifiedJavaType("org.springframework.stereotype.Service"));
        clazz.addAnnotation("@Service");
        String daoFieldType = introspectedTable.getMyBatis3JavaMapperType();
        String daoFieldName = firstCharToLowCase(daoFieldType.substring(daoFieldType.lastIndexOf(".") + 1));
        //描述类的成员属性
        Field daoField = new Field(daoFieldName, new FullyQualifiedJavaType(daoFieldType));
        clazz.addImportedType(new FullyQualifiedJavaType(daoFieldType));
        clazz.addImportedType(new FullyQualifiedJavaType("org.springframework.beans.factory.annotation.Autowired"));
        daoField.addJavaDocLine("");
        //描述成员属性 的注解
        daoField.addAnnotation("@Autowired");
        //描述成员属性修饰符
        daoField.setVisibility(JavaVisibility.PRIVATE);
        clazz.addField(daoField);
        //描述 方法名
        Method method = new Method("selectAll");
        //方法注解
        method.addAnnotation("@Override");
        FullyQualifiedJavaType methodReturnType = new FullyQualifiedJavaType("Object");
        //返回值
        method.setReturnType(methodReturnType);
        //方法体，逻辑代码
        method.addBodyLine("return " + daoFieldName + ".selectAll()" + ";");
        //修饰符
        method.setVisibility(JavaVisibility.PUBLIC);
        clazz.addMethod(method);
        GeneratedJavaFile generatedJavaFile = new GeneratedJavaFile(clazz, targetProject, context.getJavaFormatter());
        return generatedJavaFile;
    }

    // 生成controller类
    private GeneratedJavaFile generateController(IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType controller = new FullyQualifiedJavaType(controllerName);
        TopLevelClass clazz = new TopLevelClass(controller);
        //描述类的作用域修饰符
        clazz.setVisibility(JavaVisibility.PUBLIC);
        //添加@Controller注解，并引入相应的类
        clazz.addImportedType(new FullyQualifiedJavaType("org.springframework.web.bind.annotation.RestController"));
        clazz.addAnnotation("@RestController");
        //添加@RequestMapping注解，并引入相应的类
        clazz.addImportedType(new FullyQualifiedJavaType("org.springframework.web.bind.annotation.RequestMapping"));
        clazz.addAnnotation("@RequestMapping(\"/" + firstCharToLowCase(modelName) + "\")");
        //添加@Validated注解，并引入相应的类
        clazz.addImportedType(new FullyQualifiedJavaType("org.springframework.validation.annotation.Validated"));
        clazz.addAnnotation("@Validated");
        //添加@Api注解，并引入相应的类
        clazz.addImportedType(new FullyQualifiedJavaType("io.swagger.annotations.Api"));
        String controllerSimpleName = controllerName.substring(controllerName.lastIndexOf(".") + 1);
        clazz.addAnnotation("@Api(tags = \"" + controllerSimpleName + "\")");
        //引入controller的父类和model，并添加泛型
        clazz.addImportedType(controller);
        clazz.addImportedType(recordType);
        //引入Service
        FullyQualifiedJavaType service = new FullyQualifiedJavaType(serviceName);
        clazz.addImportedType(service);
        //添加Service成员变量
        String serviceFieldName = firstCharToLowCase(serviceName.substring(serviceName.lastIndexOf(".") + 1));
        Field daoField = new Field(serviceFieldName, new FullyQualifiedJavaType(serviceName));
        clazz.addImportedType(new FullyQualifiedJavaType(serviceName));
        clazz.addImportedType(new FullyQualifiedJavaType("org.springframework.beans.factory.annotation.Autowired"));
        daoField.addJavaDocLine("");
        //描述成员属性 的注解
        daoField.addAnnotation("@Autowired");
        //描述成员属性修饰符
        daoField.setVisibility(JavaVisibility.PRIVATE);
        clazz.addField(daoField);
        //描述 方法名
        Method method = new Method("selectAll");
        //方法注解
        //添加@AuthCheck注解，并引入相应的类
        clazz.addImportedType(new FullyQualifiedJavaType("com.core.anotation.AuthCheck"));
        method.addAnnotation("@AuthCheck");
        //添加@PostMapping注解，并引入相应的类
        clazz.addImportedType(new FullyQualifiedJavaType("org.springframework.web.bind.annotation.PostMapping"));
        // method.addAnnotation("@PostMapping(\"/find" + firstCharToLowCase(modelName) + "\")");
        method.addAnnotation("@PostMapping(\"/selectAll\")");
        //添加@Logged注解，并引入相应的类
        clazz.addImportedType(new FullyQualifiedJavaType("com.core.anotation.Logged"));
        method.addAnnotation("@Logged(remark = \"查询" + firstCharToLowCase(modelName) + "列表\")");
        //添加@ApiOperation注解，并引入相应的类
        clazz.addImportedType(new FullyQualifiedJavaType("io.swagger.annotations.ApiOperation"));
        method.addAnnotation("@ApiOperation(value = \"查询" + firstCharToLowCase(modelName) + "列表\")");
        //返回类型
        method.setReturnType(FullyQualifiedJavaType.getObjectInstance());
        //方法体，逻辑代码
        method.addBodyLine("return " + serviceFieldName + ".selectAll()" + ";");
        //修饰符
        method.setVisibility(JavaVisibility.PUBLIC);
        clazz.addMethod(method);
        GeneratedJavaFile generatedJavaFile = new GeneratedJavaFile(clazz, targetProject, context.getJavaFormatter());
        return generatedJavaFile;
    }

    private String firstCharToLowCase(String str) {
        char[] chars = new char[1];
        chars[0] = str.charAt(0);
        String temp = new String(chars);
        if (chars[0] >= 'A' && chars[0] <= 'Z') {
            return str.replaceFirst(temp, temp.toLowerCase());
        }
        return str;
    }

}