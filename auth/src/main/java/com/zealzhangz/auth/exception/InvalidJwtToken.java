package com.zealzhangz.auth.exception;

import org.springframework.security.authentication.AuthenticationServiceException;

/**
 * @author Created by ao.zhang/ao.zhang@iluvatar.ai.<br/>
 * @version Version: 0.0.1
 * @date DateTime: 2019/06/22 16:00:00<br/>
 */
public class InvalidJwtToken extends AuthenticationServiceException {
    public InvalidJwtToken(String msg) {
        super(msg);
    }
}
