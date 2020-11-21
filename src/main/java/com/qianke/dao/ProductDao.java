/**
 * 
 */
package com.qianke.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.qianke.model.CardBank;
import com.qianke.model.Category;
import com.qianke.model.Order;
import com.qianke.model.Product;
import com.qianke.model.WaterCard;

/**
 * 
 * Copyright © 2019 订单与商品服务类放在一起. All rights reserved.
 * @Description: 
 * @author: jin.hui.bin 
 * @date: 
 * @version: V1.0
 */
@Mapper
public interface ProductDao {
     /**
    * @Description  增加新商品种类
    * @param
    * @return
    */
    public int addCategory(Category category);
     
     //
     /**
    * @Description  查询商品
    * @param
    * @return
    */
    public List<Product> listProduct(Product product);
    
    
    /**
    * @Description  查询商品
    * @param
    * @return
    */
    public Product getProductById(@Param("id")String id);
    
    
    /**
    * @Description  生成一个新订单
    * @param
    * @return
    */
    public int creatOrder(Order order);
    
    
    /**
     * @Description  修改订单状态为已支付，未配送1
     * @param
     * @return
     */
     public int payOrder(@Param("orderid")String orderid, @Param("money")String money);

	/**
	* @Description  返回订单info
	* @param
	* @return
	*/
	public Order getOrderInfoById(@Param("orderid")String orderId);

	/**
	* @Description 
	* @param
	* @return
	*/
	public List<Order> getOrderByParam(Order order);

	/**
	* @Description 
	* @param
	* @return
	*/
	public int confirmDeliver(Order order);

	/**
	* @Description 
	* @param
	* @return
	*/
	public List<WaterCard> listWaterCard(WaterCard watercard);

	/**
	* @Description 
	* @param
	* @return
	*/
	public WaterCard getCardById(int id);

	/**
	* @Description 
	* @param
	* @return
	*/
	public int creatCardBank(CardBank bank);

	/**
	* @Description 根据openid与type查询水卡
	* @param
	* @return
	*/
	public CardBank getCardByParam(CardBank bankParam);

	/**
	* @Description 在该条card_bank的水桶数加水桶
	* @param
	* @return
	*/
	public int addCardBankPailCount(CardBank cardResult);

	public CardBank getCardCountByOpenid(@Param("openid")String openid,@Param("type")int type);

	/**
	* @Description 根据openid查询用户下面的水卡桶数
	* @param
	* @return
	*/
	public List<Product> listCardWaterByOpenid(@Param("openid")String openid);

	
} 
