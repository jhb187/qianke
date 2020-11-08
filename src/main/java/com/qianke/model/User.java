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
public class User {
	String openid;//微信openid
    String mobile;
    String access_token;
    String refresh_token;
    String contact;//联系人
    String address;//送货地址
    String tenant_code;
    /*******非必填*******/
    String nickname;//昵称
    String pwd;
    
    //TODO:这里增加水卡信息，非必填
    String waterName;
    String waterId;
    int waterCount;
}
