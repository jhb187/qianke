/**
 * 
 */
package com.qianke.model;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 
 * Copyright © 2019 . All rights reserved.
 * 
 * @Description:水卡实体类
 * @author: jin.hui.bin
 * @date:
 * @version: V1.0
 */
@Data
public class WaterCard {

	int id;
	/****水卡可买水桶水***/
	int pailcount;
	String tenant_code;
	/**水卡全称***/
	String name;
	/***水卡售价***/
	BigDecimal price;
	/***图片**/
	String img;
	int count;

}
