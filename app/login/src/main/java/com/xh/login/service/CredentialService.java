package com.xh.login.service;

import com.xh.login.model.UserCredentialDTO;
import com.xh.login.model.WebSubmitKeyPair;

public interface CredentialService {

    /**
     * 生成用户密码凭证
     *
     * @param userId uid
     * @param privateKey 解密私钥
     * @param encryptedPasswordBase64 前端加密过的用户密码
     * @return 用户凭证类
     */
    UserCredentialDTO generatePasswordCredential(String userId, String privateKey, String encryptedPasswordBase64);

    /**
     * 持久化用户凭证
     * @param userCredential 用户凭证
     */
    void saveCredential(UserCredentialDTO userCredential);

    /**
     * 校验用户密码凭证
     *
     * @param userId uid
     * @param privateKey 解密私钥
     * @param encryptedPasswordBase64 前端加密过的用户密码
     */
    boolean verifyPasswordCredential(String userId, String privateKey, String encryptedPasswordBase64);

    /**
     * 生成前端密码明文加密密钥并缓存
     * @return
     */
    WebSubmitKeyPair generateCachedWebSubmitKeyPair();

    /**
     * 根据id获取密钥缓存
     * @param keyPairId
     * @return
     */
    WebSubmitKeyPair getCachedWebSubmitKeyPair(String keyPairId);

    /**
     * 根据id移除密钥缓存
     * @param keyPairId
     */
    void removeCachedWebSubmitKeyPair(String keyPairId);
}
