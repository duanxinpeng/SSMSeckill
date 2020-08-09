package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SecKill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {

    @Autowired
    private SeckillDao seckillDao;
    @Test
    public void reduceNumber() {
        //JDBC Connection [com.alibaba.druid.proxy.jdbc.ConnectionProxyImpl@c446b14] will not be managed by Spring
        Date killTime = new Date();
        int updateCount = seckillDao.reduceNumber(1000,killTime);
        System.out.println(updateCount);
    }

//    Caused by: org.springframework.core.NestedIOException: Failed to parse config resource: class path resource [mybatis-config.xml]; nested exception is org.apache.ibatis.builder.BuilderException: Error parsing SQL Mapper Configuration. Cause: org.apache.ibatis.builder.BuilderException: The setting mapunderscoreCamelCase is not known.  Make sure you spelled it correctly (case sensitive).
//    at org.mybatis.spring.SqlSessionFactoryBean.buildSqlSessionFactory(SqlSessionFactoryBean.java:437)
//    at org.mybatis.spring.SqlSessionFactoryBean.afterPropertiesSet(SqlSessionFactoryBean.java:343)
//    at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.invokeInitMethods(AbstractAutowireCapableBeanFactory.java:1633)
//    at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1570)
//            ... 39 more
//    Caused by: org.apache.ibatis.builder.BuilderException: Error parsing SQL Mapper Configuration. Cause: org.apache.ibatis.builder.BuilderException: The setting mapunderscoreCamelCase is not known.  Make sure you spelled it correctly (case sensitive).
//    at org.apache.ibatis.builder.xml.XMLConfigBuilder.parseConfiguration(XMLConfigBuilder.java:121)
//    at org.apache.ibatis.builder.xml.XMLConfigBuilder.parse(XMLConfigBuilder.java:98)
//    at org.mybatis.spring.SqlSessionFactoryBean.buildSqlSessionFactory(SqlSessionFactoryBean.java:431)
//            ... 42 more
//    Caused by: org.apache.ibatis.builder.BuilderException: The setting mapunderscoreCamelCase is not known.  Make sure you spelled it correctly (case sensitive).
//    at org.apache.ibatis.builder.xml.XMLConfigBuilder.settingsAsProperties(XMLConfigBuilder.java:134)
//    at org.apache.ibatis.builder.xml.XMLConfigBuilder.parseConfiguration(XMLConfigBuilder.java:106)
//            ... 44 more
    @Test
    public void queryById() {
        long id = 1000;
        SecKill res = seckillDao.queryById(id);
        System.out.println(res.getName());
    }

    @Test
    public void queryAll() {
        List<SecKill> seckills = seckillDao.queryAll(0,100);
        for (SecKill seckill:
             seckills) {
            System.out.println(seckill);
        }
    }
}