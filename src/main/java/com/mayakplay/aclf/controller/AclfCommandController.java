package com.mayakplay.aclf.controller;

import com.mayakplay.aclf.annotation.ChatMapping;
import com.mayakplay.aclf.annotation.CommandController;
import com.mayakplay.aclf.annotation.TailArgumentCommand;
import com.mayakplay.aclf.exception.ACLFCommandException;
import com.mayakplay.aclf.pojo.ConsoleCommandResponse;

@CommandController("test")
public class AclfCommandController {

    @ChatMapping("t")
    @TailArgumentCommand
    public void command(ConsoleCommandResponse response, String playerName, String message) {



        throw new ACLFCommandException("");
    }

}