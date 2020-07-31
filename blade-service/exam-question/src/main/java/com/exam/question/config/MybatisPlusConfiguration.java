package com.exam.question.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * mybatisplus配置
 */
@Configuration
@MapperScan("com.exam.**.mapper.**")
public class MybatisPlusConfiguration {
}
