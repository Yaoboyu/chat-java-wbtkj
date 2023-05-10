package com.wbtkj.chat.service;

import com.wbtkj.chat.pojo.model.User;

import java.util.List;

public interface AdminService {

    List<User> getUsers(Integer page, Integer pagesize, String email);

    Object addOpenAiKey(String key);

    Object delOpenAiKey(String key);

    Object changeStatus(String key, int status);

    List<String> getKeys();
}
