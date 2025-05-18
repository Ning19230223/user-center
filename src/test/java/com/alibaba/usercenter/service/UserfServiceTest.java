package com.alibaba.usercenter.service;

import com.alibaba.usercenter.model.domain.Userf;
import jakarta.annotation.Resource;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserfServiceTest {

    @Resource
    private UserfService userfService;

    @Test
    public void testAddUser() {
        Userf userf = new Userf();
        userf.setUsername("dogManing");
        userf.setUserAccount("123");
        userf.setAvatarUrl("https://img95.699pic.com/element/40252/6545.png_860.png");
        userf.setGender(0);
        userf.setUserPassword("xxx");
        userf.setPhone("123");
        userf.setEmail("456");
        boolean result = userfService.save(userf);
        System.out.println(userf.getId());
        Assertions.assertTrue(result);
    }

    @Test
    public void userfRegister() {
        String userAccount = "maning";
        String userPassword = "";
        String checkPassword = "123456";
        long result = userfService.userfRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);
        userAccount = "ma";
        result = userfService.userfRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);
        userAccount = "maning";
        userPassword = "123456";
        result = userfService.userfRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);
        userAccount = "ma ning";
        userPassword = "12345678";
        checkPassword = "12345678";
        result = userfService.userfRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);
        checkPassword = "123456789";
        result = userfService.userfRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);
        userAccount = "123";
        checkPassword = "12345678";
        result = userfService.userfRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);
        userAccount = "maning";
        result = userfService.userfRegister(userAccount, userPassword, checkPassword);
        Assertions.assertTrue(result > 0);
    }
}