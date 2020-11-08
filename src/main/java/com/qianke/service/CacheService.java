/**
 * 
 */
package com.qianke.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qianke.util.CacheUtil;

/**
 * 
 * Copyright © 2019 易联云计算有限公司. All rights reserved.
 * @Description:  缓存服务类
 * @author: jin.hui.bin 
 * @date: 
 * @version: V1.0
 */
@Service
public class CacheService {
	@Resource
	CacheUtil cacheUtil;
	
	/**
	* @Description 缓存值
	* @param seconds 秒数
	* @return
	*/
	public void setCache(String key,Object value,String seconds) {
		
		cacheUtil.set(key, value,Long.parseLong(seconds));
	}
	
	/**
	* @Description 取值
	* @param   TODO:对于返回null的判断，放在业务层
	* @return
	*/
	public Object getCache(String key) {
		if(cacheUtil.get(key)==null) {
			return "";
		}else {
			return cacheUtil.get(key);
		}
		
	}
	
}
