package com.xh.common.model.web;

import lombok.Data;

@Data
public class Response<T> {

    private Boolean success;

    private String message;

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

    public static <T> Response<T> ofFail(String message) {
        Response<T> response = new Response<>();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }
}
