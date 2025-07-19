package com.example.demo.test;

import com.example.demo.entity.Channel;
import com.example.demo.entity.Message;
import com.example.demo.entity.User;
import com.example.demo.service.ChannelService;
import com.example.demo.service.MessageService;
import com.example.demo.service.UserService;

public class IntegrationTest {
    public static void main(String[] args) {
        System.out.println("integration test");
        System.out.println("------------------------");

        UserService userService = new UserService();
        MessageService messageService = new MessageService();
        ChannelService channelService = new ChannelService();

        User user = userService.createUser("이유호", "EMAIL");
        Channel channel = channelService.createChannel("스프린트 5기");
        Message message = messageService.sendMessage(user, channel, "안녕하세요.");
        Message message1 = messageService.sendMessage(user, channel, "반갑습니다.");

        userService.joinChannel(user, channel);

        System.out.println(user);
        System.out.println(channel.toString());
        System.out.println(message.toString());
        System.out.println(message1.toString());

    }
}
