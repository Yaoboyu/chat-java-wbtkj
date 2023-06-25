package com.wbtkj.chat.service;

import com.wbtkj.chat.pojo.dto.thirdPartyModelKey.KeyAndHost;
import com.wbtkj.chat.pojo.model.ThirdPartyModelKey;

import java.util.List;

public interface ThirdPartyModelKeyService {

    List<ThirdPartyModelKey> getAllKey();

    boolean addKey(ThirdPartyModelKey thirdPartyModelKeyVO);

    boolean delKey(long id);

    boolean changeStatus(long id, int status);

    boolean changeStatus(String key, int status);

    List<KeyAndHost> getEnableGpt3Keys();

    List<KeyAndHost> getEnableGpt4Keys();

}
