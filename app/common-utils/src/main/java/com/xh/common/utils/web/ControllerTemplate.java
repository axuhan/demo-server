package com.xh.common.utils.web;

import com.xh.common.model.exception.WebBizException;
import com.xh.common.model.web.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class ControllerTemplate {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerTemplate.class);

    public static <T> Response<T> process(Supplier<T> supplier) {
        return process(null, supplier);
    }

    public static <T> Response<T> process(Supplier<String> validator, Supplier<T> supplier) {
        try {
            if(validator != null){
                String violationMessage = validator.get();
                if(violationMessage != null) {
                    return Response.ofFail(violationMessage);
                }
            }
            return Response.ofSuccess(supplier.get());
        } catch (WebBizException e) {
            LOGGER.warn(e.getMessage(), e);
            return Response.ofFail(e.getAlertMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return Response.ofFail("系统繁忙");
        }
    }
}
