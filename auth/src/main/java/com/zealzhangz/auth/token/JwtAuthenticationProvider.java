package com.zealzhangz.auth.token;

import com.zealzhangz.auth.exception.InvalidJwtToken;
import com.zealzhangz.auth.exception.UserDisabledException;
import com.zealzhangz.auth.model.UserContext;
import com.zealzhangz.auth.service.impl.MemoryUserService;
import com.zealzhangz.auth.service.impl.MyUserDetails;
import com.zealzhangz.common.constant.Constant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Collections;

import static com.zealzhangz.common.constant.Constant.REFRESH_TOKEN;


/**
 * @author Created by https://zhangaoo.com.<br/>
 * @version Version: 0.0.1
 * @date DateTime: 2019/06/22 16:23:00<br/>
 */
@Slf4j
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private JwtTokenHelper tokenHelper;
    @Autowired
    private MemoryUserService memoryUserService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.notNull(authentication, "No authentication data provided");
        //username and password
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        String refreshToken = (String) authentication.getDetails();

        if (StringUtils.isNotBlank(refreshToken)) {
            // decode and verify refresh token is valid
            Jws<Claims> jwsClaims = tokenHelper.parseClaims(refreshToken);
            //check token type only access_token
            String tokenType = jwsClaims.getBody().get("type").toString();
            if (!REFRESH_TOKEN.equals(tokenType)) {
                log.warn("Invalid token type,must be a refresh token,token:" + refreshToken + ",type:" + tokenType);
                throw new InvalidJwtToken("Invalid token type,must be a refresh token");
            }
            UserContext userContext = UserContext.create(jwsClaims.getBody().get("userId", String.class),
                    jwsClaims.getBody().getSubject(), Collections.emptyList(),
                    Constant.REFRESH_TOKEN);
            return new JwtUsernamePasswordAuthenticationToken(userContext, null, Collections.emptyList());
        } else {
            MyUserDetails userDetails = memoryUserService.loadUserByUsername(username);
            //Check password
            if (!userDetails.getPassword().equals(password)) {
                throw new BadCredentialsException("Authentication Failed. Invalid username or password.");
            }
            if (!userDetails.isAccountNonExpired() || !userDetails.isAccountNonLocked() || !userDetails.isCredentialsNonExpired() || !userDetails.isEnabled()) {
                log.warn("User was deleted please contact administrator: " + userDetails.getUsername());
                throw new UserDisabledException("User was disabled please contact administrator");
            }
            UserContext userContext = UserContext.create(userDetails.getUserId(), userDetails.getUsername(), userDetails.getAuthorities(), Constant.ACCESS_TOKEN);
            return new JwtUsernamePasswordAuthenticationToken(userContext, null, Collections.emptyList());
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtUsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
