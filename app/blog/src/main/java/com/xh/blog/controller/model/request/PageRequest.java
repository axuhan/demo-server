package com.xh.blog.controller.model.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
public class PageRequest {

    @NotNull(message = "参数异常")
    @Range(min = 1, max = 100000, message = "参数异常")
    private Integer pageNo;

    @NotNull(message = "参数异常")
    @Range(min = 1, max = 1000, message = "参数异常")
    private Integer pageSize;

    public Integer getOffset() {
        return (pageNo <= 0 ? 0 : pageNo - 1) * pageSize;
    }
}
