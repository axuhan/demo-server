package com.xh.blog.controller.model.response;

import com.xh.blog.controller.model.request.PageRequest;
import lombok.Data;

import java.util.List;

@Data
public class Page<T> {

    private List<T> list;

    private Integer pageNo;

    private Integer pageSize;

    private Integer totalPage;

    private Long total;

    public static <T> Page<T> of(PageRequest request, List<T> list, long total) {
        int totalPage = total == 0 ? 1 : ((int)total - 1) / request.getPageSize() + 1;

        Page<T> page = new Page<>();
        page.setPageNo(Math.min(request.getPageNo(), totalPage));
        page.setPageSize(request.getPageSize());
        page.setList(list);
        page.setTotalPage(totalPage);
        page.setTotal(total);

        return page;
    }
}
