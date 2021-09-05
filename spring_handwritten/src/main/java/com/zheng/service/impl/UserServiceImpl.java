package com.zheng.service.impl;

import com.zheng.annotation.HService;
import com.zheng.service.UserService;

/**
 * @author: ZhengTianLiang
 * @date: 2021/08/21  21:55
 * @desc:
 */

@HService
public class UserServiceImpl implements UserService {

    public void say() {
        System.out.println("hello spring handwritter...123");
    }
}
