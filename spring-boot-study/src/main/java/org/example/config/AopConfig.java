package org.example.config;

import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 启用 AOP 支持
 */
@EnableAspectJAutoProxy(proxyTargetClass = true)  // 强制使用CGLIB
public class AopConfig {

}