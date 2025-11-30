package com.xh.login.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.xh.common.model.user.UserInfoVO;
import com.xh.login.LoginHelper;
import com.xh.login.service.LoginJwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LoginJwtServiceImpl implements LoginJwtService {

    private static final long EXPIRATION_MILLS = 86400000;

    private static final String ISSUER = "login-api";

    @Value("app.api-jwt-secrets")
    private String secretKey;

    @Override
    public String generateToken(UserInfoVO userInfoVO) {
        Date now = new Date();
        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(userInfoVO.getId())
                .withClaim(LoginHelper.USER_NAME_KEY, userInfoVO.getUserName())
                .withClaim(LoginHelper.NICK_NAME_KEY, userInfoVO.getNickName())
                .withIssuedAt(now)
                .withExpiresAt(new Date(now.getTime() + EXPIRATION_MILLS))
                .sign(Algorithm.HMAC256(secretKey));
    }

    @Override
    public DecodedJWT verifyToken(String token) {
        return JWT.require(Algorithm.HMAC256(secretKey))
                .withIssuer(ISSUER)
                .build()
                .verify(token);
    }
}
