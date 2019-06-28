package com.zealzhangz.auth.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zealzhangz.common.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Created by ao.zhang/ao.zhang@iluvatar.ai.<br/>
 * @version Version: 0.0.1
 * @date DateTime: 2019/06/25 11:39:00<br/>
 */
@Component
public class LoginAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final ObjectMapper mapper;

    @Autowired
    public LoginAuthenticationFailureHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        if (e instanceof BadCredentialsException) {
            response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
            mapper.writeValue(response.getWriter(), new ResponseData(ResponseData.FAIL,"Error Password"));
        } else {
            mapper.writeValue(response.getWriter(), new ResponseData(ResponseData.FAIL, "Authentication General Error"));
        }
    }
}
