/**
 * 
 */
package com.qianke.model;

import com.qianke.service.SMSService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import lombok.Data;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 
 * Copyright © 2020 掮客 汉王. All rights reserved.
 * @Description: 租户的短信以及微信支付参数走数据库
 * @author: jin.hui.bin 
 * @date: 
 * @version: V1.0
 */
@Data
@Slf4j
@Component
public class TencentConfig {
    @Value("${tenant_code}")
    String tenant_code;

    @Resource
    SMSService  smsService;

    public String baseurl;
    public String sdkappid;
    public String appKey;
    public String tplid;
	public static String sign="掮客";

	@PostConstruct
	public void getSMSConfigFromDb(){
		log.info("开始从数据库加载租户短信配置...");
		TencentConfigDTO tencentConfig=smsService.getSMSConfig(tenant_code);
		baseurl=tencentConfig.baseurl;
		sdkappid=tencentConfig.sdkappid;
		appKey=tencentConfig.appKey;
		tplid=tencentConfig.tplid;
		log.info("当前租户短信配置，baseurl={},sdkappid={},appKey={},tplid={}",baseurl,sdkappid,appKey,tplid);
	}

}
