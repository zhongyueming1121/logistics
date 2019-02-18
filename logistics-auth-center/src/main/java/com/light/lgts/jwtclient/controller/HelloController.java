package com.light.lgts.jwtclient.controller;

import com.light.lgts.jwtclient.annotation.CurrentUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(@CurrentUser String userName){
        return "Hello," + userName;
    }

}
