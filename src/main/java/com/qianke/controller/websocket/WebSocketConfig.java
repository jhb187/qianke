/**
 *
 */
package com.qianke.controller.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * Copyright © 2019 掮客. All rights reserved.
 *
 * @Description:
 * @author: jin.hui.bin
 * @date:
 * @version: V1.0
 */
@Configuration
public class WebSocketConfig implements WebMvcConfigurer {


    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }


}
