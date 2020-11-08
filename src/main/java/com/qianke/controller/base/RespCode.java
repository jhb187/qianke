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
public enum RespCode {

    RESP_CODE_000000("0000000", "执行成功"),
    RESP_CODE_999999("9999990", "系统异常"),
    RESP_CODE_000001("000001", "帐号或密码无效"),
    RESP_CODE_000002("000002", "当前账户下无电子水票"),
    RESP_CODE_0002000("0002000", "缺少必要参数");
    private String respCode;
    private String respContent;

    RespCode(String code, String content) {
        respCode = code;
        respContent = content;
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
    
}
