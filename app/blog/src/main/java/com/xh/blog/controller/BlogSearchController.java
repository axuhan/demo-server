package com.xh.blog.controller;


import com.xh.blog.controller.model.request.BlogSearchRequest;
import com.xh.blog.controller.model.response.BlogVO;
import com.xh.blog.controller.model.response.Page;
import com.xh.common.model.web.Response;
import com.xh.common.utils.validation.ValidatorUtil;
import com.xh.common.utils.web.ControllerTemplate;
import com.xh.dal.postgres.mapper.BlogMapper;
import com.xh.dal.postgres.model.Blog;
import com.xh.dal.postgres.model.BlogExample;
import com.xh.login.Login;
import com.xh.login.LoginInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping(path = "/api/blog/search")
public class BlogSearchController {

    @Resource
    private BlogMapper blogMapper;

    @RequestMapping(path = "my-blog", method = RequestMethod.POST)
    @ResponseBody
    @Login
    public Response<Page<BlogVO>> myBlog(@RequestBody BlogSearchRequest request, LoginInfo loginInfo) {
        return ControllerTemplate.process(
                () -> ValidatorUtil.validate(request),
                () -> {
                    BlogExample example = new BlogExample();
                    example.createCriteria()
                            .andUserIdEqualTo(loginInfo.getUserId())
                    ;
                    long total = blogMapper.countByExample(example);
                    example.setOrderByClause("gmt_create desc");
                    example.setOffset(request.getOffset());
                    example.setLimit(request.getPageSize());
                    List<Blog> blogs = blogMapper.selectByExampleWithoutBlobs(example);
                    return Page.of(request, BlogVOConverter.INSTANCE.toVO(blogs), total);
        });
    }

}
