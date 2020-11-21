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

     public String getId() {
          return id;
     }

     public void setId(String id) {
          this.id = id;
     }

     public String getCreate_time() {
          return create_time;
     }

     public void setCreate_time(String create_time) {
          this.create_time = create_time;
     }

     public int getType() {
          return type;
     }

     public void setType(int type) {
          this.type = type;
     }

     public int getPailcount() {
          return pailcount;
     }

     public void setPailcount(int pailcount) {
          this.pailcount = pailcount;
     }

     public String getOpenid() {
          return openid;
     }

     public void setOpenid(String openid) {
          this.openid = openid;
     }

     public String getMobile() {
          return mobile;
     }

     public void setMobile(String mobile) {
          this.mobile = mobile;
     }

     public String getContact() {
          return contact;
     }

     public void setContact(String contact) {
          this.contact = contact;
     }

     public int getStatus() {
          return status;
     }

     public void setStatus(int status) {
          this.status = status;
     }
}
