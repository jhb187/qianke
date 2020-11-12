/**
 * 
 */
package com.qianke.model;

import lombok.Data;
import org.springframework.stereotype.Component;
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
@Component
public class WxConfigDTO {
	String token;
	String AppID;
	String AppSecret;
	String baseUrl;
	String MchId;
	String NotifyUrl;

}
