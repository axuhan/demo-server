package com.xh.blog.controller;

import com.xh.blog.controller.model.request.BlogEditRequest;
import com.xh.common.model.enums.CommonStatus;
import com.xh.common.model.web.Response;
import com.xh.common.utils.web.ControllerTemplate;
import com.xh.dal.postgres.mapper.BlogMapper;
import com.xh.dal.postgres.model.Blog;
import com.xh.login.Login;
import com.xh.login.LoginInfo;
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
    @Login
    public Response<String> newBlog(@RequestBody BlogEditRequest request, LoginInfo loginInfo) {
        return ControllerTemplate.process(() -> {
            Blog record = new Blog();
            record.setUserId(loginInfo.getUserId());
            record.setTitle(request.getTitle());
            record.setBlogText(request.getBlogText());
            record.setStatus(CommonStatus.NORMAL.getNumber());
            blogMapper.insertSelective(record);
            return "success";
        });
    }
}
