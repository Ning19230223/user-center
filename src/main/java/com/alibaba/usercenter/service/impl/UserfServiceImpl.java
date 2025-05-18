package com.alibaba.usercenter.service.impl;
import java.util.Date;

import com.alibaba.usercenter.mapper.UserfMapper;
import com.alibaba.usercenter.model.domain.Userf;
import com.alibaba.usercenter.service.UserfService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author ning
* @description 针对表【userf(用户)】的数据库操作Service实现
* @createDate 2025-05-17 13:57:37
*/
@Service
@Slf4j
public class UserfServiceImpl extends ServiceImpl<UserfMapper, Userf> implements UserfService{

    @Resource
    private UserfMapper userfMapper;

    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "maning";

    /**
     * 用户登录态键
     */
    private static final String USER_LOGIN_STATE = "userLoginState";

    @Override
    public long userfRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            // todo 修改为自定义异常
            return -1;
        }
        if (userAccount.length() < 4) {
            return -1;
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            return -1;
        }
        // 账户不能包含特殊字符
        String validPattern = "[^a-zA-Z0-9._-]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return -1;
        }
        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            return -1;
        }
        // 账户不能重复
        QueryWrapper<Userf> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userfMapper.selectCount(queryWrapper);
        if (count > 0) {
            return -1;
        }
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 3. 插入数据
        Userf userf = new Userf();
        userf.setUserAccount(userAccount);
        userf.setUserPassword(encryptPassword);
        boolean saveResult = this.save(userf);
        if (!saveResult) {
            return -1;
        }
        return userf.getId();
    }

    @Override
    public Userf userfLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        if (userAccount.length() < 4) {
            return null;
        }
        if (userPassword.length() < 8) {
            return null;
        }
        // 账户不能包含特殊字符
        String validPattern = "[^a-zA-Z0-9._-]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return null;
        }
        // 2. 加密
        String encrptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在
        QueryWrapper<Userf> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encrptPassword);
        Userf userf = userfMapper.selectOne(queryWrapper);
        if (userf == null) {
            log.info("userf login failed, userAccount cannot match userPassword.");
            return null;
        }
        // 3. 用户脱敏
        Userf safetyUserf = new Userf();
        safetyUserf.setId(userf.getId());
        safetyUserf.setUsername(userf.getUsername());
        safetyUserf.setUserAccount(userf.getUserAccount());
        safetyUserf.setAvatarUrl(userf.getAvatarUrl());
        safetyUserf.setGender(userf.getGender());
        safetyUserf.setPhone(userf.getPhone());
        safetyUserf.setEmail(userf.getEmail());
        safetyUserf.setUserStatus(userf.getUserStatus());
        safetyUserf.setCreateTime(userf.getCreateTime());
        // 4. 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUserf);
        return safetyUserf;
    }
}




