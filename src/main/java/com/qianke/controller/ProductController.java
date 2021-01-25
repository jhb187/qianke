/**
 * 
 */
package com.qianke.controller;

import com.alibaba.fastjson.JSON;
import com.qianke.controller.base.BaseResponse;
import com.qianke.model.*;
import com.qianke.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * 
 * Copyright © 2019 . All rights reserved.
 * @Description: 订单与商品放在同一个controller  dao  service中（防止类爆炸）
 * 
 * @author: jin.hui.bin 
 * @date: 
 * @version: V1.0
 */
@RestController
@RequestMapping("product")
@Slf4j
@CrossOrigin
public class ProductController {
	@Resource
	ProductService  productService;
	
	/**
	 * @Description 增加新种类
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/category/add")
	public BaseResponse<String> addCategory(@RequestBody Category category) {
		log.info("【category/add】接口调用开始"+category);
		int result=productService.addCategory(category);
		if(result==1) {
			return new BaseResponse<String>("1");
		}else {
			return new BaseResponse<String>("0");

		}
		
	}
	
	/**
	 * @Description 查询所有种类
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/category/list")
	public BaseResponse<String> listCategory(@RequestBody Category category) {
		log.info("【/category/list】接口调用开始"+category);
		int result=productService.addCategory(category);
		if(result==1) {
			return new BaseResponse<String>("1");
		}else {
			return new BaseResponse<String>("0");

		}
		
	}
	
	

	/**
	 * @Description 查询所有商品列表
	 * @param  tenantCode，category
	 * @return  
	 */
	@RequestMapping(value = "/list")
	public BaseResponse<List<Product>> listProduct(@RequestBody Product product) {
		log.info("【/product/list】接口调用开始"+product);
		List<Product> productList=productService.listProduct(product);
		BaseResponse<List<Product>> result=new BaseResponse<List<Product>>(productList);
		return result;
	}



	/**
	 * @Description 用水卡数抵扣当前金额
	 * @param  tenantCode，category
	 * @return
	 */
	@RequestMapping(value = "/cardCalcu")
	public BaseResponse<Integer> listProduct(@RequestBody HashMap<String,String> product) {
		log.info("【/product/cardCalcu】接口调用开始"+product);
		int productId=Integer.parseInt(product.get("productId"));
		String openId=product.get("openId");
		int count=Integer.parseInt(product.get("count"));
		CardBank cardBank=productService.getCardCountByOpenid(openId,productId);
		if(cardBank!=null){

			if(cardBank.getPailcount()>count){
				return new BaseResponse<Integer>(0);//卡余额>买桶数，传0；无需付钱,提交订单.只扣卡余额
			}
			if(cardBank.getPailcount()<count){
				return new BaseResponse<Integer>(count-cardBank.getPailcount());//还需要付的桶数
			}
			if(cardBank.getPailcount()==count){
				return new BaseResponse<Integer>(0);//水卡全额抵扣，无需付钱
			}
			return new BaseResponse<Integer>(cardBank.getPailcount()-count);
		}else{
			return new BaseResponse<Integer>(count);//无水卡，直接付原先桶数
		}

	}

	
	
	/**
	 * @Description 查询所有水卡列表
	 * @param  tenantCode，category
	 * @return  watercard
	 */
	@RequestMapping(value = "/watercard/list")
	public BaseResponse<List<WaterCard>> listWaterCard(@RequestBody WaterCard product) {
		log.info("【/watercard/list】接口调用开始"+product);
		List<WaterCard> waterCardtList=productService.listWaterCard(product);
		BaseResponse<List<WaterCard>> result=new BaseResponse<List<WaterCard>>(waterCardtList);
		return result;
	}
	
	
	
	/**
	 * @Description 查询订单
	 * @param  status，category
	 * @return   TODO:优化代码结构，去掉双层for循环；存订单表的时候，就可以直接存商品信息，不需要只是存id，没有多大意义
	 */
	@RequestMapping(value = "/order/search")
	public BaseResponse<List<Order>> listOrder(@RequestBody Order order) {
		log.info("【/order/search】接口调用开始"+order);
		List<Order> orderList=productService.searchOrder(order);

		for(Order ordersrc:orderList) {
			String infoJson=ordersrc.getProduct_info();
			log.info("【productInfo：】"+infoJson);
			List<Product> list  =JSON.parseArray(infoJson, Product.class); 
			String infos="";
			for(Product product:list) {
				Product products=productService.getProductById(product.getId());
				infos=infos+products.getName()+"  ×  "+product.getCount()+"<br>";
			}
			ordersrc.setProduct_info(infos);
		}
		BaseResponse<List<Order>> result=new BaseResponse<List<Order>>(orderList);
		return result;
	}
	
	
	/**
	 * @Description 指派订单
	 * @param  status，category
	 * @return
	 */
	@RequestMapping(value = "/order/point")
	public BaseResponse<String> pointOrder(@RequestBody Order order) {
		log.info("【/order/point】接口调用开始"+order);
		productService.pointOrder(order);
		return new BaseResponse<String>("指派成功");
	}
	
	
	
	/**
	 * @Description 修改订单状态为正在配送，同时发给买家
	 * @param  status，category
	 * @return
	 */
	@RequestMapping(value = "/order/deliverconfirm")
	public BaseResponse<String> confirmDeliverOrder(@RequestBody Order order) {
		log.info("【/order/deliverconfirm】接口调用开始"+order);
		productService.confirmDeliver(order);
		return new BaseResponse<String>("确认成功");
	}
	
	/**
	 * @Description 修改订单状态为已经完成，同时发给买家
	 * @param  status，category
	 * @return
	 */
	@RequestMapping(value = "/order/finish")
	public BaseResponse<String> finishOrder(@RequestBody Order order) {
		log.info("【/order/finsh】接口调用开始"+order);
		productService.finishOrder(order);
		return new BaseResponse<String>("订单已完成");
	}
	
	/**
	 * @Description 根据订单id查询状态
	 * @param  status，category
	 * @return
	 */
	@RequestMapping(value = "/order/getstatus")
	public BaseResponse<Order> getOrderStatus(@RequestBody Order order) {
		log.info("【/order/getstatus】接口调用开始"+order);
		Order orders = productService.getOrderStatus(order);
		return new BaseResponse<Order>(orders);
	}
	
	
}
