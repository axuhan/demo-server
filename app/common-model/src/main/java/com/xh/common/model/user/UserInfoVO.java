package com.xh.common.model.user;

import com.xh.common.model.enums.CommonStatus;
import lombok.Data;

@Data
public class UserInfoVO {

    private String id;

    private String userName;

    private String nickName;

    private String profile;

    private CommonStatus status;
}
