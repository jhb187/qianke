/**
 * 
 */
package com.qianke.model;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 
 * Copyright © 2019 易联云计算有限公司. All rights reserved.
 * @Description: 
 * @author: jin.hui.bin 
 * @date: 
 * @version: V1.0
 */
@Data
public class Product {
   String id;
   String tenantCode;
   String name;
   BigDecimal price;
   int count;
   int category;
   String img;
}
