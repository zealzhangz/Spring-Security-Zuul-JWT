package com.zealzhangz.auth.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Created by https://zhangaoo.com.<br/>
 * @version Version: 0.0.1
 * @date DateTime: 2019/06/24 19:49:00<br/>
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt.token")
public class TokenMetadata {
    /**
     * 发行者
     */
    private String issuer;
    /**
     * Token 过期时间，单位分钟
     */
    private Integer expiredMinutes;
    /**
     * 刷新Token过期时间，单位分钟
     */
    private Integer refreshExpiredMinutes;
    /**
     * 签名key
     */
    private String signingKey;
}
