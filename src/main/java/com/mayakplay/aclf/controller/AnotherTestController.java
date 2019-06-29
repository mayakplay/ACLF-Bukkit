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

    @ChatMapping("test")
    @Permitted("test")
    public void something(CommandResponse response, String string, String test) {
        System.out.println(string + " : " + test);
    }

    @ChatMapping("o")
    @OpsOnly
    public void something1() {
        System.out.println("OPA4KI");
    }

}