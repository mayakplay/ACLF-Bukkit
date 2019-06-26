package com.mayakplay.aclf.controller;

import com.mayakplay.aclf.annotation.*;
import com.mayakplay.aclf.definition.response.CommandResponse;
import com.mayakplay.aclf.exception.ACLFException;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 09.06.2019.
 */
@CommandController
@CommandMapping
public class TestController {

    private static int counter = 0;

    public TestController() {
        System.out.println(++counter + "st/nd/rd (I dont give a fk) controller created");
    }

    @ChatMapping("test")
    public void something(CommandResponse response, String string, String test) {
        System.out.println(string + " : " + test);
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