package com.example.demo.entity;

import java.time.LocalTime;
import java.util.UUID;

public class Message {

    private UUID id;
    private final LocalTime createdAt;
    private LocalTime updatedAt;

    private User user;
    private Channel channel;
    private String content;

    public Message(User user, Channel channel, String content) {
        this.id = UUID.randomUUID();
        this.createdAt = LocalTime.now();
        this.updatedAt = LocalTime.now();
        this.user = user;
        this.channel = channel;
        this.content = content;
    }

    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Message{");
        sb.append("user=").append(user.getName());
        sb.append(", channel=").append(channel.getName());
        sb.append(", content='").append(content).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
