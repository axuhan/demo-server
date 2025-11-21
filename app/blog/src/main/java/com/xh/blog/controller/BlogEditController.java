package com.xh.blog.controller;

import com.xh.blog.controller.model.request.BlogEditRequest;
import com.xh.blog.controller.model.response.Response;
import com.xh.dal.postgres.mapper.BlogMapper;
import com.xh.dal.postgres.model.Blog;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping(path = "blog/edit")
public class BlogEditController {

    @Resource
    private BlogMapper blogMapper;

    @RequestMapping(path = "newBlog", method = RequestMethod.POST)
    @ResponseBody
    public Response<String> newBlog(@RequestBody BlogEditRequest request) {
        return ControllerTemplate.process(() -> {
            Blog record = new Blog();
            record.setTitle(request.getTitle());
            record.setBlogText(request.getBlogText());
            blogMapper.insertSelective(record);
            return "success";
        });
    }
}
