package com.xh.login.controller.model;

import com.xh.login.model.WebSubmitKeyPair;
import lombok.Data;

@Data
public class WebPublicKeyInfo {

    private String keyPair;

    private String publicKey;

    public static WebPublicKeyInfo fromKeyPair(WebSubmitKeyPair keyPair) {
        WebPublicKeyInfo publicKeyInfo = new WebPublicKeyInfo();
        publicKeyInfo.setKeyPair(keyPair.getId());
        publicKeyInfo.setPublicKey(keyPair.getPublicKey());
        return publicKeyInfo;
    }
}
