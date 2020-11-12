/**
 * 
 */
package com.qianke.dao;

import java.util.List;
import com.qianke.model.TencentConfigDTO;
import com.qianke.model.WxConfigDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.qianke.model.Tenant;
import org.springframework.stereotype.Component;
/**
 * 
 * Copyright © 
 * @Description: 
 * @author: jin.hui.bin 
 * @date: 
 * @version: V1.0
 */
@Mapper
@Component
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

	public TencentConfigDTO getSMSConfigByCode(@Param("tenantCode")String tenantCode);

	public WxConfigDTO getWxConfigByCode(@Param("tenantCode")String tenantCode);


}
