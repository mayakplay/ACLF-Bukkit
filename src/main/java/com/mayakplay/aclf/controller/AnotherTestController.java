package com.mayakplay.aclf.controller;

import com.mayakplay.aclf.annotation.AsyncCommand;
import com.mayakplay.aclf.annotation.CommandController;
import com.mayakplay.aclf.annotation.CommandMapping;
import com.mayakplay.aclf.definition.response.PlayerCommandResponse;
import com.mayakplay.aclf.resource.ResourceRepository;
import com.mayakplay.aclf.resource.SomeResource;
import lombok.AllArgsConstructor;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 26.06.2019.
 */
@CommandController
@CommandMapping("test")
@AllArgsConstructor
public class AnotherTestController {

    private final ResourceRepository<SomeResource> resourceRepository;

    @AsyncCommand
    @CommandMapping("a")
    public void something(PlayerCommandResponse response1) {

        resourceRepository.sendToPlayer(new SomeResource(), response1.getSender());
    }

    @CommandMapping("o")
    public void something1() throws InterruptedException {

        System.out.println(Thread.currentThread());

    }
}