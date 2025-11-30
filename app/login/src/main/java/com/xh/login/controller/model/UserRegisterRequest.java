package com.xh.login.controller.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UserRegisterRequest {

    @NotBlank(message = "用户名不能为空")
    @Length(min = 6, max = 32, message = "用户名长度必须介于6-32个字符")
    @Pattern(regexp = "^[0-9a-zA-Z_]+$", message = "用户名只能由位数字、字母、下划线组成")
    private String userName;

    @NotBlank(message = "昵称不能为空")
    @Length(min = 6, max = 32, message = "昵称长度必须介于6-32个字符")
    @Pattern(regexp = "^[0-9a-zA-Z_]+$", message = "昵称只能由位数字、字母、下划线组成")
    private String nickName;

    @NotBlank(message = "参数异常")
    @Length(max = 64, message = "参数异常")
    private String keyPair;

    @NotBlank(message = "参数异常")
    @Length(max = 4096, message = "参数异常")
    private String encryptedPasswordBase64;
}
