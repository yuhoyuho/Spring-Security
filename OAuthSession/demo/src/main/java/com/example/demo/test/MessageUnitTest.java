package com.example.demo.test;

import com.example.demo.entity.Channel;
import com.example.demo.entity.Message;
import com.example.demo.entity.User;
import com.example.demo.service.ChannelService;
import com.example.demo.service.MessageService;
import com.example.demo.service.UserService;

public class MessageUnitTest {
    public static void main(String[] args) {
        UserService userService = new UserService();
        ChannelService channelService = new ChannelService();
        MessageService messageService = new MessageService();

        User user = userService.createUser("user1", "<EMAIL>");
        User user2 = userService.createUser("user2", "<EMAIL>");

        Channel channel = channelService.createChannel("channel1");

        // create
        Message message1 = messageService.sendMessage(user, channel, "첫번째");
        System.out.println(message1);

        // update
        Message updateMessage = messageService.updateMessage(message1, "첫번째_수정");
        System.out.println(updateMessage);

        // delete
        System.out.println(channel.getMessages().toString());
        messageService.deleteMessage(message1);
        System.out.println(channel.getMessages().toString());
    }
}
