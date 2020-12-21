package com.core.anotation.annotation_eg;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2020-06-03
 */
public class AnnotationParsingTest {

    public static void main(String[] args) {
        try {
            for (Method method : AnnotationParsingTest.class
                    .getClassLoader()
                    .loadClass(("com.core.anotation.DemoExample"))
                    .getMethods()) {
                // checks if MethodInfo annotation is present for the method
                if (method.isAnnotationPresent(AnotationInfo.class)) {
                    try {
                        // iterates all the annotations available in the method
                        for (Annotation annotation : method.getDeclaredAnnotations()) {
                            System.out.println("Annotation in Method '" + method + "' : " + annotation);
                        }
                        AnotationInfo anotationInfo = method.getAnnotation(AnotationInfo.class);
                        if (anotationInfo.revision() == 1) {
                            System.out.println("Method: " + method);
                        }

                    } catch (Throwable ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (SecurityException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}