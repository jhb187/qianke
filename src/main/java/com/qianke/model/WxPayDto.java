/**
 * 
 */
package com.qianke.model;

import lombok.Data;

/**
 * 
 * Copyright © 2019 易联云计算有限公司. All rights reserved.
 * @Description: 
 * @author: jin.hui.bin 
 * @date: 
 * @version: V1.0
 */
@Data
public class WxPayDto {
	String orderId;
	String orderName;
	double money;
	String openid;
	
}
