package com.mayakplay.aclf.controller;

import com.mayakplay.aclf.annotation.ChatMapping;
import com.mayakplay.aclf.annotation.CommandController;
import com.mayakplay.aclf.annotation.CommandMapping;
import com.mayakplay.aclf.annotation.TranslatedString;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 09.06.2019.
 */
@CommandController
@CommandMapping("test")
public class TestController {

    private static int counter = 0;

    @TranslatedString private final String SOME_TEXT = "";
    @TranslatedString private final String SOME_ANOTHER_TEXT = "";

    public TestController() {
        counter ++;
        System.out.println(counter + " controller created");

        System.out.println("created +");
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            System.out.println("            - " + element);
        }

    }

    @ChatMapping("channel")
    public void test() {

    }

    @ChatMapping("chat")
    public void test1() {
        System.out.println("chat");
    }

}