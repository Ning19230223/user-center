package com.alibaba.usercenter.service.impl;

import com.alibaba.usercenter.model.User;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.alibaba.usercenter.model.domain.Userf;
import com.alibaba.usercenter.service.UserfService;
import com.alibaba.usercenter.mapper.UserfMapper;
import jakarta.annotation.Resource;
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
public class UserfServiceImpl extends ServiceImpl<UserfMapper, Userf> implements UserfService{

    @Resource
    private UserfMapper userfMapper;

    @Override
    public long userfRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
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
        final String SALT = "maning";
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
}




