/**
 * 
 */
package com.qianke.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * Copyright © . All rights reserved.
 * 
 * @Description:
 * @author: jin.hui.bin
 * @date:
 * @version: V1.0
 */
public enum MsgTemplate {

	// ***consumer
	/** 下单成功 */ 
	ORDER_SUCCESSFULLY("0fF4_cI7ulpGkrPIxWt8cpL14ClBXJAKnCEEgIEQA40"),
	

	/** 开始配送提醒 */ 
	START_DELIVERY("5XU2JvqkBHhVK3l7wCyrQDBnvNacLiuN5W-hGDDhh0w"),
	
	/****接单成功提醒***/
	ORDER_RECEIVING("RX1TTBFXBveN-VfmKlsylUO023met7dD4wueZgrWwp8"),
	
	/****订单完成通知***/
    ORDER_FINISHED("2NdbMWh8TEN3EjCglgLgiaA-c2EnOJUh1gFhgtIjvac");
	
	
	
	private HashMap<String, TemplateKeyWord> data ;
	private String template_id;

	// static HashMap<String, TemplateKeyWord> data=null;
	
	
	
	/**
	 * @Description 下单成功data
	 * @param
	 * @return
	 */
	public  void orderSuccessData(Map<String, String> param) {
		HashMap<String, TemplateKeyWord> orderSuccessfullydata = new HashMap<String, TemplateKeyWord>();
		orderSuccessfullydata.put("first", new TemplateKeyWord("您好，您已下单成功。"));
		orderSuccessfullydata.put("keyword1", new TemplateKeyWord("好水上府"));
		orderSuccessfullydata.put("keyword2", new TemplateKeyWord(param.get("time")));// time
		orderSuccessfullydata.put("keyword3", new TemplateKeyWord(param.get("info")));// info
		orderSuccessfullydata.put("keyword4", new TemplateKeyWord(param.get("money")));// money 传参数构造
		orderSuccessfullydata.put("remark", new TemplateKeyWord(param.get("remark")));
		data = orderSuccessfullydata;
	}
    
	
	/**
	 * @Description 接单成功data
	 * @param
	 * @return
	 */
	public  void orderReceivingData(Map<String, String> param) {
		HashMap<String, TemplateKeyWord> orderSuccessfullydata = new HashMap<String, TemplateKeyWord>();
		orderSuccessfullydata.put("first", new TemplateKeyWord("您已接单成功，以下是订单派送信息"));
		orderSuccessfullydata.put("keyword1", new TemplateKeyWord(param.get("productName")));
		orderSuccessfullydata.put("keyword2", new TemplateKeyWord(param.get("recipients")));// time
		orderSuccessfullydata.put("keyword3", new TemplateKeyWord(param.get("receiverMobile")));// time
		orderSuccessfullydata.put("keyword4", new TemplateKeyWord(param.get("receiverAddress")));// time
		orderSuccessfullydata.put("keyword5", new TemplateKeyWord(param.get("orderTime")));// time
		orderSuccessfullydata.put("remark", new TemplateKeyWord("请尽快处理"));
		data = orderSuccessfullydata;
	}
	
	
	
	
	/**
	 * @Description 开始配送提醒data
	 * @param
	 * @return
	 */
	public  void startDeliveryData(Map<String, String> param) {
		HashMap<String, TemplateKeyWord> orderSuccessfullydata = new HashMap<String, TemplateKeyWord>();
		orderSuccessfullydata.put("first", new TemplateKeyWord("你好，你的订单已经开始配送，预计于"+param.get("deliverTime")+"之前送达，请注意查收，如没在指定时间内到达，请联系配送人员。"));
		orderSuccessfullydata.put("keyword1", new TemplateKeyWord(param.get("deliverName")));
		orderSuccessfullydata.put("keyword2", new TemplateKeyWord(param.get("deliverMobile")));// time
		orderSuccessfullydata.put("remark", new TemplateKeyWord(param.get("remark")));
		data = orderSuccessfullydata;
	}
	
	
	
	/**
	 * @Description 订单完成data
	 * @param
	 * @return
	 */
	public  void orderFinishedData(Map<String, String> param) {
		HashMap<String, TemplateKeyWord> orderSuccessfullydata = new HashMap<String, TemplateKeyWord>();
		orderSuccessfullydata.put("first", new TemplateKeyWord("您预约的订单已完成。"));
		orderSuccessfullydata.put("keyword1", new TemplateKeyWord(param.get("orderId")));
		orderSuccessfullydata.put("keyword2", new TemplateKeyWord(param.get("productName")));// time
		orderSuccessfullydata.put("keyword3", new TemplateKeyWord(param.get("orderTime")));// info
		orderSuccessfullydata.put("remark", new TemplateKeyWord("如有问题请联系我们的客服电话:0358-7850529"));
		data = orderSuccessfullydata;
	}
	
	

	private MsgTemplate(String template_id) {
		this.template_id = template_id;
	}



	public String getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

	public HashMap<String, TemplateKeyWord> getData() {
		return data;
	}

	public void setData(HashMap<String, TemplateKeyWord> data) {
		this.data = data;
	}



}
