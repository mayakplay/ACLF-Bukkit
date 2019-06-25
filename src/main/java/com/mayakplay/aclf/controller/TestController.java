package com.mayakplay.aclf.controller;

import com.mayakplay.aclf.annotation.*;
import com.mayakplay.aclf.exception.ACLFException;
import com.mayakplay.aclf.service.interfaces.CommandContainerService;

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

    private final CommandContainerService service;

    public TestController(CommandContainerService service) {
        this.service = service;

        System.out.println(service.getDefaultArgumentProcessor());

        counter ++;
        System.out.println(counter + " controller created");

        System.out.println("created +");
//        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
//            System.out.println("            - " + element);
//        }

    }

    @ChannelMapping("channel")
    public void test() {

        System.out.println("some message from another command");

        throw new RuntimeException("Wrong exception");
    }

    @ChatMapping("chat")
    public void test1() {

        System.out.println("some message from command");

        throw new ACLFException("Ahahaha lol");

    }

}