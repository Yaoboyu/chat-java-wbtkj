package com.wbtkj.chat.config;

import com.wbtkj.chat.exception.MyServiceException;
import com.wbtkj.chat.pojo.DTO.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class ThreadLocalConfig {

    // jdk建议将 ThreadLocal 定义为 private static ，这样就不会有弱引用，内存泄漏的问题了
    private static ThreadLocal<UserDTO> localUser = new ThreadLocal<>();

    //获取当前线程的存的变量
    public static UserDTO getUser() {
        UserDTO user = localUser.get();
        if (null == user) {
            throw new MyServiceException("登录失效，请重新登录！");
        }
        return user;
    }

    //设置当前线程的存的变量
    public static void setgetUser(UserDTO user) {
        localUser.set(user);
    }

    //移除当前线程的存的变量
    public static void remove() {
        localUser.remove();
    }

}
