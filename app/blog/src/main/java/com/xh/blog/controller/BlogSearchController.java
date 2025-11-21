package com.xh.blog.controller;


import com.xh.blog.controller.model.request.BlogSearchRequest;
import com.xh.blog.controller.model.response.BlogVO;
import com.xh.blog.controller.model.response.Page;
import com.xh.blog.controller.model.response.Response;
import com.xh.dal.postgres.mapper.BlogMapper;
import com.xh.dal.postgres.model.Blog;
import com.xh.dal.postgres.model.BlogExample;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping(path = "blog/search")
public class BlogSearchController {

    @Resource
    private BlogMapper blogMapper;

    @RequestMapping(path = "list", method = RequestMethod.POST)
    @ResponseBody
    public Response<Page<BlogVO>> newBlog(@RequestBody BlogSearchRequest request) {
        BlogExample example = new BlogExample();
        example.setOrderByClause("gmt_create desc");
        example.setOffset(request.getOffset());
        example.setLimit(request.getPageSize());
        return ControllerTemplate.process(() -> {
            List<Blog> blogs = blogMapper.selectByExampleWithoutBlobs(example);
            return Page.of(request, BlogVOMapper.INSTANCE.map(blogs));
        });
    }

    @Mapper
    interface BlogVOMapper {
        BlogVOMapper INSTANCE = Mappers.getMapper(BlogVOMapper.class);

        Blog map(Blog blog);

        List<BlogVO> map(List<Blog> blogs);
    }
}
