package com.zealzhangz.auth.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

/**
 * @author Created by ao.zhang/ao.zhang@iluvatar.ai.<br/>
 * @version Version: 0.0.1
 * @date DateTime: 2019/06/22 16:28:00<br/>
 */
@Data
public class User {
    private String id;
    private String username;
    private String password;
    private List<GrantedAuthority> authorities;
    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;
    private Boolean enabled;
}
