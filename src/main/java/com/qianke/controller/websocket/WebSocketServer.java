/**
 * 
 */
package com.qianke.controller.websocket;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Copyright © 2019 . All rights reserved.
 * 
 * @Description:
 * @author: jin.hui.bin
 * @date:
 * @version: V1.0
 */
@Component
@ServerEndpoint("/websocket/{sid}")
@Slf4j
public class WebSocketServer {

	/**
	 * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
	 */
	private static int onlineCount = 0;
	/**
	 * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
	 */
	private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<>();
	/**
	 * 与某个客户端的连接会话，需要通过它来给客户端发送数据
	 */
	private Session session;
	/**
	 * 接收sid
	 */
	private String sid = "";

	/**
	 * 连接建立成功调用的方法
	 **/
	@OnOpen
	public void onOpen(Session session, @PathParam("sid") String sid) {
		this.session = session;
		// 加入set中
		webSocketSet.add(this);
		// 在线数加1
		addOnlineCount();
		log.info("有新窗口开始监听:" + sid + ",当前在线人数为" + getOnlineCount());
		this.sid = sid;
		try {
			sendMessage("连接成功");
		} catch (IOException e) {
			log.error("websocket IO异常");
		}
	}

	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose() {
		// 从set中删除
		webSocketSet.remove(this);
		// 在线数减1
		subOnlineCount();
		log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
	}

	/**
	 * 收到客户端消息后调用的方法
	 * 
	 * @param message 客户端发送过来的消息
	 **/
	@OnMessage
	public void onMessage(String message, Session session) {
		log.info("收到来自窗口" + sid + "的信息:" + message);
		// 群发消息
		for (WebSocketServer item : webSocketSet) {
			try {
				item.sendMessage(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		log.error("发生错误");
		error.printStackTrace();
	}

	/**
	 * 实现服务器主动推送
	 */
	public String sendMessage(String message) throws IOException {
		this.session.getBasicRemote().sendText(message);
		return "success";
	}

	/**
	 * 群发自定义消息
	 */
	public static String sendInfo(String message, @PathParam("sid") String sid) throws IOException {
        
		if(webSocketSet.size()==0) {
			return "当前没有商户在线";
		}else {
			for (WebSocketServer item : webSocketSet) {
				try {
					// 为null则全部推送
					if (sid == null) {
						item.sendMessage(message);
					} else if (item.sid.equals(sid)) {//跟websocket一致，可以发送
						if (item.session.isOpen()) {
							return item.sendMessage(message);
						}//不需要再写额外else，

					}else {
						//跟websocket不一致
						// TODO:向商户微信端推送模板消息，或者发送短信
						return sid + "该商户管理端未运行";
					}
				} catch (IOException e) {
					continue;
				}
			}
		}
		return "";
		
		
	}

	public static synchronized int getOnlineCount() {
		return onlineCount;
	}

	public static synchronized void addOnlineCount() {
		WebSocketServer.onlineCount++;
	}

	public static synchronized void subOnlineCount() {
		WebSocketServer.onlineCount--;
	}

}
