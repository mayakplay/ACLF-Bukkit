package com.mayakplay.aclf.controller;

import com.mayakplay.aclf.annotation.ChatMapping;
import com.mayakplay.aclf.annotation.CommandController;
import com.mayakplay.aclf.annotation.CommandMapping;
import com.mayakplay.aclf.annotation.TranslatedString;
import lombok.AllArgsConstructor;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 09.06.2019.
 */
@CommandController
@CommandMapping("test")
@AllArgsConstructor
public class TestController {

    @TranslatedString
    private final String SOME_TEXT = "";

    @TranslatedString
    private final String SOME_ANOTHER_TEXT = "";

    @ChatMapping("channel")
    public void test() {



    }

    @ChatMapping("chat")
    public void test1() {



    }

}