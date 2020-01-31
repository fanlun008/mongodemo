package com.cargosmart.mongodemo.controller;

import com.cargosmart.mongodemo.annotation.Authorized;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/aop")
public class TokenValidByAOPController {

    @GetMapping("/auth")
    @Authorized
    public String createToken() {
        System.out.println("TokenValidByAOPController...createToken...");
        return "TokenValidByAOPController...createToken...";
    }
}
