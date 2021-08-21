package com.zheng.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: ZhengTianLiang
 * @date: 2021/08/21  10:23
 * @desc: 手写spring的ioc的时候，所需要的注解，是自定义注解，此注解是参照的spring。Repository写的
 */

@Target({ElementType.TYPE})     // 表示这个注解是修饰在类上的
@Retention(RetentionPolicy.RUNTIME) // 声明周期是运行时
public @interface HRepositiry {
}
