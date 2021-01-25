/**
 * 
 */
package com.qianke.model;

import lombok.Data;

/**
 * 
 * Copyright © 2019 
 * @Description: 微信用户实体类
 * @author: jin.hui.bin 
 * @date: 
 * @version: V1.0
 */
@Data
public class Order {
	String orderid;
	String create_time;
	/******在指派订单接口中，为送水工的openid******/
	String openid;//买家微信openid
	String tenant_code;
	String money;
	String product_info;//商品的名称，价格，数量，id；
    String status;//未支付 0；  未配送 1；  已完成 2
	String out_trade_no;
	String delivery_address;
    String mobile;
    String contact;//联系人
    String deliveryman;//配送员//为配送员openid
	String searchparam; //查询参数
    
    
	
   
     
 
    
}
