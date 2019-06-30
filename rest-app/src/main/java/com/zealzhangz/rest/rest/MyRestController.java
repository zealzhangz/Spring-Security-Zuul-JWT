package com.zealzhangz.rest.rest;

import com.zealzhangz.common.model.ResponseData;
import com.zealzhangz.common.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Created by ao.zhang/ao.zhang@iluvatar.ai.<br/>
 * @version Version: 0.0.1
 * @date DateTime: 2019/06/27 09:58:00<br/>
 */
@RestController
@RequestMapping(value = "/user")
public class MyRestController {
    private static final ConcurrentHashMap<String, User> MEMORY_USER = new ConcurrentHashMap<>(2);

    public MyRestController() {
        User user = new User();
        user.setId("1");
        user.setUsername("admin");
        user.setPassword("admin123456");
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        User user1 = new User();
        user1.setId("2");
        user1.setUsername("testUser");
        user1.setPassword("testUser123456");
        user1.setAccountNonExpired(true);
        user1.setAccountNonLocked(true);
        user1.setCredentialsNonExpired(true);
        user1.setEnabled(true);
        MEMORY_USER.put(user.getUsername(),user);
        MEMORY_USER.put(user1.getUsername(),user1);
    }

    @GetMapping("/{username}")
    public ResponseData getUser(@PathVariable String username){
        User user = MEMORY_USER.get(username);
        if(null != user){
            return new ResponseData(user);
        }
       return new ResponseData(ResponseData.FAIL,"Can not find user:" + username);
    }
    @GetMapping("/all")
    public ResponseData getAllUser(){
        return new ResponseData(MEMORY_USER);
    }
}
