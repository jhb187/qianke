/**
 * 
 */
package com.qianke.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.qianke.controller.websocket.WebSocketServer;

/**
 * 
 * Copyright © 2019 易联云计算有限公司. All rights reserved.
 * 
 * @Description:
 * @author: jin.hui.bin
 * @date:
 * @version: V1.0
 */
@Service
public class WebSocketService {

	/**
	* @Description 发送websocket服务类，
	* @param message消息  cid商户ws 端口，cid
	* @return
	*/
	public String sendWsMsg(String message, String cid) {
		try {
		 return	WebSocketServer.sendInfo(message, cid);
		} catch (IOException e) {
			e.printStackTrace();
			return "error:" + cid + "#" + e.getMessage();
		}
	}

}
