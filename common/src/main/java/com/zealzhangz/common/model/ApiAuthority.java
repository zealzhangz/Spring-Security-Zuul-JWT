package com.zealzhangz.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Created by ao.zhang/ao.zhang@iluvatar.ai.<br/>
 * @version Version: 0.0.1
 * @date DateTime: 2019/06/30 15:59:00<br/>
 */
@AllArgsConstructor
@Data
public class ApiAuthority {
    /**
     * API URL
     */
    private String apiUrl;
    /**
     * API Method(GET, HEAD, POST, PUT, DELETE)
     */
    private String method;
    /**
     * Access API authority
     */
    private String authority;
}
