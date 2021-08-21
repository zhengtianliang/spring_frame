package com.zheng.controller;

import com.zheng.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * @author: ZhengTianLiang
 * @date: 2021/08/21  9:27
 * @desc:
 */

@Controller // 或者是 @Service@Component@Repository 任意一个，并且在@ComponentScan的包下 就会被扫描到
public class OrderController {

    @Autowired
    private UserService userService;
}
