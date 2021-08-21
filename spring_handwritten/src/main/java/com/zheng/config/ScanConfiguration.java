package com.zheng.config;

import com.zheng.annotation.HComponentScan;
import com.zheng.annotation.HConfiguration;

/**
 * @author: ZhengTianLiang
 * @date: 2021/08/21  16:03
 * @desc: 是为了模拟上面的案例项目中的  扫描包的权限定路径的配置类
 */

@HConfiguration
@HComponentScan(basePackage = "com.zheng.controller")
public class ScanConfiguration {
}
