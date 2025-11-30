package com.xh.login.service.impl;

import com.xh.common.model.enums.CommonStatus;
import com.xh.common.model.user.UserInfoVO;
import com.xh.dal.postgres.mapper.SequenceMapper;
import com.xh.dal.postgres.mapper.UserInfoMapper;
import com.xh.dal.postgres.model.UserInfo;
import com.xh.dal.postgres.model.UserInfoExample;
import com.xh.login.service.UserInfoService;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private SequenceMapper sequenceMapper;

    @Override
    public UserInfoVO getUserInfo(String userId) {
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
        return UserInfoConverter.INSTANCE.toVO(userInfo);
    }

    @Override
    public UserInfoVO getUserInfoByName(String userName) {
        UserInfoExample example = new UserInfoExample();
        example.createCriteria()
                .andUserNameEqualTo(userName);
        List<UserInfo> list = userInfoMapper.selectByExample(example);
        return list.stream().findFirst().map(UserInfoConverter.INSTANCE::toVO).orElse(null);
    }

    @Override
    public void addUserInfo(UserInfoVO userInfo) {
        UserInfo record = UserInfoConverter.INSTANCE.toDO(userInfo);
        userInfoMapper.insertSelective(record);
    }

    @Override
    public String generateUserId() {
        Long userIdSeq = sequenceMapper.selectNextVal("user_id_seq");
        return userIdSeq.toString();
    }

    @Mapper
    interface UserInfoConverter extends CommonStatus.CommonStatusConverter {

        UserInfoConverter INSTANCE = Mappers.getMapper(UserInfoConverter.class);

        UserInfoVO toVO(UserInfo record);

        UserInfo toDO(UserInfoVO vo);

    }
}
