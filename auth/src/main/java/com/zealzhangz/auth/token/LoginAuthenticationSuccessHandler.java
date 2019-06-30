package com.zealzhangz.auth.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zealzhangz.auth.model.UserContext;
import com.zealzhangz.common.constant.Constant;
import com.zealzhangz.common.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Created by ao.zhang/ao.zhang@iluvatar.ai.<br/>
 * @version Version: 0.0.1
 * @date DateTime: 2019/06/25 14:42:00<br/>
 */
@Component
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private JwtTokenHelper jwtTokenHelper;
    @Autowired
    private ObjectMapper mapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        UserContext userContext = (UserContext) authentication.getPrincipal();
        Map<String, Object> tokenMap = new HashMap<>(2);
        if (Constant.ACCESS_TOKEN.equals(userContext.getCurrentTokenType())) {
            String accessToken = jwtTokenHelper.createAccessToken(userContext.getUsername(), userContext.getUserId());
            String refreshToken = jwtTokenHelper.createRefreshToken(userContext.getUsername(), userContext.getUserId());
            tokenMap.put("access_token", accessToken);
            tokenMap.put("refresh_token", refreshToken);
        } else if (Constant.REFRESH_TOKEN.equals(userContext.getCurrentTokenType())) {
            String accessToken = jwtTokenHelper.createAccessToken(userContext.getUsername(), userContext.getUserId());
            tokenMap.put("access_token", accessToken);
        }
        ResponseData token = new ResponseData(tokenMap);
        mapper.writeValue(response.getWriter(), token);
        clearAuthenticationAttributes(request);
    }

    /**
     * Removes temporary authentication-related data which may have been stored
     * in the session during the authentication process..
     */
    protected void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
}
