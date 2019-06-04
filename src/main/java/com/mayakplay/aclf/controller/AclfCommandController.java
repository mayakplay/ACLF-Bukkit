package com.mayakplay.aclf.controller;

import com.mayakplay.aclf.annotation.ChatMapping;
import com.mayakplay.aclf.annotation.CommandController;
import com.mayakplay.aclf.annotation.TailArgumentCommand;
import com.mayakplay.aclf.exception.ACLFCommandException;
import com.mayakplay.aclf.pojo.CommandResponse;
import com.mayakplay.aclf.service.interfaces.TranslationService;

@CommandController("aclf")
public class AclfCommandController {

    private final TranslationService translationService;

    public AclfCommandController(TranslationService translationService) {
        this.translationService = translationService;
    }

    @ChatMapping("reload")
    @TailArgumentCommand
    public void reloadTranslationCommandMethod(CommandResponse response) {


        throw new ACLFCommandException("");
    }

}