package com.zheng.controller;

import com.zheng.annotation.HAutowired;
import com.zheng.annotation.HController;
import com.zheng.service.UserService;

/**
 * @author: ZhengTianLiang
 * @date: 2021/08/21  16:04
 * @desc: 目的是为了让手写的ioc也能扫描到这个文件，生成bean对象即可。
 */

@HController
public class UserController {

    /**
     * 通过@HAutowired注解完成对UserService的注入，(从ioc容器中寻找有没有UserService的实现类，
     * 如果有一个，则将其复制给userService的引用变量)
     */
    @HAutowired
    private UserService userService;

    // 测试，看能够正常的注入进来
    public void userController(){
        userService.say();
    }
}
