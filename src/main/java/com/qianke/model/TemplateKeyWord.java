/**
 * 
 */
package com.qianke.model;

import lombok.Data;

/**
 * 
 * Copyright © 2019 易联云计算有限公司. All rights reserved.
 * 
 * @Description:
 * @author: jin.hui.bin
 * @date:
 * @version: V1.0
 */
@Data
public class TemplateKeyWord {
	String value;
	/**** 默认: #173177 *******/
	String color="#173177";

	public TemplateKeyWord(String value) {
		super();
		this.value = value;
	}

}
