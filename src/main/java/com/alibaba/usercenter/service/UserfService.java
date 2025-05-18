package com.alibaba.usercenter.service;

import com.alibaba.usercenter.model.domain.Userf;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author ning
* @description 针对表【userf(用户)】的数据库操作Service
* @createDate 2025-05-17 13:57:37
*/
public interface UserfService extends IService<Userf> {

    /**
     * 用户注释
     *
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @return 新用户id
     */
    long userfRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @return 脱敏后的用户信息
     */
    Userf userfLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     * @param originUserf
     * @return
     */
    Userf getSafetyUserf(Userf originUserf);
}
