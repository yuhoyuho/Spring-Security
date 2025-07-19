package com.example.demo.service;

import com.example.demo.entity.Channel;
import com.example.demo.entity.Message;
import com.example.demo.entity.User;

public class MessageService {

    public Message sendMessage(User user, Channel channel, String content) {
        Message message = new Message(user, channel, content);
        channel.getMessages().add(message);
        user.getMessages().add(message);

        return message;
    }

    public void deleteMessage(Message message) {
        User user = message.getUser();
        Channel channel = message.getChannel();

        user.getMessages().remove(message);
        channel.getMessages().remove(message);
    }

    public Message updateMessage(Message message, String newContent) {
        User user = message.getUser();
        Channel channel = message.getChannel();

        for(Message m : user.getMessages()) {
            if(m.getId().equals(message.getId())) {
                m.setContent(newContent);
            }
        }

        return message;
    }
}
