package com.zheng.controller;

import org.springframework.stereotype.Controller;

/**
 * @author: ZhengTianLiang
 * @date: 2021/08/21  9:40
 * @desc: 虽然我在ScanConfiguration中扫描了controller这个包，但是我没有在这个controller中加上
 * @Controller或者@Service@Reponsitory@Conponent注解，所以这个bean对象不会被添加到ioc容器中
 */

@Controller
public class ProductController {
}
