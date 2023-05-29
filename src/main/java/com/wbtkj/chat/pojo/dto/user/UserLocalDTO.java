package com.wbtkj.chat.pojo.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLocalDTO {
    private Long id;
    private String email;
}
