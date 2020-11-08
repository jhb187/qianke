/**
 * 
 */
package com.qianke.service;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qianke.dao.ProductDao;
import com.qianke.model.Category;
import com.qianke.model.DeliveryMan;
import com.qianke.model.MsgTemplate;
import com.qianke.model.Order;
import com.qianke.model.Product;
import com.qianke.model.TemplateKeyWord;
import com.qianke.model.TemplateMsg;
import com.qianke.model.WaterCard;
import com.qianke.util.DateUtil;

/**
 * 
 * Copyright © 2019 易联云计算有限公司. All rights reserved.
 * 
 * @Description:
 * @author: jin.hui.bin
 * @date:
 * @version: V1.0
 */
@Service
public class ProductService {
	@Resource
	ProductDao productDao;

	@Resource
	WxService wxService;

	@Resource
	WXPayService wxPayService;

	@Resource
	UserService userService;

	public int addCategory(Category category) {
		return productDao.addCategory(category);

	}

	public List<Product> listCardWaterByOpenid(String openId) {
		return productDao.listCardWaterByOpenid(openId);
	}
	
	public List<Product> listProduct(Product product) {
		return productDao.listProduct(product);
	}

	public int creatOrder(Order order) {
		return productDao.creatOrder(order);
	}

	/**
	 * @Description 查询订单
	 * @param
	 * @return
	 */
	public List<Order> searchOrder(Order order) {
		return productDao.getOrderByParam(order);
	}

	/**
	 * @Description
	 * @param
	 * @return
	 */
	public Product getProductById(String id) {
		return productDao.getProductById(id);

	}

	/**
	 * @Description 将订单信息推送给该送水工，(工人确认才修改)修改订单状态；
	 * @param
	 * @return
	 */
	public void pointOrder(Order order) {
		// 向送水工发送配送消息；
		String deliverman = order.getDeliveryman();
		TemplateMsg templateMsg = new TemplateMsg();
		templateMsg.setTouser(deliverman);//
		templateMsg.setTemplate_id(MsgTemplate.ORDER_RECEIVING.getTemplate_id());
		order = productDao.getOrderInfoById(order.getOrderid());
		templateMsg.setUrl("http://qianke668.club:8090/receive-order.html?orderid=" + order.getOrderid() + "&openid="
				+ order.getOpenid() + "&deliverman=" + deliverman + "&status=" + order.getStatus());// 确认接单的页面；orderid传入，在这个页面修改状态；
		HashMap<String, String> param = new HashMap<String, String>();

		String productName = wxPayService.getInfoByOrderId(order.getOrderid());

		param.put("productName", productName);
		param.put("recipients", order.getContact());
		param.put("receiverMobile", order.getMobile());
		param.put("receiverAddress", order.getDelivery_address());
		param.put("orderTime", order.getCreate_time());
		MsgTemplate.ORDER_RECEIVING.orderReceivingData(param);// 记得调用对应的data方法,传入构造的param
		wxService.sendTemplateMsg(templateMsg, MsgTemplate.ORDER_RECEIVING.getData());

	}

	/**
	 * @Description 修改订单状态，给买家发模板消息，修改送水工状态
	 * @param
	 * @return
	 */
	public String confirmDeliver(Order order) {

		productDao.confirmDeliver(order);

		TemplateMsg templateMsg = new TemplateMsg();
		templateMsg.setTouser(order.getOpenid());//
		templateMsg.setTemplate_id(MsgTemplate.START_DELIVERY.getTemplate_id());

		HashMap<String, String> param = new HashMap<String, String>();
		// orderid openid order.deliverman

		DeliveryMan worker = userService.getDeliverManById(order.getDeliveryman());

		param.put("deliverTime", DateUtil.nextTime());// 当前时间加1个小时，后续改变；
		param.put("deliverName", worker.getName());//
		param.put("deliverMobile", worker.getMobile());
		param.put("remark", "请耐心等待");
		MsgTemplate.START_DELIVERY.startDeliveryData(param);// 记得调用对应的data方法,传入构造的param
		wxService.sendTemplateMsg(templateMsg, MsgTemplate.START_DELIVERY.getData());

		// 修改送水工状态为配送中
		DeliveryMan deliverMan = new DeliveryMan();
		deliverMan.setOpenid(order.getDeliveryman());
		deliverMan.setStatus(1);
		userService.deliverManStatusChange(deliverMan);
		return "success";

	}

	/**
	 * @Description 完成订单
	 * @param
	 * @return
	 */
	public void finishOrder(Order order) {
		productDao.confirmDeliver(order);

		// 给买家发送订单完成模板消息；
		TemplateMsg templateMsg = new TemplateMsg();
		templateMsg.setTouser(order.getOpenid());//
		templateMsg.setTemplate_id(MsgTemplate.ORDER_FINISHED.getTemplate_id());

		HashMap<String, String> param = new HashMap<String, String>();
		// orderid openid
		order = productDao.getOrderInfoById(order.getOrderid());
		String productName = wxPayService.getInfoByOrderId(order.getOrderid());

		param.put("orderId", order.getOrderid());//
		param.put("productName", productName);//
		param.put("orderTime", order.getCreate_time());// 下单时间；
		MsgTemplate.ORDER_FINISHED.orderFinishedData(param);// 记得调用对应的data方法,传入构造的param
		wxService.sendTemplateMsg(templateMsg, MsgTemplate.ORDER_FINISHED.getData());

		// 修改送水工状态为空闲
		DeliveryMan deliverMan = new DeliveryMan();
		deliverMan.setOpenid(order.getDeliveryman());
		deliverMan.setStatus(0);
		userService.deliverManStatusChange(deliverMan);

	}

	/**
	 * @Description
	 * @param
	 * @return
	 */
	public Order getOrderStatus(Order order) {

		return productDao.getOrderInfoById(order.getOrderid());
	}

	/**
	 * @Description
	 * @param
	 * @return
	 */
	public List<WaterCard> listWaterCard(WaterCard watercard) {
		return productDao.listWaterCard(watercard);
	}

}
