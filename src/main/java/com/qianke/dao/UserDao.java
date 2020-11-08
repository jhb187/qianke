/**
 * 
 */
package com.qianke.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.qianke.model.DeliveryMan;
import com.qianke.model.SMS;
import com.qianke.model.User;
//import org.apache.ibatis.annotations.Mapper;
/**
 * 
 * Copyright © 2019 易联云计算有限公司. All rights reserved.
 * @Description: 
 * @author: jin.hui.bin 
 * @date: 
 * @version: V1.0
 */
import com.qianke.model.WebAccessToken;
@Mapper
public interface UserDao {
	
	/**TODO:废弃
	* @Description 查询单个用户(登录)
	* @param
	* @return
	*/
	public User getOne( @Param("openid")String openid);
	
	
	/**    
	* @Description 新增一条微信用户信息
	* @param   User user
	* @return
	*/
	public int insertWxUser(User user);
	
	
	

	/**
	* @Description 根据openid查询用户(手机号码)是否存在
	* @param
	* @return
	*/
	public User getUserByOpenid( @Param("openid")String openid);
	

	/**
	* @Description 根据openid查询用户(手机号码)是否存在
	* @param
	* @return
	*/
	public User getUserByMobile( SMS sms);


	/**
	* @Description 
	* @param
	* @return
	*/
	public List<DeliveryMan> listDeliveryMan(DeliveryMan man);


	/**
	* @Description 
	* @param
	* @return
	*/
	public DeliveryMan getDeliverManById(@Param("deliverman")String deliverMan);


	/**
	* @Description 
	* @param
	* @return
	*/
	public int insertWorker(DeliveryMan worker);


	/**
	* @Description 
	* @param
	* @return
	*/
	public User getWorkerByMobile(SMS sms);


	/**
	* @Description 
	* @param
	* @return
	*/
	public DeliveryMan checkWorkerByOpenid(DeliveryMan worker);


	/**
	* @Description 修改送水工状态
	* @param
	* @return
	*/
	public int changeDeliverManStatus(DeliveryMan worker);
}
