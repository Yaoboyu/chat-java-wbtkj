package com.wbtkj.chat.service;

import com.wbtkj.chat.pojo.dto.openai.chat.Message;
import com.wbtkj.chat.pojo.model.ChatSession;
import com.wbtkj.chat.pojo.vo.role.RoleBriefVO;
import com.wbtkj.chat.pojo.vo.role.RoleHistoryVO;
import com.wbtkj.chat.pojo.vo.role.RoleInfoVO;

import java.util.List;

public interface RoleService {
    /**
     * 获取用户所属角色
     * 如果角色属于用户，则返回所有字段，如果不属于，则返回应用市场查询出来的字段
     * @return
     */
    List<Object> getRole();

    /**
     * 获取上架角色
     * @return
     * @param page
     * @param pageSize
     * @param type
     * @param name
     */
    List<RoleBriefVO> getShopRole(Integer page, Integer pageSize, Integer type, String name);

    /**
     * 添加角色
     * @param roleInfo
     * @return
     */
    boolean addRole(RoleInfoVO roleInfo);

    /**
     * 修改角色
     * @param roleInfo
     * @return
     */
    boolean updateRole(RoleInfoVO roleInfo);

    /**
     * 在应用市场通过id添加role
     * @param roleId
     * @param isNew
     * @return
     */
    boolean addRoleById(long roleId, boolean isNew);

    /**
     * 删除role，只删除UserRole表
     * @param roleId
     * @return
     */
    boolean deleteRole(long roleId);

    /**
     * 获取role的对话历史
     * @param roleId
     * @return
     */
    List<RoleHistoryVO> getRoleHistory(long roleId);

    /**
     * 添加一轮新的对话
     * @param userId
     * @param roleId
     * @return
     */
    ChatSession addChatSession(long userId, long roleId);

    /**
     * 通过chatSessionId获取ChatSession
     * @param chatSessionId
     * @return
     */
    ChatSession getChatSessionById(String chatSessionId);

    /**
     * 更新对话
     * @param chatSessionId mongodb id，可能为null
     * @param messageList 对话历史，可能为null
     * @return
     */
    boolean updateChatSession(String chatSessionId, List<Message> messageList);

    /**
     * 向WSChatSession中添加assistant回复
     * @param wsSessionId
     * @param message
     * @return
     */
    boolean addReturnToWSChatSessionMessageList(String wsSessionId, String message);

    /**
     * 增加UserRole的Used
     * @param roleId
     * @param userId
     * @param point
     * @return
     */
    int addUserRoleUsed(long roleId, long userId, int point);

    boolean checkUserRole(long roleId, long userId);
}