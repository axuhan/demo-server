package com.xh.login.model;

import lombok.Data;

import java.security.KeyPair;
import java.util.Base64;

@Data
public class WebSubmitKeyPair {

    private String id;

    private String publicKey;

    private String privateKey;

    public static WebSubmitKeyPair fromKeyPair(String id, KeyPair keyPair) {
        WebSubmitKeyPair webSubmitKeyPair = new WebSubmitKeyPair();
        webSubmitKeyPair.setId(id);
        webSubmitKeyPair.setPublicKey(Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
        webSubmitKeyPair.setPrivateKey(Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
        return webSubmitKeyPair;
    }
}
