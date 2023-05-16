package com.wbtkj.chat.config;

import com.wbtkj.chat.exception.MyServiceException;
import com.wbtkj.chat.pojo.dto.admin.AdminLocalDTO;
import com.wbtkj.chat.pojo.dto.user.UserLocalDTO;
import org.springframework.stereotype.Component;

@Component
public class ThreadLocalConfig {

    // jdk建议将 ThreadLocal 定义为 private static ，这样就不会有弱引用，内存泄漏的问题了
    private static ThreadLocal<UserLocalDTO> localUser = new ThreadLocal<>();
    private static ThreadLocal<AdminLocalDTO> localAdmin = new ThreadLocal<>();

    //获取当前线程的存的变量
    public static UserLocalDTO getUser() {
        UserLocalDTO user = localUser.get();
        if (null == user) {
            throw new MyServiceException("登录失效，请重新登录！");
        }
        return user;
    }

    //设置当前线程的存的变量
    public static void setUser(UserLocalDTO user) {
        localUser.set(user);
    }

    public static AdminLocalDTO getAdmin() {
        AdminLocalDTO admin = localAdmin.get();
        if (null == admin) {
            throw new MyServiceException("登录失效，请重新登录！");
        }
        return admin;
    }

    public static void setAdmin(AdminLocalDTO admin) {
        localAdmin.set(admin);
    }

    //移除当前线程的存的变量
    public static void remove() {
        localUser.remove();
        localAdmin.remove();
    }

}
