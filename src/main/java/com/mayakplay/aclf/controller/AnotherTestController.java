package com.mayakplay.aclf.controller;

import com.mayakplay.aclf.annotation.*;
import com.mayakplay.aclf.definition.response.CommandResponse;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 26.06.2019.
 */
@CommandController
@CommandMapping("test")
public class AnotherTestController {

    @AsyncCommand
    @CommandMapping("a")
    public void something(CommandResponse response) {

        System.out.println(Thread.currentThread());

    }

    @CommandMapping("o")
    public void something1() throws InterruptedException {

        System.out.println(Thread.currentThread());

    }
}