package com.example.demo.service;

import com.example.demo.entity.Channel;
import com.example.demo.entity.User;

import java.util.*;
import java.util.stream.Collectors;

public class ChannelService {

    private Map<UUID, Channel> channels = new HashMap<>();

    public Channel createChannel(String name) {
        Channel channel = new Channel(name);
        channels.putIfAbsent(channel.getId(), channel);

        return channel;
    }

    public Channel find(UUID channelId) {
        return channels.get(channelId);
    }

    public List<Channel> findAll() {
        List<Channel> list = new ArrayList<>();
        list.addAll(channels.values());

        return list;
    }

    public List<Channel> findByUser(User user) {
        return channels.values()
                .stream()
                .filter(channel ->
                        channel.getUsers().stream()
                                .anyMatch(u -> u.getId().equals(user.getId()))
                )
                .collect(Collectors.toList());
    }

    public void update(Channel channel) {
        channels.put(channel.getId(), channel);
    }

    public void delete(UUID channelId) {
        channels.remove(channelId);
    }
}
