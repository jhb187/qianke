/**
 * 
 */
package com.qianke.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.qianke.controller.base.BaseResponse;
import com.qianke.model.MsgTemplate;
import com.qianke.model.SMS;
import com.qianke.model.TemplateKeyWord;
import com.qianke.model.TemplateMsg;
import com.qianke.model.User;
import com.qianke.model.WebAccessToken;
import com.qianke.service.CacheService;
import com.qianke.service.WxService;
import com.qianke.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Copyright © 2019 易联云计算有限公司. All rights reserved.
 * 
 * @Description:
 * @author: jin.hui.bin
 * @date:
 * @version: V1.0
 */
@Slf4j
@RestController
@RequestMapping(value = "/wx")
@CrossOrigin
public class WeChatController {

	@Resource
	WxService wxService;

	@Resource
	CacheService cacheService;

	/**
	 * @Description 用于微信与服务端通信验证
	 * @param
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public void validate(HttpServletRequest req, HttpServletResponse resp) {
		wxService.tokenCheck(req, resp);
	}

	/**
	 * @Description 处理微信用户发过来的消息
	 * @param
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	public void handleMsg(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		wxService.handleMsg(req, resp);
	}

	/**
	 * @Description 生成js-sdk用于签名的Signature
	 * @param 当前页面的html url
	 * @return appId: '', // 必填，公众号的唯一标识 timestamp: , // 必填，生成签名的时间戳 nonceStr: '',
	 *         // 必填，生成签名的随机串 signature: '',// 必填，签名
	 * @throws IOException
	 */
	@RequestMapping(value = "/jsapi/getSignature")
	public BaseResponse<Map<String, String>> getSignature(@RequestBody Map<String, String> data) {
		log.info("【jsapi/getSignature】调用开始" + data);
		Map<String, String> map = wxService.getJsApiSignature(data.get("htmlUrl").toString());
		return new BaseResponse<Map<String, String>>(map);

	}

	// cache/get set 测试使用*************正式业务不需要
	@RequestMapping(value = "/cache/set")
	public String cacheSet(@RequestBody SMS sms) {
		cacheService.setCache(sms.getMobile(), sms.getCode(), sms.getHandle());
		return "success";
	}

	/**
	 * @Description 演示如何获取access_token
	 * @param { "access_token":"access_token" }
	 * @return
	 */
	@RequestMapping(value = "/cache/get")
	public String cacheGet(@RequestBody Map<String, String> key) {
		return cacheService.getCache(key.get("access_token")).toString();
	}

	/**
	 * @Description 手动拉取access_token并存入缓存，用于项目启动前
	 * @param TODO:项目启动后先触发执行该接口
	 * @return
	 */
	@RequestMapping(value = "/accessToken/fresh")
	public BaseResponse<String> accessToken() {
		wxService.getAccessToken();
		return new BaseResponse<String>();
	}

	/**
	 * @Description 外部手动用access_token获取jsapi_ticket并存入缓存，用于获取JS-SDK签名进行JS-SDK调用
	 * @param accessToken 已经在初始化函数中自动获取,定时任务，无需手动再调用
	 * @return
	 */
	@RequestMapping(value = "/jsapiTicket/get")
	public BaseResponse<String> getJsapiTicket() {
		wxService.getJsapiTicket();
		return new BaseResponse<String>();
	}

	/**
	 * @Description 拉取全部关注用户列表
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/user/list")
	public BaseResponse<Map<String, Object>> getWxUserList() {
		Map<String, Object> map = wxService.getWxUserList();
		return new BaseResponse<Map<String, Object>>(map);
	}

	/**
	 * @Description 获取用户的code获取网页授权的access_token
	 * @param code是为了获取 webaccessToken，合并为一步
	 * @return water card mall center
	 * @throws IOException
	 */
	@RequestMapping(value = "/user/code/get")
	public void getUserCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO:根据state，重定向到不同的业务页面 跳转之前，先判断，该openid在数据库中是否存在，如果存在，证明已经注册过信息，
		// 如果数据库中没有，则让用户去注册（手机，地址，联系人，openid，access_token），
		WebAccessToken webAccessToken = wxService.getWebAccessToken(request.getParameter("code"));
		User user = wxService.getUserByOpenid(webAccessToken);
		log.info("根据openid查询到的user是:" + user);
		
		String state = request.getParameter("state");
		if("worker".equals(state)) {
			response.sendRedirect("http://qianke668.club:8090/worker-add.html?openid=" + webAccessToken.getOpenid());
		}
		// TODO:根据state参数做重定向三个不同的入口网页
		if (user != null) {
			// 已经注册过，但凡注册，就要填手机号，(openid)登录过的老用户
			// 跳去三个业务页面
			// 一键订水的页面
			if ("water".equals(state)) {
				response.sendRedirect("http://qianke668.club:8090/buywater.html?user="+user.getOpenid());
				// 水卡充值
			} else if ("card".equals(state)) {
				response.sendRedirect("http://qianke668.club:8090/watercard.html?user="+user.getOpenid());
				// 在线商城
			} else if ("mall".equals(state)) {
				response.sendRedirect("http://qianke668.club:8090/mall.html?user="+user.getOpenid());
				// 个人中心
			} else if ("center".equals(state)) {
				response.sendRedirect("http://qianke668.club:8090/user-center.html?user="+user.getOpenid());
				
			} else {
				response.sendRedirect("http://qianke668.club:8090/404.html");

			}
		} else {
			//送水工扫码注册
			response.sendRedirect("http://qianke668.club:8090/member-add.html?openid=" + webAccessToken.getOpenid()
					+ "&access_token=" + webAccessToken.getAccess_token() + "&refresh_token="
					+ webAccessToken.getRefresh_token() + "&state=" + state);
		}

	}

	/**
	 * @Description 发送模板消息调用DEMO
	 * @param TODO:抽象为service，传入参数进行调用
	 * @return
	 */
	@RequestMapping(value = "/send/templateMsg")
	public BaseResponse<String> sendTemplateMsg() {
		TemplateMsg templateMsg = new TemplateMsg();
		templateMsg.setTouser("onJZEwhl4dGAkaYwzrCTF6S8h1rE");// touser从业务中获取

		// 决定用哪个模板发送，需要：1.设置模板Id 2.构造发送参数 ，同时调用该模板对应的参数构造方法
		// 3.在wx服务发送方法中添加templateMsg 参数 与获取data；
		templateMsg.setTemplate_id(MsgTemplate.ORDER_FINISHED.getTemplate_id());

		HashMap<String, String> param = new HashMap<String, String>();

		param.put("orderId", "47474747474743636362828");
		param.put("productName", "童装玻尿酸*7桶");
		param.put("orderTime", "2019-11-10 11:21");
		MsgTemplate.ORDER_FINISHED.orderFinishedData(param);// 记得调用对应的data方法,传入构造的param
		wxService.sendTemplateMsg(templateMsg, MsgTemplate.ORDER_FINISHED.getData());
		return new BaseResponse<String>();
	}

}
