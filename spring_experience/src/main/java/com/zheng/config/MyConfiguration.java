package com.zheng.config;

import com.zheng.domain.Order;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: ZhengTianLiang
 * @date: 2021/08/17  21:00
 * @desc: 一个配置类
 *  虽然这个类上加了 @Configuration 注解，但是在ScanConfiguration这个类中，只扫描了com.zheng.controller包，
 *  并没有扫描到 这个 com.zheng.config包，所以这个MyConfiguration不会被扫描到，
 *  那为何会扫描到ScanConfiguration这个文件呢，因为(方式3)你传的就是这个配置文件，所以这个文件一定会被扫描到
 */

@Configuration  // 相当于 .xml文件
public class MyConfiguration {

    @Bean       // 相当于xml文件中的 bean 标签(bean的名称，就是方法的名称，getOrder)。向ioc容器中注入一个bean
    public Order getOrder(){
        return new Order();
    }

}
