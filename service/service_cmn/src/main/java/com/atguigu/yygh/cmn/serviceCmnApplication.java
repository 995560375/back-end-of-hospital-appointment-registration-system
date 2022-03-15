package com.atguigu.yygh.cmn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.atguigu")
public class serviceCmnApplication {
    public static void main(String[] args) {
        SpringApplication.run(serviceCmnApplication.class, args);
    }
}
