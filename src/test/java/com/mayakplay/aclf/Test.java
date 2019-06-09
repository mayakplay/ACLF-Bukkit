package com.mayakplay.aclf;

import com.mayakplay.aclf.annotation.ChannelMapping;
import com.mayakplay.aclf.annotation.CommandMapping;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.Method;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 08.06.2019.
 */
public class Test {

    public static void main(String[] args) throws Exception {
        Method prikolMethod = Test.class.getDeclaredMethod("test");

        CommandMapping commandMappingAnnotation = AnnotatedElementUtils.findMergedAnnotation(prikolMethod, CommandMapping.class);

        if (commandMappingAnnotation != null) {
            System.out.println(commandMappingAnnotation);
        }


    }

    @ChannelMapping("opa")
    private static void test() {

    }

}
