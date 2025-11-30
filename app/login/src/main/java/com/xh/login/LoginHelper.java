package com.xh.login;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.xh.login.service.LoginJwtService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginHelper {
    private static final String TOKEN = "token";

    private static final int COOKIE_MAX_AGE = 60 * 60 * 24 * 7;

    public static final String LOGIN_INFO_ATTRIBUTE = "loginInfo";

    public static final String USER_NAME_KEY = "userName";

    public static final String NICK_NAME_KEY = "nickName";

    public static void addLoginTokenToCookie(HttpServletResponse response, String jwtToken) {
        Cookie cookie = new Cookie(TOKEN, jwtToken);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(COOKIE_MAX_AGE);
        response.addCookie(cookie);
    }

    public static LoginInfo parseLoginTokenFromCookie(HttpServletRequest request, LoginJwtService loginJwtService) {
        if(request == null || request.getCookies() == null) {
            return null;
        }
        for (Cookie cookie : request.getCookies()) {
            if(TOKEN.equals(cookie.getName())) {
                String jwtToken = cookie.getValue();
                DecodedJWT decodedJWT = loginJwtService.verifyToken(jwtToken);
                return fromJwt(decodedJWT);
            }
        }
        return null;
    }

    public static void putLoginInfoToRequest(HttpServletRequest request, LoginInfo loginInfo) {
        if(request == null) {
            return;
        }
        request.setAttribute(LOGIN_INFO_ATTRIBUTE, loginInfo);
    }

    public static LoginInfo getLoginInfoFromRequest(HttpServletRequest request) {
        if(request == null) {
            return null;
        }
        return (LoginInfo) request.getAttribute(LOGIN_INFO_ATTRIBUTE);
    }

    private static LoginInfo fromJwt(DecodedJWT decodedJWT) {
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUserId(decodedJWT.getSubject());
        loginInfo.setUserName(decodedJWT.getClaim(USER_NAME_KEY).asString());
        loginInfo.setNickName(decodedJWT.getClaim(NICK_NAME_KEY).asString());

        return loginInfo;
    }
}
