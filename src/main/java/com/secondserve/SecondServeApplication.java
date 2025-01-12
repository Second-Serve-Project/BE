package com.secondserve;

import org.springframework.boot.SpringApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(
        basePackages = "com.secondserve.repository",
        repositoryImplementationPostfix = "CustomImpl"
)
public class SecondServeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecondServeApplication.class, args);
    }

}