package com.xavi.mall.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MyBatis相关配置
 * Created by xavier
 */
@Configuration
@EnableTransactionManagement
@MapperScan({"com.xavi.mall.mapper","com.xavi.mall.dao"})
public class MyBatisConfig {
}
