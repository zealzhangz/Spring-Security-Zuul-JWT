package com.zealzhangz.auth.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Created by ao.zhang/ao.zhang@iluvatar.ai.<br/>
 * @version Version: 0.0.1
 * @date DateTime: 2019/06/22 16:02:00<br/>
 */
public class LoginRequest {
    private String username;
    private String password;
    private String refreshToken;

    @JsonCreator
    public LoginRequest(@JsonProperty("username") String username,
                        @JsonProperty("password") String password,
                        @JsonProperty("refresh_token") String refreshToken) {
        this.username = username;
        this.password = password;
        this.refreshToken = refreshToken;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
