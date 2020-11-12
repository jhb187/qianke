/**
 * 
 */
package com.qianke.model;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import com.qianke.service.WxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
@Slf4j
public class WxConfig {
	@Value("${tenant_code}")
	String tenant_code;

	String token;
	String AppID;
	String AppSecret;
	String baseUrl;
	String MchId;
	String NotifyUrl;
	@Resource
	CacheService cacheService;

	@Resource
	WxService  wxService;

	@PostConstruct
	public void getWXConfigFromDb(){
		log.info("开始从数据库加载微信支付配置...");
		WxConfigDTO wxConfig=wxService.getWxConfig(tenant_code);
		token=wxConfig.token;
		AppID=wxConfig.AppID;
		baseUrl=wxConfig.baseUrl;
		MchId=wxConfig.MchId;
		NotifyUrl=wxConfig.NotifyUrl;
		AppSecret=wxConfig.AppSecret;
		log.info("当前租户微信支付配置，token={},AppID={},baseUrl={},MchId={},NotifyUrl={},AppSecret={}",token,AppID,baseUrl,MchId,NotifyUrl,AppSecret);
	}


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
