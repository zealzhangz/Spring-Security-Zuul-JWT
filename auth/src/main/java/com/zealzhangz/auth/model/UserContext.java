package com.zealzhangz.auth.model;

import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

/**
 * @author Created by ao.zhang/ao.zhang@iluvatar.ai.<br/>
 * @version Version: 0.0.1
 * @date DateTime: 2019/06/22 16:54:00<br/>
 */
@Data
public class UserContext {
    private final String username;
    private final String userId;
    private final List<GrantedAuthority> authorities;
    private final String currentTokenType;

    private UserContext(String userId, String username, List<GrantedAuthority> authorities,String currentTokenType) {
        this.userId = userId;
        this.username = username;
        this.authorities = authorities;
        this.currentTokenType = currentTokenType;
    }

    public static UserContext create(String userId, String username, List<GrantedAuthority> authorities,String currentTokenType) {
        if (StringUtils.isBlank(username)){
            throw new IllegalArgumentException("Username is blank: " + username);
        }
        return new UserContext(userId, username, authorities,currentTokenType);
    }
}
