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

    @CommandDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor" +
            " incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation" +
            " ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit" +
            " in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat" +
            " non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
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