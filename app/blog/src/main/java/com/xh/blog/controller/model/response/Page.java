package com.xh.blog.controller.model.response;

import com.xh.blog.controller.model.request.PageRequest;
import lombok.Data;

import java.util.List;

@Data
public class Page<T> {

    private List<T> list;

    private Integer pageNo;

    private Integer pageSize;

    public static <T> Page<T> of(PageRequest request, List<T> list) {
        Page<T> page = new Page<>();
        page.setPageNo(request.getPageNo());
        page.setPageSize(request.getPageSize());
        page.setList(list);

        return page;
    }
}
