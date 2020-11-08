/**
 * 
 */
package com.qianke.model;

import lombok.Data;

/**
 * 
 * Copyright 
 * @Description: 
 * @author: jin.hui.bin 
 * @date: 
 * @version: V1.0
 */
@Data
public class Tenant {
    String tenantId;
    String tenantCode;
    String pwd;
    String tenantName;
    String mobile;
    String address;
    String status;
    String remark;
    String role;
    String mch_id;
    
    String auto_reply;
    String follow_reply;
    
    
}
