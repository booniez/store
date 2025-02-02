package com.storehub.store.flow.api.dto;

import com.alibaba.fastjson.JSONObject;
import com.storehub.store.flow.api.dto.json.UserInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author LoveMyOrange
 * @create 2022-10-14 23:27
 */
@Data
@Schema(description = "启动流程实例需要传入的参数")
public class StartProcessInstanceDTO {
    @Schema(description="流程定义id")
    private String processDefinitionId;
    @Schema(description="表单数据")
    private JSONObject formData;
    @Schema(description="发起人自选的信息, key 是nodeId, value是用户信息")
    private Map<String, List<UserInfo>> processUsers;
    @Schema(description="当前人用户信息,(因为本项目没有做登录功能,所以就是直接传递用户信息就行 简单起见)")
    private UserInfo startUserInfo;
}
