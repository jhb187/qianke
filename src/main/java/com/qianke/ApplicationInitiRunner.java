/**
 * 
 */
package com.qianke;

import javax.annotation.Resource;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.qianke.service.WxService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Copyright © 2019 易联云计算有限公司. All rights reserved.
 * @Description: 
 * @author: jin.hui.bin 
 * @date: 
 * @version: V1.0
 */
@Component
@Slf4j
public class ApplicationInitiRunner implements ApplicationRunner {
	@Resource
	WxService wxService;
	/**
	* @Description 
	* @param
	* @return
	*/
	@Override
	public void run(ApplicationArguments args) throws Exception {
		//调用获取access_token的方法，存入缓存
		log.info("初始化access_token开始执行");
		wxService.getAccessToken();
		wxService.getJsapiTicket();
		
	}

}
