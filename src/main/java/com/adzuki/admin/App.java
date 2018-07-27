package com.adzuki.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableTransactionManagement
@ComponentScan(basePackages={"com.adzuki.admin"})
@MapperScan(basePackages = {"com.adzuki.admin.mapper"})
public class App{
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
