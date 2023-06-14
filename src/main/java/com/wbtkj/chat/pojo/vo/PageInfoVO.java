package com.wbtkj.chat.pojo.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageInfoVO {
    private int totalPage;
    private int totalCount;
}
