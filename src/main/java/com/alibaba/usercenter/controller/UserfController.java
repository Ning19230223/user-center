package com.alibaba.usercenter.controller;

import com.alibaba.usercenter.model.domain.Userf;
import com.alibaba.usercenter.model.request.UserfLoginRequest;
import com.alibaba.usercenter.model.request.UserfRegisterRequest;
import com.alibaba.usercenter.service.UserfService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户接口
 */
@RestController
@RequestMapping("/user")
public class UserfController {

    @Resource
    private UserfService userfService;

    @PostMapping("/register")
    public Long userfRegister(@RequestBody UserfRegisterRequest userfRegisterRequest) {
        if (userfRegisterRequest == null) {
            return null;
        }
        String userAccount = userfRegisterRequest.getUserAccount();
        String userPassword = userfRegisterRequest.getUserPassword();
        String checkPassword = userfRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        return userfService.userfRegister(userAccount, userPassword, checkPassword);
    }

    @PostMapping("/login")
    public Userf userfLogin(@RequestBody UserfLoginRequest userfLoginRequest, HttpServletRequest request) {
        if (userfLoginRequest == null) {
            return null;
        }
        String userAccount = userfLoginRequest.getUserAccount();
        String userPassword = userfLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        return userfService.userfLogin(userAccount, userPassword, request);
    }
}
