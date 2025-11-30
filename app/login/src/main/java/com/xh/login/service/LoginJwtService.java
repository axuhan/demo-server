package com.xh.login.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.xh.common.model.user.UserInfoVO;

public interface LoginJwtService {

    String generateToken(UserInfoVO userInfoVO);

    DecodedJWT verifyToken(String token);
}
