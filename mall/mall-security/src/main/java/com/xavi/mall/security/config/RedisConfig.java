package com.xavi.mall.security.config;

import com.xavi.mall.common.config.BaseRedisConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * Redis相关配置
 * Created by xavier
 */
@EnableCaching
@Configuration
public class RedisConfig extends BaseRedisConfig {

}
