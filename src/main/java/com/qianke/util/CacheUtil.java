/**
 * 
 */
package com.qianke.util;

import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

/**
 * 
 * Copyright © 2019 易联云计算有限公司. All rights reserved.
 * 
 * @Description: ConcurrentHashMap cache缓存工具类 缺点，如果不去调用检测，会一直存在 优点：简单，轻量，省内存
 * @author: jin.hui.bin
 * @date:
 * @version: V1.0
 */
@Component
public class CacheUtil {

	public static final ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<>();

	/**
	* @Description seconds 为秒
	* @param
	* @return
	*/
	public void set(String key, Object value, long seconds) {
		if (key == null) {
			return;
		}
		cache.put(key, value);
		cache.put(key + "_HoldTime", System.currentTimeMillis() + seconds*1000L);// 缓存失效时间
	}

	public void remove(String key) {
		cache.remove(key);
		cache.remove(key + "_HoldTime");
	}

	public Object get(String key) {
		if (checkCacheName(key)) {
			return cache.get(key);
		}
		return null;
	}

	public void clear() {
		cache.clear();
	}

	/**
	 * 检查缓存对象是否存在， 若不存在，则返回false 若存在，检查其是否已过有效期，如果已经过了则删除该缓存并返回false
	 * 
	 * @param cacheName
	 * @return
	 */
	public boolean checkCacheName(String key) {
		Long cacheHoldTime = (Long) cache.get(key + "_HoldTime");
		if (cacheHoldTime == null || cacheHoldTime == 0L) {
			return false;
		}
		if (cacheHoldTime < System.currentTimeMillis()) {
			remove(key);
			return false;
		}
		return true;
	}

}
