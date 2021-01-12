/**
 * 
 */
package com.qianke.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 
 * Copyright © 2019 易联云计算有限公司. All rights reserved.
 * 
 * @Description:
 * @author: jin.hui.bin
 * @date:
 * @version: V1.0
 */
@Configuration
@ConfigurationProperties(prefix = "page", ignoreUnknownFields = true)
@Data
@Component
public class PageConfig {


	String baseurl;



}
