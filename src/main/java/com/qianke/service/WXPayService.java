/**
 * 
 */
package com.qianke.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import com.qianke.dao.ProductDao;
import com.qianke.model.CardBank;
import com.qianke.model.MsgTemplate;
import com.qianke.model.Order;
import com.qianke.model.Product;
import com.qianke.model.TemplateMsg;
import com.qianke.model.WaterCard;
import com.qianke.model.WxConfig;
import com.qianke.model.WxPayDto;
import com.qianke.util.DateUtil;
import com.qianke.util.JsonUtil;
import com.qianke.util.WxUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Copyright © 2019 . All rights reserved.
 * 
 * @Description:
 * @author: jin.hui.bin
 * @date:
 * @version: V1.0
 */
@Slf4j
@Service
public class WXPayService {

	WxPayConfig wxPayConfig;

	BestPayServiceImpl bestPayService;
	@Resource
	WxConfig wxConfig;
	
	@Resource
	WxService wxService;
	
	@Resource
	WebSocketService wsService;
	
	@Resource
	ProductDao productDao;
	
	@PostConstruct
	public void init() {
		// 微信支付配置
		wxPayConfig = new WxPayConfig();
		wxPayConfig.setAppId(wxConfig.getAppID()); // 公众号Id

		// 支付商户资料
		wxPayConfig.setMchId(wxConfig.getMchId());
		wxPayConfig.setMchKey(wxConfig.getAppSecret());
		wxPayConfig.setNotifyUrl(wxConfig.getNotifyUrl());
	}

	public PayResponse wxpay(WxPayDto payParam) {
		bestPayService = new BestPayServiceImpl();
		bestPayService.setWxPayConfig(wxPayConfig);
		PayRequest payRequest = new PayRequest();
		payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_MP);
		payRequest.setOrderId(payParam.getOrderId());
		payRequest.setOrderName(payParam.getOrderName());
		payRequest.setOrderAmount(payParam.getMoney());
		payRequest.setOpenid(payParam.getOpenid());
		return bestPayService.pay(payRequest);
	}

	
	/**
	* @Description  根据orderId与金额修改订单状态为已支付1
	* @param
	* @return
	*/
	public int payOrder(String orderId,String money) {
		return productDao.payOrder(orderId, money);
	}
	
	
	/**
	* @Description  根据orderId查询商品信息,一行行格式化栏目信息
	* @param
	* @return
	*/
	public String getInfoByOrderId(String orderId) {
		log.info("【orderId：】"+orderId);
		Order order= productDao.getOrderInfoById(orderId);
		log.info("【order：】"+order);
		String info=order.getProduct_info();
		log.info("【productInfo：】"+info);
		
		List<Product> list  =JSON.parseArray(info, Product.class); 
		
		String infos="";
		for(Product product:list) {
			Product products=productDao.getProductById(product.getId());
			infos=infos+products.getName()+"  ×  "+product.getCount()+"\n";
		}
		 return infos;
	}
	
	
	
	/**
	* @Description  根据orderId查询商品信息,一行行格式化栏目信息
	* @param
	* @return
	*/
	public String getInfoByCardId(String orderId) {
		log.info("【CardId：】"+orderId);
		Order order= productDao.getOrderInfoById(orderId);
		log.info("【Card：】"+order);
		String info=order.getProduct_info();
		log.info("【productInfo：】"+info);
		
		List<WaterCard> list  =JSON.parseArray(info, WaterCard.class); 
		//TODO:将list遍历插入 card-bank表
		String infos="";
		int tail=1;
		for(WaterCard card:list) {
			WaterCard cards=productDao.getCardById(card.getId());
			//TODO:如果该用户有该类型的卡，则不new，直接在水桶水上加桶数
			CardBank bankParam= new CardBank();
			bankParam.setOpenid(order.getOpenid());
			bankParam.setType(card.getId());
			CardBank cardResult=productDao.getCardByParam(bankParam);
			if(cardResult!=null) {
				//在该条上的pailcount增加watercard的pailcount
				cardResult.setPailcount(cards.getPailcount()*card.getCount()+cardResult.getPailcount());//
				int addResult=productDao.addCardBankPailCount(cardResult);
				log.info("【水卡水桶数增加成功】入参:{} ,结果{}",cardResult,addResult);
			}else {
				//new 新水卡；
				CardBank bank= new CardBank();
				bank.setContact(order.getContact());
				bank.setMobile(order.getMobile());
				bank.setOpenid(order.getOpenid());
				bank.setId(orderId.substring(8)+tail);
				bank.setPailcount(cards.getPailcount()*card.getCount());
				bank.setType(cards.getId());
				bank.setCreate_time(DateUtil.now());
				int result=productDao.creatCardBank(bank);
				log.info("【cardBnak插入成功】{},{}",bank,result);
			}
			infos=infos+cards.getName()+"  ×  "+card.getCount()+"\n";
			tail++;
		}
		 return infos;
	}
	
	
	/**
	* @Description  微信支付异步回调，用户支付成功之后处理订单；
	* @param
	* @return
	*/
	public String asyncNotify(HttpServletRequest request) throws IOException {
		InputStream inStream = request.getInputStream();
		int _buffer_size = 1024;
		if (inStream != null) {
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] tempBytes = new byte[_buffer_size];
			int count = -1;
			while ((count = inStream.read(tempBytes, 0, _buffer_size)) != -1) {
				outStream.write(tempBytes, 0, count);
			}
			tempBytes = null;
			outStream.flush();
			// 将流转换成字符串
			String result = new String(outStream.toByteArray(), "UTF-8");
			log.info("【微信回调返回】" + result);
			//TODO:对返回结果进行校验，签名校验，金额校验
			PayResponse wxresult = bestPayService.asyncNotify(result);
			Map<String, String> wxMap=WxUtil.xmlToMap(result);
			log.info("【WxUtil.xmlToMap--XML转换的map】" + wxMap);
			String tradeNo=wxMap.get("out_trade_no");
			if(tradeNo.startsWith("C")) {
				//处理水卡购买成功的逻辑
			    //TODO:查询商品信息的json，转为WaterCard列表，存入card——bank中；
				log.info("【水卡购买付款成功】");
				///查询商品信息的json，转为WaterCard列表，存入card——bank中；
				//向用户发送模板消息；
				TemplateMsg templateMsg = new TemplateMsg();
				templateMsg.setTouser(wxMap.get("openid"));//TODO: touser从回执当中获取，或者用orderid查出来；
				templateMsg.setTemplate_id(MsgTemplate.ORDER_SUCCESSFULLY.getTemplate_id());
				HashMap<String, String> param = new HashMap<String, String>();
				param.put("time",DateUtil.now() );
				param.put("info", getInfoByCardId(wxresult.getOrderId()));//查询商品信息的json，转为WaterCard列表，存入card——bank中；
				param.put("money",wxresult.getOrderAmount().toString() +" 元");
				param.put("remark", "");
				MsgTemplate.ORDER_SUCCESSFULLY.orderSuccessData(param);// 记得调用对应的data方法,传入构造的param
				wxService.sendTemplateMsg(templateMsg, MsgTemplate.ORDER_SUCCESSFULLY.getData());
				
				
				
			}else {
				int orderrResult=payOrder(wxresult.getOrderId(),wxresult.getOrderAmount().toString());
				
				if(orderrResult==1) {
					//语音通知商户，让商户去查询订单表中，状态为1（未配送）的订单	
					wsService.sendWsMsg("newOrder", wxMap.get("mch_id"));
					
					//向用户发送模板消息；
					TemplateMsg templateMsg = new TemplateMsg();
					templateMsg.setTouser(wxMap.get("openid"));//TODO: touser从回执当中获取，或者用orderid查出来；
					templateMsg.setTemplate_id(MsgTemplate.ORDER_SUCCESSFULLY.getTemplate_id());
					HashMap<String, String> param = new HashMap<String, String>();
					param.put("time",DateUtil.now() );
					param.put("info", getInfoByOrderId(wxresult.getOrderId()));
					param.put("money",wxresult.getOrderAmount().toString() +" 元");
					param.put("remark", "");
					MsgTemplate.ORDER_SUCCESSFULLY.orderSuccessData(param);// 记得调用对应的data方法,传入构造的param
					wxService.sendTemplateMsg(templateMsg, MsgTemplate.ORDER_SUCCESSFULLY.getData());
					log.info("【orderrResult发送了模版消息通知了商户】"+orderrResult);
				}
			}
			
			
			log.info("【框架回调返回】" + wxresult);
			return "<xml>\n" + 
					"\n" + 
					"  <return_code><![CDATA[SUCCESS]]></return_code>\n" + 
					"  <return_msg><![CDATA[OK]]></return_msg>\n" + 
					"</xml>";
		}
		return "fail";
	}

	
}
