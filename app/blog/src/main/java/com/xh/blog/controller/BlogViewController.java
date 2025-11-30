package com.xh.blog.controller;

import com.xh.blog.controller.model.response.BlogVO;
import com.xh.common.model.enums.CommonStatus;
import com.xh.common.model.web.Response;
import com.xh.common.utils.web.ControllerTemplate;
import com.xh.dal.postgres.mapper.BlogMapper;
import com.xh.dal.postgres.model.Blog;
import com.xh.dal.postgres.model.BlogExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping(path = "/api/blog/view")
public class BlogViewController {

    @Autowired
    private BlogMapper blogMapper;

    @RequestMapping(path = "{blogId}", method = RequestMethod.GET)
    @ResponseBody
    public Response<BlogVO> view(HttpServletResponse response, @PathVariable("blogId") String blogId){
        return ControllerTemplate.process(() -> {
            Integer id = null;
            try {
                id = Integer.valueOf(blogId);
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return null;
            }
            BlogExample example = new BlogExample();
            example.createCriteria()
                    .andIdEqualTo(id)
                    .andStatusEqualTo(CommonStatus.NORMAL.getNumber())
                    ;
            List<Blog> records = blogMapper.selectByExample(example);
            return records.stream().findFirst().map(BlogVOConverter.INSTANCE::toVO).orElseGet(() -> {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return null;
            });
        });
    }
}
