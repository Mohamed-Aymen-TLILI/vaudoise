package com.test.vaudoise;

import org.springframework.boot.SpringApplication;

public class TestVaudoiseApplication {

    public static void main(String[] args) {
        SpringApplication.from(VaudoiseApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
