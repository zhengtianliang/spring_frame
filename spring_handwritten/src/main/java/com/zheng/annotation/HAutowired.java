package com.zheng.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: ZhengTianLiang
 * @date: 2021/08/21  10:26
 * @desc: 是用来实现 di 功能的，此注解是参照的spring。Component写的
 *  所谓的 di 功能就是说，通过@Autowired/@Resource 来控制bean对象(比如说Controller中引入service)
 */

@Target({ElementType.TYPE})     // 表示这个注解是修饰在类上的
@Retention(RetentionPolicy.RUNTIME) // 声明周期是运行时
public @interface HAutowired {
}
