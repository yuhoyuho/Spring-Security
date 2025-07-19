package com.example.demo.test;

import com.example.demo.entity.Channel;
import com.example.demo.entity.User;
import com.example.demo.service.ChannelService;

public class ChannelUnitTest {
    public static void main(String[] args) {
        ChannelService channelService = new ChannelService();

        Channel channel1 = channelService.createChannel("channel1");
        Channel channel2 = channelService.createChannel("channel2");

        // find
        Channel find = channelService.find(channel1.getId());
        System.out.println(find.toString());

        // findAll
        System.out.println(channelService.findAll().toString());

        // update
        channel1.setName("update_name");
        channelService.update(channel1);
        System.out.println(channelService.find(channel1.getId()).toString());

        // delete
        channelService.delete(channel2.getId());
        System.out.println(channelService.findAll().toString());

        Channel channel3 = channelService.createChannel("channel3");

        User user1 = new User("user1", "<EMAIL>");
        User user2 = new User("user2", "<EMAIL>");
        channel1.getUsers().add(user1);
        channel1.getUsers().add(user2);
        channel3.getUsers().add(user1);

        // findByUser
        System.out.println(channelService.findByUser(user1).toString());

        // getUsers
        System.out.println(channel1.getUsers().toString());

        // getMessages
        System.out.println(channel1.getMessages().toString());
    }
}
