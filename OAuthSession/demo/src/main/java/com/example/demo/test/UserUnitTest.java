package com.example.demo.test;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;

public class UserUnitTest {
    public static void main(String[] args) {
        UserService userService = new UserService();

        User user1 = userService.createUser("user1", "<EMAIL>");
        User user2 = userService.createUser("user2", "<EMAIL>");

        // find
        System.out.println(userService.find(user1.getId()).toString());

        // findAll
        System.out.println(userService.findAll().toString());

        // update
        user1.setName("update_name");
        userService.update(user1);
        System.out.println(userService.find(user1.getId()).toString());

        // delete
        userService.delete(user2.getId());
        System.out.println(userService.findAll().toString());
    }
}
