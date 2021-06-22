package com.example.myxmallssoservice;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class MyxmallSsoServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyxmallSsoServiceApplication.class, args);
    }

}
