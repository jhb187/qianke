/**
 * 
 */
package com.qianke.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.qianke.model.Tenant;
/**
 * 
 * Copyright © 
 * @Description: 
 * @author: jin.hui.bin 
 * @date: 
 * @version: V1.0
 */
@Mapper
public interface TenantDao {
	
	public Tenant selectOne( @Param("tenantCode")String tenantCode, @Param("pwd")String pwd);
	
	public List<Tenant> selectAll();
	public int switchTenantStaus(Tenant tanant);

	/**
	* @Description 
	* @param
	* @return
	*/
	public Tenant selectOneByCode(@Param("tenantCode")String tenantCode);
}
