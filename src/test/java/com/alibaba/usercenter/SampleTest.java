package com.alibaba.usercenter;

import com.alibaba.usercenter.mapper.UserMapper;
import com.alibaba.usercenter.model.User;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import jakarta.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SampleTest {

    @Resource
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);  // null表示没有查询条件，查询所有
        Assert.isTrue(5 == userList.size(), "");
        userList.forEach(System.out::println);
    }
}
