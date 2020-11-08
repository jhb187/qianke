/**
 * 
 */
package com.qianke.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qianke.dao.TenantDao;
import com.qianke.model.Tenant;

/**
 * 
 * Copyright © 2019 易联云计算有限公司. All rights reserved.
 * 
 * @Description: 用户服务类
 * @author: jin.hui.bin
 * @date:
 * @version: V1.0
 */
@Service
public class TenantService {

	@Resource
	TenantDao tenantDao;
	
	/**
	* @Description 查找单个用户
	* @param
	* @return
	*/
	public Tenant selectOneByCode(String tenantCode) {
		return tenantDao.selectOneByCode(tenantCode);
	}
	
	

	/**
	* @Description 查找单个用户
	* @param
	* @return
	*/
	public Tenant selectOne(String tenantCode, String pwd) {
		return tenantDao.selectOne(tenantCode, pwd);
	}

	/**
	* @Description 查询所有用户
	* @param
	* @return
	*/
	public List<Tenant> selectAll() {
		return tenantDao.selectAll();
	}
	/**
	* @Description 启用或停用租户
	* @param
	* @return
	*/
	public int switchTenantStaus(Tenant tenant) {
		return tenantDao.switchTenantStaus(tenant);
	}
	
	
	

}
