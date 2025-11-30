package com.xh.login.service;

import com.xh.common.model.user.UserInfoVO;

public interface UserInfoService {

    UserInfoVO getUserInfo(String userID);

    UserInfoVO getUserInfoByName(String userName);

    void addUserInfo(UserInfoVO userInfo);

    String generateUserId();
}
