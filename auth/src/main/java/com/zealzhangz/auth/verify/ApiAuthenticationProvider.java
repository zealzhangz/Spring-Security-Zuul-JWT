package com.zealzhangz.auth.verify;

import com.zealzhangz.auth.exception.InvalidJwtToken;
import com.zealzhangz.auth.service.impl.MemoryUserService;
import com.zealzhangz.auth.token.JwtTokenHelper;
import com.zealzhangz.auth.verify.verifier.TokenBlacklistVerifier;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Created by ao.zhang/ao.zhang@iluvatar.ai.<br/>
 * @version Version: 0.0.1
 * @date DateTime: 2019/06/28 19:24:00<br/>
 */
@Slf4j
@Component
@SuppressWarnings("unchecked")
public class ApiAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private TokenBlacklistVerifier tokenBlackListVerifier;
    @Autowired
    private JwtTokenHelper jwtTokenHelper;
    @Autowired
    private MemoryUserService memoryUserService;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String accessToken =  (String)authentication.getCredentials();
        if(!tokenBlackListVerifier.verify(accessToken)){
            log.warn("Invalid Access Token,token had logout,token:" + accessToken);
            throw new InvalidJwtToken("Invalid Access Token");
        }
        Jws<Claims> jwsClaims;

        try {
            jwsClaims = jwtTokenHelper.parseClaims(accessToken);
        } catch (Exception e) {
            log.error("",e);
            throw new InvalidJwtToken("Invalid Access Token");
        }
        String subject = jwsClaims.getBody().getSubject();
        List<GrantedAuthority> authorities = memoryUserService.loadUserByUsername(subject).getAuthorities();
        return new JwtAuthenticationToken(jwsClaims, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
