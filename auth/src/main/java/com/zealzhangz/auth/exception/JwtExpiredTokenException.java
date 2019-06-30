package com.zealzhangz.auth.exception;

import org.springframework.security.authentication.AuthenticationServiceException;

/**
 * @author Created by https://zhangaoo.com.<br/>
 * @version Version: 0.0.1
 * @date DateTime: 2019/06/24 20:21:00<br/>
 */
public class JwtExpiredTokenException  extends AuthenticationServiceException {
    public JwtExpiredTokenException(String msg) {
        super(msg);
    }

    public JwtExpiredTokenException(String msg, Throwable t) {
        super(msg, t);
    }
}
