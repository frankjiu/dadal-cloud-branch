package com.core.utils;

import com.wlhlwl.hyzc.customer.modules.douyin.model.vo.UserInfo;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-07-31
 */
public class BeanUtil {

    public static Object abandonNullProperties(Object obj) {
        JsonConfig jsonConfig = new JsonConfig();
        PropertyFilter filter = (Object object, String fieldName, Object fieldValue) -> {
            if (fieldValue instanceof List) {
                return CollectionUtils.isEmpty((List<Object>) fieldValue);
            }
            return null == fieldValue;
        };
        jsonConfig.setJsonPropertyFilter(filter);
        return JSONArray.fromObject(obj, jsonConfig);
    }

    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static void copyProperties(Object source, Object target) {
        // Object src = abandonNullProperties(source);
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    @Test
    public void call() {
        UserInfo userInfo = new UserInfo();
        userInfo.setMobile("556756");
        //Object obj = abandonNullProperties(userInfo);

        UserInfo oldUserInfo = new UserInfo();
        oldUserInfo.setCountry("中国");
        oldUserInfo.setProvince("山东");
        oldUserInfo.setCity("烟台");

        copyProperties(userInfo, oldUserInfo);

        System.out.println(oldUserInfo);
    }


}


