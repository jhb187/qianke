/**
 * 
 */
package com.qianke.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qianke.controller.base.BaseResponse;
import com.qianke.controller.base.RespCode;
import com.qianke.controller.base.RespHead;
import com.qianke.model.Tenant;
import com.qianke.service.TenantService;

/**
 * 
 * Copyright © 
 * 
 * @Description:
 * @author: jin.hui.bin
 * @date:
 * @version: V1.0
 */
@RestController
@RequestMapping("tenant")
@CrossOrigin
public class TenantController {
	
	@Resource
	TenantService tenantService;
	
	/**
	* @Description 商户登录
	* @param      //TODO:增加对商户状态的判断，status为1才允许登录
	* @return
	*/
	@RequestMapping(value = "/login")
	public BaseResponse<Tenant> login(@RequestBody Tenant tenant) {
		BaseResponse<Tenant> resp=new BaseResponse<Tenant>();
		tenant=tenantService.selectOne(tenant.getTenantCode(), tenant.getPwd());
		
		//TODO:与浏览器建立websocket连接
		if(tenant!=null) {
			resp.setBody(tenant);
		}else {
			resp.setHead(new RespHead(RespCode.RESP_CODE_000001));
		}
		return resp;
				
	}
	
	
	/**
	* @Description  展示所有租户（商户）
	* @param
	* @return
	*/
	@RequestMapping(value = "/listAll")
	public BaseResponse<List<Tenant>> listAllTenant() {
		BaseResponse<List<Tenant>> resp=new BaseResponse<List<Tenant>>();
		List<Tenant> tenantList = tenantService.selectAll();
		if(tenantList!=null ) {
			resp.setBody(tenantList);
		}else {
			resp.setHead(new RespHead(RespCode.RESP_CODE_000001));
		}
		return resp;
				
	}
	
	/**
	* @Description 改变商户状态，用于停用或启用商户
	* @param tenantCode status
	* @return
	*/
	@RequestMapping(value = "/switch")
	public BaseResponse<String> switchTenantStaus(@RequestBody Tenant tenant) {
		BaseResponse<String> resp=new BaseResponse<String>();
		int result= tenantService.switchTenantStaus(tenant);
		resp.setBody(result+"条记录操作成功");
		return resp;
				
	}
	

}
