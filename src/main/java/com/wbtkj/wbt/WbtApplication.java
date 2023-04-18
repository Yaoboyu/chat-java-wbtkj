package com.wbtkj.wbt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

//@ServletComponentScan
@SpringBootApplication
public class WbtApplication {

    public static void main(String[] args) {
        SpringApplication.run(WbtApplication.class, args);
    }

}
