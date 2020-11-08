/**
 * 
 */
package com.qianke.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.qianke.dao.UserDao;
import com.qianke.model.TemplateKeyWord;
import com.qianke.model.TemplateMsg;
import com.qianke.model.User;
import com.qianke.model.WebAccessToken;
import com.qianke.model.WxConfig;
import com.qianke.util.CodeUtil;
import com.qianke.util.HttpClientUtil;
import com.qianke.util.JsonUtil;
import com.qianke.util.MessageHandlerUtil;
import com.qianke.util.WxUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Copyright © 2019 易联云计算有限公司. All rights reserved.
 * 
 * @Description: 微信服务类
 * @author: jin.hui.bin
 * @date:
 * @version: V1.0
 */
@Slf4j
@Service
public class WxService {

	@Resource
	WxConfig wxConfig;

	@Resource
	private RestTemplate restTemplate;

	@Resource
	CacheService cacheService;

	@Resource
	UserDao userDao;
	
	@Resource
	MessageHandlerUtil messageHandlerUtil;

	private WebClient webClient;
	MultiValueMap<String, String> params;

	@PostConstruct
	public void init() {
		webClient = WebClient.create(wxConfig.getBaseUrl());
		params = new LinkedMultiValueMap<>();
	}

	/**
	 * @Description 服务器与微信通信的token校验
	 * @param
	 * @return
	 */
	public void tokenCheck(HttpServletRequest req, HttpServletResponse resp) {
		String signature = req.getParameter("signature");
		String timestamp = req.getParameter("timestamp");
		String nonce = req.getParameter("nonce"); // 随机数
		String echostr = req.getParameter("echostr");// 随机字符串
		String TOKEN = wxConfig.getToken();
		String sortStr = WxUtil.sort(TOKEN, timestamp, nonce);
		String mySignature = CodeUtil.sha1(sortStr);
		// 校验微信服务器传递过来的签名 和 加密后的字符串是否一致, 若一致则签名通过
		if (!"".equals(signature) && !"".equals(mySignature) && signature.equals(mySignature)) {
			try {
				resp.getWriter().write(echostr);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("-----校验签名失败-----");
		}
	}

	/**
	 * @Description 处理微信用户发过来的消息
	 * @param
	 * @return
	 * @throws IOException
	 */
	public void handleMsg(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO 接收、处理、响应由微信服务器转发的用户发送给公众帐号的消息
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String result = "";
		try {
			Map<String, String> map = messageHandlerUtil.parseXml(request);
			result = messageHandlerUtil.buildXml(map);
			if (result.equals("")) {
				result = "未正确响应";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.getWriter().println(result);
	}

	/**
	 * TODO:重新规划定时任务时间，与存入缓存的时间
	 * 
	 * @Description 定时更新accessToken，存入cache
	 * @param
	 * @return
	 */
	@Scheduled(cron = "0 0 0/2 * * ?")
	public void accessTokenSchedule() {
		log.info("定时任务accessTokenSchedule======开始执行");
		getAccessToken();
		 getJsapiTicket();
		log.info("定时任务accessTokenSchedule======执行结束");
	}

	/**
	 * @Description 获取accessToken (区别于网页授权access_token) 存入缓存，
	 * @param
	 * @return
	 */
	public void getAccessToken() {
		/*HttpClientUtil.postForm(webClient, params, wxConfig.getAccessTokenUrl()).subscribe(response -> {
			cacheService.setCache("access_token", response.get("access_token").toString(),
					response.get("expires_in").toString());
			log.info("保存入redis的access_token:" + response.get("access_token").toString());
		});*/
		
		@SuppressWarnings("unchecked")
		Map<String,Object> response= HttpClientUtil.postForm(webClient, params, wxConfig.getAccessTokenUrl()).block();
		cacheService.setCache("access_token", response.get("access_token").toString(),
				response.get("expires_in").toString());
		log.info("保存入redis的access_token:" + response.get("access_token").toString());
	}

	/**
	 * @Description 获取jsapi_ticket (用于获取JS-SDK调用签名) 存入缓存，
	 * @param
	 * @return
	 */
	public void getJsapiTicket() {
		HttpClientUtil.postForm(webClient, params, wxConfig.getJsapiTicketUrl()).subscribe(response -> {
			log.info("【获取jsapi-ticket】返回结果:" + response.get("errmsg").toString());
			cacheService.setCache("ticket", response.get("ticket").toString(), response.get("expires_in").toString());
			log.info("【jsapi_ticket结束】保存入redis的ticket:" + response.get("ticket").toString());
		});
	}

	/**
	 * @Description 返回签名signature ，用于wx.config注册，
	 * @param
	 * @return
	 */
	public Map<String, String> getJsApiSignature(String htmlUrl) {

		String timestamp = (System.currentTimeMillis() / 1000) + "";
		String nonceStr = WxUtil.createRanDomStr();
		String jsapiTicket = cacheService.getCache("ticket").toString();
		Map<String, String> signMap = new HashMap<String, String>();
		signMap.put("appId", wxConfig.getAppID());
		signMap.put("timestamp", timestamp);
		signMap.put("nonceStr", nonceStr);
		signMap.put("signature", WxUtil.getSignature(timestamp, nonceStr, htmlUrl, jsapiTicket));// 调用util来生成
		return signMap;
	}

	/**
	 * @Description 获取网页授权accessToken与openid
	 * @param
	 * @return
	 */
	public WebAccessToken getWebAccessToken(String code) {
		String webUrl = wxConfig.getWebAccessTokenUrl(code);
		String response = HttpClientUtil.postFormStr(webClient, params, webUrl).block();
		log.info("response:" + response);
		WebAccessToken webAccessToken = JsonUtil.parse(response, WebAccessToken.class);
		log.info("get=====webAccessToken:" + webAccessToken);// TODO:存入数据库用户表
		return webAccessToken;

	}

	/**
	 * @Description 根据openid查询用户是否存在
	 * @param
	 * @return
	 */
	public User getUserByOpenid(WebAccessToken webAccessToken) {
		return userDao.getUserByOpenid(webAccessToken.getOpenid());
	}

	/**
	 * //TODO:待定是否要使用
	 * 
	 * @Description 通过accessToken与openid 获取用户昵称(头像)信息
	 * @param
	 * @return
	 */
	public void getUserInfo(WebAccessToken webAccessToken) {
		String webUrl = wxConfig.getUserInfoByWebAccessTokenUrl(webAccessToken);
		HttpClientUtil.postFormStr(webClient, params, webUrl).subscribe(response -> {
			log.info("response:" + response);
			// WebAccessToken webAccessTokens=JsonUtil.parse(response,
			// WebAccessToken.class);

			// TODO:存入数据库user表

		});
	}

	/**
	 * @Description 设置用户微信上的自定义菜单，
	 * @param TODO:改为postJson方式，将json参数改为Map pojo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void setWxConsumerMenu(String json) {
		Map<String, Object> args = new HashMap<String, Object>();
		args = JsonUtil.parse(json, args.getClass());
		log.info("argsJson后的Map:" + args);
		HttpClientUtil.postJSON(webClient, args, wxConfig.setWxConsumerMenuUrl()).subscribe(response -> {
			log.info("setWxConsumerMenu修改菜单的返回:" + response.toString());

		});

	}

	// onJZEwhl4dGAkaYwzrCTF6S8h1rE (靳慧斌wx)
	/**
	 * @Description 拉取全部微信关注用户列表
	 * @param TODO:做成同步接口，block，将列表返回。
	 * @return
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getWxUserList() {
		return HttpClientUtil.postForm(webClient, params, wxConfig.getWxUserListUrl()).block();
	}

	/**
	 * @Description 发送模板消息
	 * @param 模板枚举，touser，订单信息，
	 * @return
	 */
	public void sendTemplateMsg(TemplateMsg templateMsg, HashMap<String, TemplateKeyWord> data) {
		log.info("【发送模板消息sendTemplateMsg】开始"+data);
		Map<String, Object> formData = new HashMap<String, Object>();
		formData.put("touser", templateMsg.getTouser());
		formData.put("template_id", templateMsg.getTemplate_id());
		formData.put("data", data);// data信息在外封装好
		formData.put("url", templateMsg.getUrl());//可为空
		// TODO:将异步的请求换成httpclient
		HttpClientUtil.postJSON(webClient, formData, wxConfig.sendTemplateMsgUrl()).subscribe(response -> {
			log.info("【模板消息接口返回】:"+response);
		});
	}
	

}
