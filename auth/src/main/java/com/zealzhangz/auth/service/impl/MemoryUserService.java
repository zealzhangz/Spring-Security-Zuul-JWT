package com.zealzhangz.auth.service.impl;

import com.zealzhangz.auth.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Created by ao.zhang/ao.zhang@iluvatar.ai.<br/>
 * @version Version: 0.0.1
 * @date DateTime: 2019/06/22 16:31:00<br/>
 */
@Service
public class MemoryUserService implements UserDetailsService {
    private final ConcurrentHashMap<String, User> users;

    public MemoryUserService() {
        this.users = new ConcurrentHashMap<>(2);
        //admin
        User user = new User();
        user.setId("1");
        user.setUsername("admin");
        user.setPassword("admin123456");
        user.setAuthorities(Arrays.asList((GrantedAuthority) () -> "admin", () -> "user"));
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        //testUser
        User user1 = new User();
        user1.setId("2");
        user1.setUsername("testUser");
        user1.setPassword("testUser123456");
        user1.setAuthorities(Arrays.asList((GrantedAuthority) () -> "user"));
        user1.setAccountNonExpired(true);
        user1.setAccountNonLocked(true);
        user1.setCredentialsNonExpired(true);
        user1.setEnabled(true);
        users.put(user.getUsername(), user);
        users.put(user1.getUsername(), user1);
    }

    @Override
    public MyUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new MyUserDetails(this.users.get(username));
    }
}
