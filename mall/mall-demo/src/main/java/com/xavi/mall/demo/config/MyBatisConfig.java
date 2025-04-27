package com.xavi.mall.demo.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis相关配置
 * Created by xavier
 */
@Configuration
@MapperScan("com.xavi.mall.mapper")
public class MyBatisConfig {
}
