/**
 * 
 */
package com.qianke.model;

import javax.annotation.Resource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.qianke.service.CacheService;

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
@Component
@ConfigurationProperties(prefix = "wechat")
public class WxConfig {
	String token;
	String AppID;
	String AppSecret;
	String baseUrl;
	String MchId;
	String NotifyUrl;
	@Resource
	CacheService cacheService;
	// *******以下url配置不需要修改

	// 获取accessToken
	public String getAccessTokenUrl() {
		return "/cgi-bin/token?grant_type=client_credential&appid=" + AppID + "&secret=" + AppSecret;
	}

	// 通过code获取网页授权accessToken
	public String getWebAccessTokenUrl(String code) {
		return "/sns/oauth2/access_token?appid=" + AppID + "&secret=" + AppSecret + "&code=" + code
				+ "&grant_type=authorization_code";
	}

	// 通过accessToken openId获取用户信息
	public String getUserInfoByWebAccessTokenUrl(WebAccessToken webAccessToken) {
		return "/sns/userinfo?access_token=" + webAccessToken.getAccess_token() + "&openid="
				+ webAccessToken.getOpenid() + "&lang=zh_CN";
	}

	// 修改用户的自定义菜单(执行该接口前，确保缓存中已经存在access_token,手动执行刷新)
	public String setWxConsumerMenuUrl() {
		return "/cgi-bin/menu/create?access_token=" + cacheService.getCache("access_token").toString();
	}

	// 用于获取jsapi_ticket，用于获取JS-sdk的签名
	public String getJsapiTicketUrl() {
		return "/cgi-bin/ticket/getticket?access_token=" + cacheService.getCache("access_token").toString()
				+ "&type=jsapi";
	}

	// 拉取全部关注用户的
	public String getWxUserListUrl() {
		return "/cgi-bin/user/get?access_token=" + cacheService.getCache("access_token").toString() + "&next_openid=";
	}

	// 发送模板消息
	public String sendTemplateMsgUrl() {
		return "/cgi-bin/message/template/send?access_token=" + cacheService.getCache("access_token").toString();
	}

}
