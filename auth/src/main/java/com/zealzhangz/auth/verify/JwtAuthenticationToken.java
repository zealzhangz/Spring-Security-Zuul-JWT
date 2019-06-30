package com.zealzhangz.auth.verify;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author Created by https://zhangaoo.com.<br/>
 * @version Version: 0.0.1
 * @date DateTime: 2019/06/28 19:17:00<br/>
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private String accessToken;
    private Object userContext;

    public JwtAuthenticationToken(String  accessToken) {
        super(null);
        this.accessToken = accessToken;
        this.setAuthenticated(false);
    }

    public JwtAuthenticationToken(Object userContext, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.eraseCredentials();
        this.userContext = userContext;
        super.setAuthenticated(true);
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }


    @Override
    public Object getCredentials() {
        return accessToken;
    }

    @Override
    public Object getPrincipal() {
        return this.userContext;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.accessToken = null;
    }
}
