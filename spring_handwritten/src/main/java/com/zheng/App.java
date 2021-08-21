package com.zheng;

import com.zheng.config.ScanConfiguration;
import com.zheng.controller.UserController;
import com.zheng.ioc.HAnnotationConfigApplicationContext;

import java.util.List;
import java.util.Set;

/**
 * @author: ZhengTianLiang
 * @date: 2021/08/21  16:10
 * @desc: 手写spring的ioc的启动类
 */
public class App {

    public static void main(String[] args) {
        // 拿到注解方式注入的  spring的上下文
        HAnnotationConfigApplicationContext context = new HAnnotationConfigApplicationContext(ScanConfiguration.class);

        List<Class<?>> beanDefinitionNames = context.getBeanDefinitionNames();
        System.out.println(beanDefinitionNames);

        Object bean = context.getBean(UserController.class);
        System.out.println(bean);
    }
}
