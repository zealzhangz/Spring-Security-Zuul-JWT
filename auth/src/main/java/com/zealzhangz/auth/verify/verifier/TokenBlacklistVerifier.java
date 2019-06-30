package com.zealzhangz.auth.verify.verifier;

import org.springframework.stereotype.Component;

/**
 * @author Created by https://zhangaoo.com.<br/>
 * @version Version: 0.0.1
 * @date DateTime: 2019/06/28 19:26:00<br/>
 */
@Component
public class TokenBlacklistVerifier implements TokenVerifier {
    /**
     * Default implement
     * @param token
     * @return
     */
    @Override
    public boolean verify(String token) {
        return true;
    }
}