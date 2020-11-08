/**
 * 
 */
package com.qianke.controller.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
/**
 * 
 * Copyright © 2019 易联云计算有限公司. All rights reserved.
 * @Description: 
 * @author: jin.hui.bin 
 * @date: 
 * @version: V1.0
 */
@Configuration
public class WebSocketConfig {
  
  public ServerEndpointExporter serverEndpointExporter() {
    return new ServerEndpointExporter();
  }
}
