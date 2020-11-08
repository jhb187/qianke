/**
 * 
 */
package com.qianke.model;

import lombok.Data;

/**
 * 
 * Copyright © 2019 . All rights reserved.
 * @Description: 配送员实体类 
 * @author: jin.hui.bin 
 * @date: 
 * @version: V1.0
 */
@Data
public class DeliveryMan {
	String openid;//微信openid
    String mobile;
    String name;//昵称
    String tenant_code;
    int status;
}
