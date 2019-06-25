package com.mayakplay.aclf;

import com.mayakplay.aclf.annotation.Permitted;

import java.lang.reflect.Method;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 08.06.2019.
 */
@Permitted("test")
public class Test {

    public static void main(String[] args) throws Exception {
//        Method prikolMethod = Test.class.getDeclaredMethod("test");
//
//        CommandMapping commandMappingAnnotation = AnnotatedElementUtils.findMergedAnnotation(prikolMethod, CommandMapping.class);
//
//        if (commandMappingAnnotation != null) {
//            System.out.println(commandMappingAnnotation);
//        }

        Class<Test> testClass = Test.class;
        Method testMethod = testClass.getDeclaredMethod("test");

        Permitted classAnnotation = testClass.getAnnotation(Permitted.class);
        Permitted methodAnnotation = testMethod.getAnnotation(Permitted.class);

        Method value = methodAnnotation.annotationType().getDeclaredMethod("value");
        System.out.println(value);
    }

    @Permitted("test1")
    public void test() {

    }


}
