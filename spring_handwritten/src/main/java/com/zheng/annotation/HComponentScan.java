package com.zheng.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: ZhengTianLiang
 * @date: 2021/08/21  10:28
 * @desc: 是扫描包的注解，参照 spring的ComponentScan
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface HComponentScan {

    /**
     * 表示要扫描的包的权限定路径
     */
    String basePackage();
}
