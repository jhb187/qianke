/**
 * 
 */
package com.qianke.controller.base;

/**
 * 
 * Copyright © 2019 易联云计算有限公司. All rights reserved.
 * @Description: 
 * @author: jin.hui.bin 
 * @date: 
 * @version: V1.0
 */
public class RespHead {
	// 响应码
    private String respCode;
    // 响应内容
    private String respContent;

    public RespHead() {

    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespContent() {
        return respContent;
    }

    public void setRespContent(String respContent) {
        this.respContent = respContent;
    }

    public RespHead(RespCode resp) {
    	 this.respCode = resp.getRespCode();
         this.respContent = resp.getRespContent();
    }
    
    public void setResp(RespCode resp) {
        this.respCode = resp.getRespCode();
        this.respContent = resp.getRespContent();
    }

    @Override
    public String toString() {
        return "RespBeanHead [respCode=" + respCode + ", respContent="
                + respContent + "]";
    }
}
