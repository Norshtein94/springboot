package com.dada.shen.springboot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright © 2016-2017Yoongoo.
 *
 * @Title: com.dada.shen.springboot.Sample
 * @Project: springboot
 * @author: dada.shen
 * @date: 2017-12-29 17:53
 * @Description:
 */
@RestController
@RequestMapping("/dada")
@EnableAutoConfiguration
public class Example {
    @Value("${test.msg}")
    private String msg;

    @RequestMapping("/home")
    String home(){
        return msg;
    }
    
    public static void main(String[] args){
        SpringApplication.run(Example.class, args);
    }

}
