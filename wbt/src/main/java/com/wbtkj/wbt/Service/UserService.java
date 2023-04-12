package com.wbtkj.wbt.Service;

import com.wbtkj.wbt.Mapper.UserMapper;
import com.wbtkj.wbt.Pojo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;

public interface UserService {
    int UserNumber();
    void save(UserInfo userInfo) throws Exception;

}
