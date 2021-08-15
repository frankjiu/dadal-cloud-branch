package com.core.anotation.annotation_eg;

import com.core.anotation.Logged;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2020-06-03
 */
public class AnotationUses {

    @Override
    @Logged(remark = "无")
    public String toString() {
        return "Overriden toString method";
    }

    @Deprecated
    @AnotationInfo(comments = "this method is deprecated.", date = "2018/10/01")
    public static void testDeprecated() {
        System.out.println("too old, don't use it.");
    }

    @SuppressWarnings({"unchecked", "deprecation"})
    @AnotationInfo(author = "frank", comments = "Main method", date = "2019/11/11", revision = 5)
    public static void testSuppressWarnings() throws FileNotFoundException {
        List list = new ArrayList();
        list.add("abc");
        testDeprecated();
    }

}