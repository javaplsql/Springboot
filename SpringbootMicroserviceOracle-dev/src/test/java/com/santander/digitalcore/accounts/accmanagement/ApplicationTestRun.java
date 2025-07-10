package com.java.developing.accounts.accountmanaging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

import com.developing.app.accounts.accountmanaging.Application;


/*
 * This class can be executed to boot the microservice by using the test scope dependencies with 'mvn spring-boot:test-run'
 */
@TestConfiguration(proxyBeanMethods = false)
public class ApplicationTestRun {

    public static void main(String[] args) {
        SpringApplication.from(Application::main).with(ApplicationTestRun.class).run(args);
    }

}
