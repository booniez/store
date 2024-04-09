package com.storehub.store.flow.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author LoveMyOrange
 * @create 2022-10-15 10:33
 */
@Data
@Schema(description = "分页DTO")
public class PageDTO {
    @Schema(description = "第几页",requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer pageNo;
    @Schema(description = "多少条",requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer pageSize;
}
