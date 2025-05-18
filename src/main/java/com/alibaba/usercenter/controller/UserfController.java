package com.alibaba.usercenter.controller;

import com.alibaba.usercenter.model.domain.Userf;
import com.alibaba.usercenter.model.request.UserfLoginRequest;
import com.alibaba.usercenter.model.request.UserfRegisterRequest;
import com.alibaba.usercenter.service.UserfService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.alibaba.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.alibaba.usercenter.constant.UserConstant.USER_LOGIN_STATE;

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

    @GetMapping("/search")
    public List<Userf> searchUsers(String username, HttpServletRequest request) {
        // 是否为管理员
        if (!isAdmin(request)) {
            return new ArrayList<>();
        }
        QueryWrapper<Userf> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<Userf> userfList = userfService.list(queryWrapper);
        return userfList.stream().map(userf -> userfService.getSafetyUserf(userf)).collect(Collectors.toList());
    }

    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody long id, HttpServletRequest request) {
        // 是否为管理员
        if (!isAdmin(request)) {
            return false;
        }
        if (id <= 0) {
            return false;
        }
        return userfService.removeById(id);
    }

    /**
     * 是否为管理员
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        Userf userf = (Userf) userObj;
        return userf != null && userf.getUserRole() == ADMIN_ROLE;
    }
}
