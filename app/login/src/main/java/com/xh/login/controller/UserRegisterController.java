package com.xh.login.controller;

import com.xh.common.model.enums.CommonStatus;
import com.xh.common.model.exception.WebBizException;
import com.xh.common.model.user.UserInfoVO;
import com.xh.common.model.web.Response;
import com.xh.common.utils.validation.ValidatorUtil;
import com.xh.common.utils.web.ControllerTemplate;
import com.xh.login.controller.model.UserRegisterRequest;
import com.xh.login.controller.model.UserRegisterResult;
import com.xh.login.controller.model.WebPublicKeyInfo;
import com.xh.login.model.UserCredentialDTO;
import com.xh.login.model.WebSubmitKeyPair;
import com.xh.login.service.CredentialService;
import com.xh.login.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/api/user")
public class UserRegisterController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private CredentialService credentialService;

    @Autowired
    @Qualifier("postgresqlTransactionTemplate")
    private TransactionTemplate transactionTemplate;

    @RequestMapping(path = "register/encryptKey", method = RequestMethod.GET)
    @ResponseBody
    public Response<WebPublicKeyInfo> getEncryptKey() {
        return ControllerTemplate.process(() -> WebPublicKeyInfo.fromKeyPair(credentialService.generateCachedWebSubmitKeyPair()));
    }

    @RequestMapping(path = "register", method = RequestMethod.POST)
    @ResponseBody
    public Response<UserRegisterResult> register(@RequestBody UserRegisterRequest request) {
        return ControllerTemplate.process(
                () -> ValidatorUtil.validate(request),
                () -> {
                    WebSubmitKeyPair webSubmitKeyPair = credentialService.getCachedWebSubmitKeyPair(request.getKeyPair());
                    if(webSubmitKeyPair == null) {
                        throw new WebBizException("会话已失效", "key_pair_not_exist " + request.getKeyPair());
                    }
                    credentialService.removeCachedWebSubmitKeyPair(request.getKeyPair());

                    UserInfoVO existentUser = userInfoService.getUserInfoByName(request.getUserName());
                    if(existentUser != null){
                        throw new WebBizException("用户名已存在");
                    }

                    UserInfoVO userInfo = new UserInfoVO();
                    userInfo.setId(userInfoService.generateUserId());
                    userInfo.setUserName(request.getUserName());
                    userInfo.setNickName(request.getNickName());
                    userInfo.setStatus(CommonStatus.NORMAL);

                    UserCredentialDTO userCredential = credentialService.generatePasswordCredential(userInfo.getId(), webSubmitKeyPair.getPrivateKey(), request.getEncryptedPasswordBase64());

                    transactionTemplate.execute(status -> {
                        userInfoService.addUserInfo(userInfo);
                        credentialService.saveCredential(userCredential);
                        return null;
                    });

                    UserRegisterResult userRegisterResult = new UserRegisterResult();
                    userRegisterResult.setUserId(userInfo.getId());
                    return userRegisterResult;
        });
    }
}
