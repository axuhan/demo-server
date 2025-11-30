package com.xh.login.controller;

import com.xh.common.model.exception.WebBizException;
import com.xh.common.model.user.UserInfoVO;
import com.xh.common.model.web.Response;
import com.xh.common.utils.validation.ValidatorUtil;
import com.xh.common.utils.web.ControllerTemplate;
import com.xh.login.Login;
import com.xh.login.LoginHelper;
import com.xh.login.LoginInfo;
import com.xh.login.controller.model.UserLoginRequest;
import com.xh.login.controller.model.WebPublicKeyInfo;
import com.xh.login.model.WebSubmitKeyPair;
import com.xh.login.service.CredentialService;
import com.xh.login.service.LoginJwtService;
import com.xh.login.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(path = "/api/login")
public class UserLoginController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private CredentialService credentialService;

    @Autowired
    private LoginJwtService  loginJwtService;

    @RequestMapping(path = "encryptKey", method = RequestMethod.GET)
    @ResponseBody
    public Response<WebPublicKeyInfo> getEncryptKey() {
        return ControllerTemplate.process(() -> WebPublicKeyInfo.fromKeyPair(credentialService.generateCachedWebSubmitKeyPair()));
    }

    @RequestMapping(path = "", method = RequestMethod.POST)
    @ResponseBody
    public Response<String> login(HttpServletResponse response, @RequestBody UserLoginRequest request) {
        return ControllerTemplate.process(
                () -> ValidatorUtil.validate(request),
                () -> {
                    WebSubmitKeyPair webSubmitKeyPair = credentialService.getCachedWebSubmitKeyPair(request.getKeyPair());
                    if(webSubmitKeyPair == null) {
                        throw new WebBizException("会话已失效", "key_pair_not_exist");
                    }
                    credentialService.removeCachedWebSubmitKeyPair(request.getKeyPair());

                    UserInfoVO userInfoVO = userInfoService.getUserInfoByName(request.getUserName());
                    if (userInfoVO == null) {
                        throw new WebBizException("用户名或密码不正确", "user_not_exist");
                    }

                    boolean verifyResult = credentialService.verifyPasswordCredential(userInfoVO.getId(), webSubmitKeyPair.getPrivateKey(), request.getEncryptedPasswordBase64());
                    if(verifyResult) {
                        LoginHelper.addLoginTokenToCookie(response, loginJwtService.generateToken(userInfoVO));
                        return "OK";
                    } else {
                        throw new WebBizException("用户名或密码不正确", "password_verification_failed");
                    }
        });
    }

    @RequestMapping(path = "isLogin", method = RequestMethod.GET)
    @ResponseBody
    @Login(force = false)
    public Response<String> isLogin(LoginInfo loginInfo) {
        return ControllerTemplate.process(() -> loginInfo == null ? "false" : "true");
    }
}
