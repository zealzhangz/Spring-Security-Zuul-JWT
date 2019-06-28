package com.zealzhangz.auth.token;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author Created by ao.zhang/ao.zhang@iluvatar.ai.<br/>
 * @version Version: 0.0.1
 * @date DateTime: 2019/06/22 16:06:00<br/>
 */
public class JwtUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {
    /**
     * 用来传递刷新 token
     */
    private Object details;

    public JwtUsernamePasswordAuthenticationToken(Object principal, Object credentials,Object details) {
        super(principal, credentials);
        //for password refresh token
        this.details = details;
    }

    public JwtUsernamePasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
