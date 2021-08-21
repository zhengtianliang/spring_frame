package com.zheng.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author: ZhengTianLiang
 * @date: 2021/08/21  9:26
 * @desc: 像上面的那个MyConfiguration是需要自己手动的一个个导入bean对象，
 *  而这个是直接扫描某个包，就不用自己手动的去写一个个的bean对象了
 */

@Configuration  // 类似于一个xml
@ComponentScan(basePackages = "com.zheng.controller") // 类似于标签componentScan的标签
public class ScanConfiguration {
}
