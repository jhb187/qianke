/**
 * 
 */
package com.qianke.model;

import lombok.Data;

/**
 * 
 * Copyright © 2019 易联云计算有限公司. All rights reserved.
 * @Description: 水卡记录表
 * @author: 
 * @date: 
 * @version: V1.0
 */
@Data
public class CardBank {
     String id;
     String create_time;
     int type;
     int pailcount;
     String openid;
     String mobile;
     String contact;
     int status;
}
