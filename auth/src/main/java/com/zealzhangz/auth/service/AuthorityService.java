package com.zealzhangz.auth.service;

import com.zealzhangz.common.model.ApiAuthority;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author Created by ao.zhang/ao.zhang@iluvatar.ai.<br/>
 * @version Version: 0.0.1
 * @date DateTime: 2019/06/30 16:18:00<br/>
 */
@FeignClient("user-modules")
public interface AuthorityService {
    /**
     * 获取所有接口权限信息
     *
     * @return
     */
    @RequestMapping(value = "/internal/authorities", method = RequestMethod.GET)
    List<ApiAuthority> getAllAuthority();
}
