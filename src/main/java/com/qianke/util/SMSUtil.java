package com.qianke.util;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import com.qianke.model.TencentConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Copyright © 2019 易联云计算有限公司. All rights reserved.
 * 
 * @Description: 短信工具类
 * @author: jin.hui.bin
 * @date:
 * @version: V1.0
 */
@Component
@Slf4j
public class SMSUtil {

	@Resource
	TencentConfig config;

	private WebClient webClient;
	String apiUrl;

	Long random;

	@PostConstruct
	public void init() {

		webClient = WebClient.create(config.getBaseurl());
		random = getRandom();
		apiUrl = "/sendsms?sdkappid=" + config.getSdkappid() + "&random=" + random;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> send(String mobile, String handle, String veriCode) {
		Long time = getCurrentTime();
		Map<String, Object> formData = new LinkedHashMap<>();
		Map<String, String> header = new LinkedHashMap<>();
		Map<String, String> tel = new HashMap<>();
		tel.put("mobile", mobile);
		tel.put("nationcode", "86");

		String sig = sha256(config.getAppKey(), random, time, mobile);
		String[] params = { handle, veriCode };
		formData.put("params", params);
		formData.put("sig", sig);
		formData.put("sign", TencentConfig.sign);
		formData.put("tel", tel);
		formData.put("time", time);
		formData.put("tpl_id", Integer.parseInt(config.getTplid()));
		// 响应式编程，需要链路上全部是异步
		/*
		 * HttpClientUtil.postJSONWithHead(webClient, formData, apiUrl,
		 * header).subscribe(response -> { log.info("调用腾讯短信接口返回:"+response); });
		 */
		Map<String, Object> res = HttpClientUtil.postJSONWithHead(webClient, formData, apiUrl, header).block();
		log.info("腾信短信接口返回:"+res.toString());
		return res;
	}

	/**
	 * @Description sha256编码
	 * @param
	 * @return
	 */
	public static String sha256(String appkey, long random, long time, String mobile) {
		StringBuffer buffer = new StringBuffer("appkey=").append(appkey).append("&random=").append(random)
				.append("&time=").append(time).append("&mobile=").append(mobile);
		return CodeUtil.sha256(buffer.toString());
	}

	/**
	 * @Description 获取unix时间戳
	 * @param
	 * @return
	 */
	public static long getCurrentTime() {
		return System.currentTimeMillis() / 1000;
	}

	/**
	 * @Description 获取随机数
	 * @param
	 * @return
	 */
	public static long getRandom() {
		return (new Random(getCurrentTime())).nextInt(900000) + 100000;
	}

	/**
	 * @Description
	 * @param
	 * @return
	 */
	public static void main(String[] args) {
		String unixTime = Long.toString(new Date().getTime());
		System.out.println(unixTime);
	}

}
