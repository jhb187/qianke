/**
 * 
 */
package com.qianke.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

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
@Component
@ConfigurationProperties(prefix = "tencent.sms")
public class TencentConfig {
	
	String baseurl;
	String sdkappid;
	String appKey;
	String tplid;
	public static String sign="掮客";
	
	
	
    
}
