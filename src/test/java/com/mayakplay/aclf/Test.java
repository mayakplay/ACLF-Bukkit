package com.mayakplay.aclf;

import com.mayakplay.aclf.annotation.ACLFConfiguration;
import com.mayakplay.aclf.annotation.ChannelMapping;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 08.06.2019.
 */
public class Test {

    public static void main(String[] args) throws Exception {
//        Method prikolMethod = Test.class.getDeclaredMethod("test");
//
//        CommandMapping commandMappingAnnotation = AnnotatedElementUtils.findMergedAnnotation(prikolMethod, CommandMapping.class);
//
//        if (commandMappingAnnotation != null) {
//            System.out.println(commandMappingAnnotation);
//        }

        System.out.println(ACLFConfiguration.class.getPackage().getAnnotation(ACLFConfiguration.class));

    }

    @ChannelMapping("opa")
    private static void test() {

    }

}
