package com.johndoeo.bootvueserver;

import com.johndoeo.bootvueserver.controller.UserController;
import com.johndoeo.bootvueserver.module.User;
import com.johndoeo.bootvueserver.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BootVueServerApplicationTests {

    @Autowired
    private IUserService userService;
    @Test
    public void contextLoads() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("admin");
        final List<User> users = userService.selectByColumn(user);
        System.out.println(users);
    }

}

