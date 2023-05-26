package com.wbtkj.chat.service;

import com.alibaba.fastjson.JSONObject;
import com.wbtkj.chat.pojo.dto.openai.chat.Message;
import com.wbtkj.chat.pojo.model.ChatSession;
import com.wbtkj.chat.pojo.vo.role.RoleHistoryVO;
import com.wbtkj.chat.pojo.vo.role.RoleInfoVO;

import java.util.List;

public interface RoleService {
    /**
     * 获取用户所属角色
     * @return
     */
    List<JSONObject> getRole();

    /**
     *
     * @param roleInfo
     * @return
     */
    boolean addRole(RoleInfoVO roleInfo);

    /**
     *
     * @param roleInfo
     * @return
     */
    boolean updateRole(RoleInfoVO roleInfo);

    /**
     * 在应用市场通过id添加role
     * @param roleId
     * @return
     */
    boolean addRoleById(long roleId);

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
}
