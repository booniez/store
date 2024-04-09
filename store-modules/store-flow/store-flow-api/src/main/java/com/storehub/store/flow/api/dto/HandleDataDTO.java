package com.storehub.store.flow.api.dto;

import com.storehub.store.flow.api.dto.json.UserInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author LoveMyOrange
 * @create 2022-10-15 16:27
 */
@Data
@Schema(description = "各个按钮 处理数据需要传递的参数")
public class HandleDataDTO {
    @Schema(description="任务id")
    private String taskId;
    @Schema(description="流程实例id")
    private String processInstanceId;
    @Schema(description="表单数据")
    private JSONObject formData;
    @Schema(description="附件")
    private List<AttachmentDTO> attachments;
    @Schema(description="意见")
    private String comments;
    @Schema(description="签名信息")
    private String signInfo;
    @Schema(description="转办用户信息")
    private UserInfo transferUserInfo;
    @Schema(description="加签用户信息")
    private UserInfo multiAddUserInfo;
    @Schema(description="退回节点id")
    private String rollbackId;
    @Schema(description="当前用户信息")
    private UserInfo currentUserInfo;

    @Schema(description="委派的人")
    private UserInfo delegateUserInfo;
}
