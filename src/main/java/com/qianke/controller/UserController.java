/**
 * 
 */
package com.qianke.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qianke.controller.base.BaseResponse;
import com.qianke.controller.base.RespCode;
import com.qianke.controller.base.RespHead;
import com.qianke.exception.BizException;
import com.qianke.model.DeliveryMan;
import com.qianke.model.Product;
import com.qianke.model.SMS;
import com.qianke.model.User;
import com.qianke.service.ProductService;
import com.qianke.service.UserService;

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
@RequestMapping("user")
@Slf4j
@CrossOrigin //解决跨域问题添加这个注解
public class UserController {
	
	@Resource
	UserService userService;
	
	@Resource
	ProductService productService;
	//****必须将util也注入，才会获取properties配置文件中的属性*****/
	
	/**
	 * 
	 * @Description  获取用户信息，
	 * @param   openid
	 * @return  name,address,mobile
	 * @throws BizException 
	 * @throws Exception
	 */
	@RequestMapping(value = "/login")
	public BaseResponse<User> login(@RequestBody User user) {
		BaseResponse<User> resp=new BaseResponse<User>();
		user=userService.getOne(user.getOpenid());
		if(user!=null) {
			resp.setBody(user);
		}else {
			resp.setHead(new RespHead(RespCode.RESP_CODE_000001));
		}
		return resp;
				
	}
	
	
	/**
	 * 
	 * @Description  获取用户水卡信息，
	 * @param   openid
	 * @return  cardid,cardname,waterCount
	 * @throws BizException 
	 * @throws Exception
	 */
	@RequestMapping(value = "/cardinfo")
	public BaseResponse<List<Product>> cardinfo(@RequestBody User user) {
		BaseResponse<List<Product>> resp=new BaseResponse<List<Product>>();
		List<Product> cardList=productService.listCardWaterByOpenid(user.getOpenid());
		if(cardList!=null && cardList.size()>0) {
			resp.setBody(cardList);
		}else {
			resp.setHead(new RespHead(RespCode.RESP_CODE_000002));
		}
		return resp;
				
	}
	
	/**
	* @Description  用户注册
	* @param
	* @return
	*/
	@RequestMapping(value = "/register")
	public BaseResponse<String> register(@RequestBody User user) {
		log.info("register接口调用 开始，参数:"+user);
		int result=userService.insertWxUser(user);
		log.info("register接口返回，result:"+result);

		if(result!=1) {
			return new BaseResponse<String>("0");
		}else {
			return new BaseResponse<String>("1");
		}
		
				
	}
	
	
	
	
	/**
	* @Description  员工扫码注册
	* @param
	* @return
	*/
	@RequestMapping(value = "/addworker")
	public BaseResponse<String> addworker(@RequestBody DeliveryMan worker) {
		log.info("addworker接口调用 开始，参数:"+worker);
		int result=userService.insertWorker(worker);
		log.info("addworker接口返回，result:"+result);

		if(result!=1) {
			return new BaseResponse<String>("0");
		}else {
			return new BaseResponse<String>("1");
		}
		
				
	}
	/**
	* @Description  检查手机号唯一性
	* @param
	* @return
	*/
	@RequestMapping(value = "/mobilecheck")
	public BaseResponse<String> checkMobile(@RequestBody SMS sms) {
		log.info("mobilecheck接口调用 开始，参数:"+sms);
		User user=userService.checkMobile(sms);
		log.info("mobilecheck接口返回，user:"+user);

		if(user==null) {
			return new BaseResponse<String>("0");
		}else {
			return new BaseResponse<String>("1");
		}
		
				
	}
	
	
	
	/**
	* @Description  检查员工openid
	* @param
	* @return
	*/
	@RequestMapping(value = "/checkworkerreg")
	public BaseResponse<String> checkworkerreg(@RequestBody DeliveryMan worker) {
		log.info("checkworkerreg接口调用 开始，参数:"+worker);
		DeliveryMan users=userService.checkWorkerUnique(worker);
		log.info("mobilecheck接口返回，user:"+users);

		if(users==null) {
			return new BaseResponse<String>("0");
		}else {
			return new BaseResponse<String>("1");
		}
		
				
	}
	
	/**
	* @Description  检查员工手机号唯一性
	* @param
	* @return
	*/
	@RequestMapping(value = "/mobilecheck/worker")
	public BaseResponse<String> checkMobileworker(@RequestBody SMS sms) {
		log.info("mobilecheckworker接口调用 开始，参数:"+sms);
		User user=userService.checkMobileWorker(sms);
		log.info("mobilecheck接口返回，user:"+user);

		if(user==null) {
			return new BaseResponse<String>("0");
		}else {
			return new BaseResponse<String>("1");
		}
		
				
	}
	/**
	* @Description  展示全部配送员
	* @param
	* @return
	*/
	@RequestMapping(value = "/list/deliveryman")
	public BaseResponse<List<DeliveryMan>> listDeliverman(@RequestBody DeliveryMan man) {
		log.info("【list/deliveryman】接口调用 开始，参数:"+man);
		List<DeliveryMan> deliverManList=userService.listDeliveryMan(man);
		BaseResponse<List<DeliveryMan>> result=new BaseResponse<List<DeliveryMan>>(deliverManList);
		return result;
	}
	

}
