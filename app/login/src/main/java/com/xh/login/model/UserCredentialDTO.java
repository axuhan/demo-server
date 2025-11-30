package com.xh.login.model;

import com.xh.common.model.enums.CommonStatus;
import lombok.Data;

@Data
public class UserCredentialDTO {

    private Integer id;

    private String userId;

    private CredentialType credentialType;

    private String credentialValue;

    private CommonStatus status;
}
