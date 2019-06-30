package com.zealzhangz.gateway;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @author Created by ao.zhang/ao.zhang@iluvatar.ai.<br/>
 * @version Version: 0.0.1
 * @date DateTime: 2019/06/25 15:53:00<br/>
 */

/**
 * 注意：如果依赖的包名不一样，可能会扫描不到bean
 * 需要手动指定一下
 */
@EnableFeignClients("com.zealzhangz.*")
@EnableZuulProxy
@SpringBootApplication(scanBasePackages = "com.zealzhangz")
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
