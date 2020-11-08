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
public class BaseResponse<T> {
	// 响应头文件
    private RespHead head;
    // 响应正确内容
    private T body;

    public BaseResponse() {
        head = new RespHead();
        head.setRespCode(RespCode.RESP_CODE_000000.getRespCode());
        head.setRespContent(RespCode.RESP_CODE_000000.getRespContent());
    }

    public BaseResponse(RespCode code) {
        head = new RespHead();
        head.setRespCode(code.getRespCode());
        head.setRespContent(code.getRespContent());
    }

    public BaseResponse(T body) {
        head = new RespHead();
        head.setRespCode(RespCode.RESP_CODE_000000.getRespCode());
        head.setRespContent(RespCode.RESP_CODE_000000.getRespContent());
        this.body = body;
    }

    public RespHead getHead() {
        return head;
    }

    public void setHead(RespHead head) {
        this.head = head;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "RespBean [head=" + head + ", body=" + body + "]";
    }
}
