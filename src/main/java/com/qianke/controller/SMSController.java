/**
 * 
 */
package com.qianke.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qianke.controller.base.BaseResponse;
import com.qianke.model.SMS;
import com.qianke.service.SMSService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Copyright © 2019 易联云计算有限公司. All rights reserved.
 * 
 * @Description:
 * @author: jin.hui.bin
 * @date:
 * @version: V1.0
 */
@RestController
@RequestMapping("sms")
@Slf4j
@CrossOrigin //解决跨域问题添加这个注解
public class SMSController {
	
	@Resource
	SMSService smsService;
	//****必须将util也注入，才会获取properties配置文件中的属性*****/
	
	
	/**
	* @Description 发送短信验证码
	* @param mobile handle
	* @return 尽量在返回的body中封装一个map，便于前端获取数据
	*/
	@RequestMapping(value = "/send")
	public BaseResponse<Map<String,Object>> sms(@RequestBody SMS sms) {
		log.info("短信接口开始调用:"+sms.toString());
		Map<String,Object> resMap=smsService.send(sms.getMobile(), sms.getHandle());
		BaseResponse<Map<String,Object>> response=new BaseResponse<Map<String,Object>>(resMap);
		log.info("短信接口返回"+response);
		return response ;
	}
	
	
	/**
	* @Description 验证码校验
	* @param mobile handle
	* @return
	*/
	@RequestMapping(value = "/check")
	public BaseResponse<String> check(@RequestBody SMS sms) {
		log.info("check校验接口开始调用:"+sms.toString());
		String checkResult=smsService.check(sms.getMobile(), sms.getCode());
		BaseResponse<String> response=new BaseResponse<String>(checkResult);
		log.info("check校验接口返回:"+response);
		return response ;
	}
	

}
