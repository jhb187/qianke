/**
 * 
 */
package com.qianke.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qianke.controller.base.BaseResponse;
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
/**
 * 
 * Copyright © 2019 All rights reserved.
 * 
 * @Description: 掮客超级管理员管理商户的控制器
 * @author: jin.hui.bin
 * @date:
 * @version: V1.0
 */
@Slf4j
@RestController
@RequestMapping("admin")
public class AdminController {
    
	@Resource
	WxService wxService;
	
	
	/**TODO:后续做 目前为手动通过postman请求   用超级管理员请求该接口
	 * @Description 设置商户微信服务号的自定义菜单
	 * @param { "json":"{ "button":[ { "type":"click", "name":"今日歌曲",
	 *        "key":"V1001_TODAY_MUSIC" }, {" }
	 * @return
	 */
	@RequestMapping(value = "/wxmenu/set")
	public BaseResponse<String> switchTenantStaus(@RequestBody String json) {
		log.info("设置微信界面菜单接口调用开始"+json);
		BaseResponse<String> resp = new BaseResponse<String>();
		wxService.setWxConsumerMenu(json);
		return resp;

	}

}
