package com.xh.login;

import com.xh.login.service.LoginJwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginInterceptor.class);

    @Autowired
    private LoginJwtService loginJwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        Login login = method.getAnnotation(Login.class);

        //没有@Login注解的方法不需要解析token
        if(login == null) {
            return true;
        }

        LoginInfo loginInfo = null;
        try {
            //尝试解析登录token数据并置入Holder
            loginInfo = LoginHelper.parseLoginTokenFromCookie(request, loginJwtService);
            if(loginInfo != null) {
                LoginHelper.putLoginInfoToRequest(request, loginInfo);
            }
        } catch (Exception e) {
            LOGGER.warn("try_set_login_holder_fail", e);
        }

        if(loginInfo == null && login.force()){
            //在未登录状态下访问需要登录的接口，返回状态码401+响应头Need-Login，前端识别并跳转到登录页
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Need-Login");
            response.setHeader("Need-Login", "true");
            return false;
        }

        return true;
    }
}
