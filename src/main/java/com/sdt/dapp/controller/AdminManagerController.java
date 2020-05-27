package com.sdt.dapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/adminManager")
public class AdminManagerController {



    @GetMapping("")
    public String index() {
        return "adminManager";
    }

}
