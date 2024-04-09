package com.storehub.store.flow.api.dto;

import com.storehub.store.flow.api.dto.json.UserInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author LoveMyOrange
 * @create 2022-10-14 23:47
 */
@Data
@Schema(description = "待办 需要返回给前端的VO")
public class TaskDTO extends PageDTO {
    private UserInfo currentUserInfo;
}
