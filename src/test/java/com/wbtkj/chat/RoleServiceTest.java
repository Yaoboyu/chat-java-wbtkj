package com.wbtkj.chat;

import com.wbtkj.chat.config.ThreadLocalConfig;
import com.wbtkj.chat.mapper.RoleMapper;
import com.wbtkj.chat.pojo.dto.user.UserLocalDTO;
import com.wbtkj.chat.pojo.vo.role.RoleHistoryVO;
import com.wbtkj.chat.pojo.vo.role.RoleInfoVO;
import com.wbtkj.chat.service.RoleService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
@EnableConfigurationProperties
public class RoleServiceTest {
    @Resource
    private RoleService roleService;
    @Resource
    RoleMapper roleMapper;

    @Test
    public void addRoleTest() {
        ThreadLocalConfig.setUser(UserLocalDTO.builder().id(1L).email("123@qq.com").build());
//        RoleInfoVO roleInfoVO = RoleInfoVO.builder()
//                .avatar("https://ui-avatars.com/api/?background=70a99b&color=fff&name=GPT")
//                .nickname("John")
//                .greeting("Hello, world!")
//                .model("gpt-3.5-turbo")
//                .system("")
//                .contextN(10)
//                .maxTokens(1000)
//                .temperature(0.7)
//                .topP(0.9)
//                .frequencyPenalty(1.0)
//                .presencePenalty(1.0)
//                .isMarket(false)
//                .fileNames(Arrays.asList("file-123m123","url-asdjkv23jhb"))
//                .marketType(1)
//                .fileNames(Arrays.asList("file-2kjbndfkbl1kjbf1","file-fdknmsbfjkl2bjgfb"))
//                .build();
//        roleService.addRole(roleInfoVO);

        List<Object> userRole = roleService.getUserRole();

        System.out.println(userRole);
    }

    @Test
    public void updateRoleTest() {
        ThreadLocalConfig.setUser(UserLocalDTO.builder().id(1L).email("123@qq.com").build());
        RoleInfoVO roleInfoVO = RoleInfoVO.builder()
                .id(2L)
                .nickname("martin")
                .isMarket(false)
                .build();
        roleService.updateRole(roleInfoVO);
    }

    @Test
    public void addRoleByIdTest(){
        ThreadLocalConfig.setUser(UserLocalDTO.builder().id(1l).email("123@qq.com").build());
        roleService.addRoleById(2);
    }

    @Test
    public void getRoleHistoryTest(){
        ThreadLocalConfig.setUser(UserLocalDTO.builder().id(2l).email("123@qq.com").build());
        List<RoleHistoryVO> roleHistory = roleService.getRoleHistory(1);
        System.out.println(roleHistory);
    }
}
