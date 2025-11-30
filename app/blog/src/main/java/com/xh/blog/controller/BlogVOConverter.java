package com.xh.blog.controller;

import com.xh.blog.controller.model.response.BlogVO;
import com.xh.dal.postgres.model.Blog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
interface BlogVOConverter {
    BlogVOConverter INSTANCE = Mappers.getMapper(BlogVOConverter.class);

    @Mappings({
            @Mapping(target = "content", source = "blogText")
    })
    BlogVO toVO(Blog blog);

    List<BlogVO> toVO(List<Blog> blogs);
}
