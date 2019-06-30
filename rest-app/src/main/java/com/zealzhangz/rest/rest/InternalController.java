package com.zealzhangz.rest.rest;

import com.zealzhangz.common.model.ApiAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by ao.zhang/ao.zhang@iluvatar.ai.<br/>
 * @version Version: 0.0.1
 * @date DateTime: 2019/06/30 16:08:00<br/>
 */
@RestController
@RequestMapping(value = "/internal")
public class InternalController {
    @GetMapping("authorities")
    public List<ApiAuthority> getAllAuthorities(){
        List<ApiAuthority> list = new ArrayList<>(3);
        list.add(new ApiAuthority("/api/user/admin","GET","admin"));
        list.add(new ApiAuthority("/api/user/testUser","GET","user"));
        list.add(new ApiAuthority("/api/user/all","GET","admin"));
        return list;
    }
}
