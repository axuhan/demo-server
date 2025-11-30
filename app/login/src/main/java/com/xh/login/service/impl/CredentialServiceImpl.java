package com.xh.login.service.impl;

import com.xh.common.model.enums.CommonStatus;
import com.xh.dal.postgres.mapper.UserCredentialMapper;
import com.xh.dal.postgres.model.UserCredential;
import com.xh.dal.postgres.model.UserCredentialExample;
import com.xh.login.model.CredentialType;
import com.xh.login.model.UserCredentialDTO;
import com.xh.login.model.WebSubmitKeyPair;
import com.xh.login.service.CredentialService;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@CacheConfig(cacheManager = "CaffeineCacheManager")
public class CredentialServiceImpl implements CredentialService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CredentialServiceImpl.class);

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[0-9a-zA-Z_!@#$%^&*,./?;:]+$");

    @Resource
    private UserCredentialMapper userCredentialMapper;

    @Override
    public UserCredentialDTO generatePasswordCredential(String userId, String privateKey, String encryptedPasswordBase64) {
        byte[] password = decryptPassword(privateKey, encryptedPasswordBase64);
        validatePasswordAndThrow(password);
        String credentialValue = generatePasswordCredentialValue(password);
        UserCredentialDTO userCredential = new UserCredentialDTO();
        userCredential.setUserId(userId);
        userCredential.setCredentialType(CredentialType.PASSWORD);
        userCredential.setCredentialValue(credentialValue);
        userCredential.setStatus(CommonStatus.NORMAL);
        return userCredential;
    }

    @Override
    public void saveCredential(UserCredentialDTO userCredential) {
        UserCredential record = UserCredentialConverter.INSTANCE.toDO(userCredential);
        userCredentialMapper.insertSelective(record);
    }

    @Override
    public boolean verifyPasswordCredential(String userId, String privateKey, String encryptedPasswordBase64) {
        UserCredentialExample example = new UserCredentialExample();
        example.createCriteria()
                .andUserIdEqualTo(userId)
                .andCredentialTypeEqualTo(CredentialType.PASSWORD.name())
                .andStatusEqualTo(CommonStatus.NORMAL.getNumber());
        List<UserCredential> records = userCredentialMapper.selectByExample(example);
        UserCredentialDTO credential = records.stream().findFirst().map(UserCredentialConverter.INSTANCE::toDTO).orElse(null);
        if(credential == null) {
            throw new RuntimeException("user_credential_not_found");
        }
        return comparePasswordCredentialValue(
                decryptPassword(privateKey, encryptedPasswordBase64),
                credential.getCredentialValue()
        );
    }

    @Override
    @CachePut(value = "CredentialKeyPairCache", key = "#result.id")
    public WebSubmitKeyPair generateCachedWebSubmitKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            return WebSubmitKeyPair.fromKeyPair(UUID.randomUUID().toString(), keyPair);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("generate_web_submit_key_pair_fail", e);
        }
    }

    @Override
    @Cacheable(value = "CredentialKeyPairCache", key = "#keyPairId")
    public WebSubmitKeyPair getCachedWebSubmitKeyPair(String keyPairId) {
        //不做任何操作，@Cacheable注解执行缓存查询动作
        return null;
    }

    @Override
    @CacheEvict(value = "CredentialKeyPairCache", key = "#keyPairId")
    public void removeCachedWebSubmitKeyPair(String keyPairId) {
        //不做任何操作，@CacheEvict注解执行缓存移除动作
    }

    private byte[] decryptPassword(String privateKeyBase64, String encryptedPasswordBase64) {
        Cipher cipher = null;
        try {
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyBase64));
            PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(privateKeySpec);
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(Base64.getDecoder().decode(encryptedPasswordBase64));
        } catch (Exception e) {
            throw new RuntimeException("decrypt_password_error", e);
        }
    }

    private String generatePasswordCredentialValue(byte[] password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        return encoder.encode(new String(password));
    }

    private boolean comparePasswordCredentialValue(byte[] password, String credentialValue) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        return encoder.matches(new String(password), credentialValue);
    }

    private void validatePasswordAndThrow(byte[] password) {
        String passwordStr = new String(password);
        Matcher matcher = PASSWORD_PATTERN.matcher(passwordStr);
        passwordStr = null;
        if (!matcher.find()) {
            throw new RuntimeException("password_validate_fail");
        }
    }

    @Mapper
    interface UserCredentialConverter extends CommonStatus.CommonStatusConverter {

        UserCredentialConverter INSTANCE = Mappers.getMapper(UserCredentialConverter.class);

        UserCredentialDTO toDTO(UserCredential record);

        UserCredential toDO(UserCredentialDTO dto);

        default CredentialType fromString(String credentialType) {
            return CredentialType.valueOf(credentialType.toUpperCase());
        }

        default String toCredentialTypeString(CredentialType credentialType) {
            return credentialType.name();
        }
    }
}
