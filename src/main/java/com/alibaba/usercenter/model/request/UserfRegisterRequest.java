package com.alibaba.usercenter.model.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户注册请求体
 */
@Data
public class UserfRegisterRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -4247539863543790287L;
    
    private String userAccount;

    private String userPassword;

    private String checkPassword;
}
