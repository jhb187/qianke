/**
 * 
 */
package com.qianke.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
/**
 * 
 * Copyright © 2020 掮客 汉王. All rights reserved.
 * @Description: 
 * @author: jin.hui.bin 
 * @date: 
 * @version: V1.0
 */
@Data
@Slf4j
@Component  //租户的短信以及微信支付参数走数据库
public class TencentConfigDTO {
	String baseurl;
	String sdkappid;
	String appKey;
	String tplid;
}
