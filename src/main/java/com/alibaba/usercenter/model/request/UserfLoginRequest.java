package com.alibaba.usercenter.model.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户登录请求体
 */
@Data
public class UserfLoginRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -3011067902322541292L;

    private String userAccount;

    private String userPassword;

}
