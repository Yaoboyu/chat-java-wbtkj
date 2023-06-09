package com.wbtkj.chat.service.impl;

import com.wbtkj.chat.config.ThreadLocalConfig;
import com.wbtkj.chat.mapper.UserFileMapper;
import com.wbtkj.chat.pojo.dto.file.UserFileType;
import com.wbtkj.chat.pojo.model.UserFile;
import com.wbtkj.chat.pojo.model.UserFileExample;
import com.wbtkj.chat.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    @Resource
    UserFileMapper userFileMapper;

    @Override
    @Transactional
    public long addUserFile(String originalName, UserFileType type) {
        UserFile userFile = new UserFile();
        userFile.setUserId(ThreadLocalConfig.getUser().getId());
        userFile.setType(type.getType());
        userFile.setOriginalName(originalName);

        userFileMapper.insert(userFile);

        return userFile.getId();
    }

    @Override
    @Transactional
    public boolean addNameAfterParse(long id, String name) {
        UserFile userFile = new UserFile();
        userFile.setId(id);
        userFile.setName(name);

        userFileMapper.updateByPrimaryKeySelective(userFile);

        return true;
    }

    @Override
    @Transactional
    public List<UserFile> getUserFile() {
        UserFileExample userFileExample = new UserFileExample();
        userFileExample.createCriteria().andUserIdEqualTo(ThreadLocalConfig.getUser().getId());
        List<UserFile> userFiles = userFileMapper.selectByExample(userFileExample);
        return userFiles;
    }
}
