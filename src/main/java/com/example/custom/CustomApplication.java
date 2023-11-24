package com.example.custom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CustomApplication {//  extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(CustomApplication.class, args);
    }

//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder
//                                                         application) {
//        return application.sources(CustomApplication.class);
//    }

}
