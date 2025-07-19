package com.example.demo.service;

import com.example.demo.entity.Channel;
import com.example.demo.entity.User;

import java.util.*;

public class UserService {
    private Map<UUID, User> users = new HashMap<>();

    public User createUser(String name, String email) {
        User user = new User(name, email);
        users.put(user.getId(), user);

        return user;
    }

    public User find(UUID userId) {
        return users.get(userId);
    }

    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        list.addAll(users.values());

        return list;
    }

    public void update(User user) {
        users.put(user.getId(), user);
    }

    public void delete(UUID userId) {
        users.remove(userId);
    }

    public void joinChannel(User user, Channel channel) {
        if(!user.getChannels().contains(channel)) {
            user.getChannels().add(channel);
            channel.getUsers().add(user);
        }
    }
}
