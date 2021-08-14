package com.core.utils;

import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-08-02
 */
public class EntityUtil {

    /**
     * 字段重命名为驼峰形式
     *
     * @param param
     * @return
     */
    public static String convertToCamel(String param) {
        if (org.apache.commons.lang3.StringUtils.isBlank(param)) {
            return "";
        }
        StringBuilder stb = new StringBuilder(param);
        Matcher matcher = Pattern.compile("_").matcher(param);
        int i = 0;
        while (matcher.find()) {
            int position = matcher.end() - (i++);
            stb.replace(position - 1, position + 1, stb.substring(position, position + 1).toUpperCase());
        }
        return stb.toString();
    }

    /**
     * net.sf.json.JSONObject 属性名转驼峰
     *
     * @param jsonObject
     * @return
     */
    public static JSONObject toCamel(JSONObject jsonObject) {
        Iterator iterator = jsonObject.entrySet().iterator();
        Map<String, Object> newMap = new HashMap<>();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String key = (String) entry.getKey();
            key = EntityUtil.convertToCamel(key);
            Object value = entry.getValue();
            if ("null".equals(key) || "null".equals(value)) {
                continue;
            }
            if (value instanceof List<?>) {
                List<JSONObject> list = (List<JSONObject>) value;
                if (CollectionUtils.isEmpty(list) || "null".equals(list)) {
                    continue;
                }
                value = list.stream().map(e -> toCamel(e)).collect(Collectors.toList());
            } else if (value instanceof JSONObject) {
                value = toCamel((JSONObject) value);
            } else if (value instanceof JSONNull) {
                continue;
            }
            newMap.put(key, value);
        }
        return mapToJsonObj(newMap);
    }

    /**
     * map 转 JSONObject
     *
     * @param map
     * @return
     */
    public static JSONObject mapToJsonObj(Map<String, Object> map) {
        JSONObject resultJson = new JSONObject();
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            resultJson.put(key, map.get(key));
        }
        return resultJson;
    }

}
