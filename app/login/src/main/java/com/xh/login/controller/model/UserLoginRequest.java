package com.xh.login.controller.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UserLoginRequest {

    @NotBlank(message = "参数异常")
    @Length(min = 6, max = 32, message = "参数异常")
    @Pattern(regexp = "^[0-9a-zA-Z_]+$", message = "参数异常")
    private String userName;

    @NotBlank(message = "参数异常")
    @Length(max = 64, message = "参数异常")
    private String keyPair;

    @NotBlank(message = "参数异常")
    @Length(max = 4096, message = "参数异常")
    private String encryptedPasswordBase64;
}
