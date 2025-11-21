package com.xh.blog.controller.model.response;

import lombok.Data;

@Data
public class Response<T> {

    private Boolean success;

    private T data;

    public static <T> Response<T> ofSuccess(T data) {
        Response<T> response = new Response<>();
        response.setSuccess(true);
        response.setData(data);
        return response;
    }

    public static <T> Response<T> ofFail() {
        Response<T> response = new Response<>();
        response.setSuccess(false);
        return response;
    }
}
