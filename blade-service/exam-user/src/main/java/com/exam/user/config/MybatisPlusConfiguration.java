package com.exam.user.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * mybatisplus配置
 */
@Configuration
@MapperScan("com.exam.user.mapper.**")
public class MybatisPlusConfiguration {
}
