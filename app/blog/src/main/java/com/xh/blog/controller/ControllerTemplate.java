package com.xh.blog.controller;

import com.xh.blog.controller.model.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class ControllerTemplate {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerTemplate.class);

    public static <T> Response<T> process(Supplier<T> supplier) {
        try {
            return Response.ofSuccess(supplier.get());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return Response.ofFail();
        }
    }
}
