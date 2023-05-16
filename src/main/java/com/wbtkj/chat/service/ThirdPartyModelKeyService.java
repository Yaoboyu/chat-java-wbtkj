package com.wbtkj.chat.service;

import com.wbtkj.chat.pojo.model.ThirdPartyModelKey;

import java.util.List;

public interface ThirdPartyModelKeyService {

    List<ThirdPartyModelKey> getAllKey();

    boolean addKey(String key, String model);

    boolean delKey(long id);

    boolean changeStatus(String key, int status);

    List<String> getEnableGpt3Keys();

    List<String> getEnableGpt4Keys();

}
