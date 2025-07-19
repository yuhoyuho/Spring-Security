package com.example.demo.entity;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel {

    private UUID id;
    private final LocalTime createdAt;
    private LocalTime updatedAt;

    private String name;
    private List<User> users = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();

    public Channel(String name) {
        this.id = UUID.randomUUID();
        this.createdAt = LocalTime.now();
        this.updatedAt = LocalTime.now();

        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Channel{");
        sb.append("name='").append(name).append('\'');
//        sb.append(", users=").append(users);
//        -> 이 부분을 주석 처리 하지 않으면 user.toString과 channel.toString이 순환 참조되어 스택 오버플로우 발생
        sb.append(", messages=").append(messages);
        sb.append('}');
        return sb.toString();
    }
}
