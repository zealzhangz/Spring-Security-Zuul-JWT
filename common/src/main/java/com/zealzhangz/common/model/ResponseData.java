package com.zealzhangz.common.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author Created by https://zhangaoo.com.<br/>
 * @version Version: 0.0.1
 * @date DateTime: 2019/06/25 14:06:00<br/>
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseData<T> {
    public static final int SUCCESS = 200;
    public static final int FAIL = 5000;

    private int code = SUCCESS;

    private T data;

    public ResponseData() {
    }

    public ResponseData(T data) {
        this.data = data;
    }

    public ResponseData(int code, T data) {
        this.code = code;
        this.data = data;
    }
}
