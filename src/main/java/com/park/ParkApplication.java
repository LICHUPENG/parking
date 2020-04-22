package com.park;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan("com.park.mapper")
@SpringBootApplication
public class ParkApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParkApplication.class, args);
    }

}
