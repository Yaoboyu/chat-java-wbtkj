package com.wbtkj.chat.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.wbtkj.chat.mapper")
public class MybatisConfig {

}
