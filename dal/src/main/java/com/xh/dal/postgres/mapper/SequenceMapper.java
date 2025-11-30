package com.xh.dal.postgres.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SequenceMapper {

    Long selectNextVal(@Param("seq") String seq);
}
