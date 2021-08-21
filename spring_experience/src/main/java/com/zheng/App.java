package com.zheng;

import com.zheng.config.MyConfiguration;
import com.zheng.config.ScanConfiguration;
import com.zheng.domain.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author: ZhengTianLiang
 * @date: 2021/08/17  20:52
 * @desc: 测试项目的启动类(说是启动类 ， 其实不是springboot项目 ， 只是习惯了)
 *
 *  spring的ioc的三种注入方式：
 *      1、基于xml的方式，具体看xml
 *      2、基于单个bean的方式，具体看MyConfiguration
 *      3、基于包扫描的方式，具体看ScanConfiguration
 */

public class App {

    public static void main(String[] args) {


    }

    /**
     * @author: ZhengTianLiang
     * @date: 2021/08/21  9:52
     * @desc: 是基于 扫描包的 注解的方式(方式3)   ScanConfiguration
     */
    public static void scanIoc(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ScanConfiguration.class);
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String s : beanDefinitionNames){
            System.out.println(s);
        }
    }

    /**
     * @author: ZhengTianLiang
     * @date: 2021/08/17  20:59
     * @desc: 是基于 xml 的方式实现的(方式1)
     */
    public static void xmlIoc() {
        // 是用的xml的方式来实现ioc的控制反转，
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        User user = (User) context.getBean("user");
        System.out.println(user);
    }

    /**
     * @author: ZhengTianLiang
     * @date: 2021/08/17  21:10
     * @desc: 是基于 注入单个bean 的方式实现的(方式2)，MyConfiguration
     */
    public static void annotationIoc() {
        // 通过注解方式，实现的ioc
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MyConfiguration.class);
        System.out.println(context.getBean("getOrder"));
    }

}
