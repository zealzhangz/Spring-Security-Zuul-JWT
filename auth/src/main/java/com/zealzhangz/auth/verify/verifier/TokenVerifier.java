package com.zealzhangz.auth.verify.verifier;

/**
 * @author Created by https://zhangaoo.com.<br/>
 * @version Version: 0.0.1
 * @date DateTime: 2019/06/28 17:31:00<br/>
 */
public interface TokenVerifier {
    boolean verify(String token);
}
