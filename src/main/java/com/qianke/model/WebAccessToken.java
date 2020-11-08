/**
 * 
 */
package com.qianke.model;

import lombok.Data;

/**
 * 
 * Copyright © 2019 易联云计算有限公司. All rights reserved.
 * 
 * @Description:
 * @author: jin.hui.bin
 * @date:
 * @version: V1.0
 */
@Data
public class WebAccessToken {
	String access_token;
	String expires_in;
	String refresh_token;
	String openid;
	String scope;
    
}
