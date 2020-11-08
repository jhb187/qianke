/**
 * 
 */
package com.qianke.util;

import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

/**
 * 
 * Copyright © 2019 
 * 
 * @Description:
 * @author: jin.hui.bin
 * @date:
 * @version: V1.0
 */

public class CodeUtil {

	/**
	* @Description sha256密码算法
	* @param
	* @return
	*/
	public static String sha256(String rawString) {
		return DigestUtils.sha256Hex(rawString);
	}
	/**
	* @Description sha1密码算法
	* @param
	* @return
	*/
	public static String sha1(String rawString) {
		return DigestUtils.sha1Hex(rawString);
	}
	
	/**
	* @Description 产生指定位数验证码算法
	* @param
	* @return
	*/
	public static String creatRandom(int len) {
		Random r = new Random();
		StringBuilder rs = new StringBuilder();
		for (int i = 0; i < len; i++) {
			rs.append(r.nextInt(10));
		}
		return rs.toString();
	}
	
}
