/**
 * 
 */
package com.qianke.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qianke.dao.UserDao;
import com.qianke.model.DeliveryMan;
import com.qianke.model.SMS;
import com.qianke.model.User;

/**
 * 
 * Copyright © 2019 易联云计算有限公司. All rights reserved.
 * @Description: 用户服务类
 * @author: jin.hui.bin 
 * @date: 
 * @version: V1.0
 */
@Service
public class UserService {
	
	@Resource
	UserDao userDao;
    
    public User getOne(String openid) {
    	
    	return userDao.getOne(openid);
    	
    }
    
    /**
	 * @Description 插入一条用户信息
	 * @param
	 * @return
	 */
	public int insertWxUser(User user) {
		return 	userDao.insertWxUser(user);
	}
	
	/**
	 * @Description 检查手机号是否存在
	 * @param
	 * @return
	 */
	public User checkMobile(SMS sms) {
		return 	userDao.getUserByMobile(sms);
	}

	/**
	* @Description 
	* @param
	* @return
	*/
	public List<DeliveryMan> listDeliveryMan(DeliveryMan man) {
		return 	userDao.listDeliveryMan(man);
	}

	/**
	* @Description 
	* @param
	* @return
	*/
	public DeliveryMan getDeliverManById(String deliverMan) {
		return 	userDao.getDeliverManById(deliverMan);
	}

	/**
	* @Description  添加送水工
	* @param
	* @return
	*/
	public int insertWorker(DeliveryMan worker) {
		return 	userDao.insertWorker(worker);
	}

	/**
	* @Description 
	* @param
	* @return
	*/
	public User checkMobileWorker(SMS sms) {
		return 	userDao.getWorkerByMobile(sms);
	}

	/**
	* @Description 
	* @param
	* @return
	*/
	public DeliveryMan checkWorkerUnique(DeliveryMan worker) {
		// TODO Auto-generated method stub
		 return userDao.checkWorkerByOpenid(worker);
	}

	

	/**
	* @Description 修改送水工状态
	* @param   status ，openid
	* @return
	*/
	public int deliverManStatusChange(DeliveryMan deliverMan) {
		return userDao.changeDeliverManStatus(deliverMan);
	}
	
	
    
}
