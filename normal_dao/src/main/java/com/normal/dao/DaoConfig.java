package com.normal.dao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author: fei.he
 */
@Configuration
@MapperScan(basePackages = {"com.normal.dao"})
public class DaoConfig {


}
