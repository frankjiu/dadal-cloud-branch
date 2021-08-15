package com.core.utils;

import com.alibaba.fastjson.JSONObject;
import com.core.result.RespCode;
import net.minidev.json.JSONArray;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @Description: 获取系统响应码工具
 * @Author: QiuQiang
 * @Date: 2021-06-02
 */
public class EnumUtil {

    private static String ENUM_CLASSPATH = "java.lang.Enum";

    public static List<Map<String, Object>> enumToListMap(Class<?> enumClass) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        if (!ENUM_CLASSPATH.equals(enumClass.getSuperclass().getCanonicalName())) {
            return resultList;
        }
        // 获取所有public方法
        Method[] methods = enumClass.getMethods();
        List<Field> fieldList = new ArrayList<>();
        //1.通过get方法提取字段，避免get作为自定义方法的开头，建议使用 ‘find’或其余命名
        Arrays.stream(methods)
                .map(Method::getName)
                .filter(
                        methodName -> methodName.startsWith("get") && !"getDeclaringClass".equals(methodName) && !"getClass".equals(methodName)
                ).forEachOrdered(methodName -> {
            try {
                Field field = enumClass.getDeclaredField(StringUtils.uncapitalize(methodName.substring(3)));
                if (null != field) {
                    fieldList.add(field);
                }
            } catch (NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
            }
        });

        //2.将字段作为key，逐一把枚举值作为value 存入list
        if (CollectionUtils.isEmpty(fieldList)) {
            return resultList;
        }

        Enum<?>[] enums = (Enum[]) enumClass.getEnumConstants();
        for (Enum<?> anEnum : enums) {
            Map<String, Object> map = new HashMap<>(fieldList.size());
            for (Field field : fieldList) {
                field.setAccessible(true);
                try {
                    // 向map集合添加字段名称 和 字段值
                    map.put(field.getName(), field.get(anEnum));
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            // 将Map添加到集合中
            resultList.add(map);
        }
        return resultList;
    }

    public static JSONArray listEnum() {
        // 枚举类
        List<Map<String, Object>> list = enumToListMap(RespCode.class);
        JSONArray json = new JSONArray();
        for (Map<String, Object> map : list) {
            JSONObject js = new JSONObject();
            //js.put("code", map.get("code"));
            //js.put("description", map.get("description"));
            js.put(map.get("code").toString(), map.get("description"));
            json.add(js);
        }
        //System.out.println(json);
        return json;
    }

}

