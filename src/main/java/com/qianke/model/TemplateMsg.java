/**
 * 
 */
package com.qianke.model;

import java.util.HashMap;

import lombok.Data;

/**
 * 
 * Copyright ©  All rights reserved.
 * @Description: 模板消息
 * @author: jin.hui.bin 
 * @date: 
 * @version: V1.0
 */
@Data
public class TemplateMsg {
   String touser;
   String template_id;
   HashMap<String,TemplateKeyWord> data;
   String url;
}
