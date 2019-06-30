package com.zealzhangz.auth.verify.verifier;

/**
 * @author Created by ao.zhang/ao.zhang@iluvatar.ai.<br/>
 * @version Version: 0.0.1
 * @date DateTime: 2019/06/28 17:31:00<br/>
 */
public interface TokenVerifier {
    boolean verify(String token);
}
