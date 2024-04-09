package com.storehub.store.flow.api.dto.json;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @Author:LoveMyOrange
 * @Description:
 * @Date:Created in 2022/10/9 16:15
 */
@Data
public class NotifyTypeInfo {
  private String title;
  private JSONObject types;
}
