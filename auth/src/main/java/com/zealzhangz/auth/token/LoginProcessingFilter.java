package com.zealzhangz.auth.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zealzhangz.auth.model.LoginRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Created by https://zhangaoo.com.<br/>
 * @version Version: 0.0.1
 * @date DateTime: 2019/06/22 16:02:00<br/>
 */
public class LoginProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private final ObjectMapper objectMapper;
    private final AuthenticationFailureHandler failureHandler;
    private final AuthenticationSuccessHandler successHandler;

    public  LoginProcessingFilter(String defaultFilterProcessesUrl,
                                 ObjectMapper objectMapper,
                                 AuthenticationSuccessHandler successHandler,
                                 AuthenticationFailureHandler failureHandler) {
        super(defaultFilterProcessesUrl);
        this.objectMapper = objectMapper;
        this.failureHandler = failureHandler;
        this.successHandler = successHandler;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        LoginRequest loginRequest;
        try {
            loginRequest = objectMapper.readValue(request.getReader(), LoginRequest.class);
        } catch (Exception e){
            throw new BadCredentialsException("Username or Password or refresh_token is invalid");
        }
        //对用户名密码或刷新 token 做初步的认证
        if(!checkUserInfo(loginRequest)){
            throw new BadCredentialsException("Username or Password or refresh_token is invalid");
        }
        JwtUsernamePasswordAuthenticationToken token = new JwtUsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword(),
                loginRequest.getRefreshToken());
        return this.getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        successHandler.onAuthenticationSuccess(request,response,authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }

    private boolean checkUserInfo(LoginRequest loginRequest){
        //check refresh token first
        if(StringUtils.isNotBlank(loginRequest.getRefreshToken())){
            return true;
        } else {
            //check username or password
            if (StringUtils.isBlank(loginRequest.getUsername()) || StringUtils.isBlank(loginRequest.getPassword())) {
                return false;
            } else {
                return true;
            }
        }
    }
}
