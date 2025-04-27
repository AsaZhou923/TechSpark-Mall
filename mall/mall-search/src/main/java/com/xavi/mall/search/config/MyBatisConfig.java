package com.xavi.mall.search.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis相关配置
 * Created by xavier
 */
@Configuration
@MapperScan({"com.xavi.mall.mapper","com.xavi.mall.search.dao"})
public class MyBatisConfig {
}
