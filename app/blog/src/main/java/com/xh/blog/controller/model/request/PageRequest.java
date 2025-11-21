package com.xh.blog.controller.model.request;

import lombok.Data;

@Data
public class PageRequest {

    private Integer pageNo;

    private Integer pageSize;

    public Integer getOffset() {
        return (pageNo <= 0 ? 0 : pageNo - 1) * pageSize;
    }
}
