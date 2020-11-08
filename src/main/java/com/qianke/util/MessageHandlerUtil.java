package com.qianke.util;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.qianke.model.Tenant;
import com.qianke.service.TenantService;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息处理工具类 Created by xdp on 2016/1/26.
 */
@Slf4j
@Component
public class MessageHandlerUtil {

	@Value("${tenant_code}")
	String tenant_code;
	
    @Resource
	TenantService tenantService;
	
    Tenant tenant;
    
    @PostConstruct
    public void init() {
       //查询出租户的自动回复与关注回复；
    	 tenant=tenantService.selectOneByCode(tenant_code);
    	log.info("【微信消息处理查询到的租户信息】{}",tenant);
    }
	
	/**
	 * TODO:后期不拼接xml，用工具类进行转换。 把回关注自动复语和问答写进配置 解析微信发来的请求（XML）
	 * 
	 * @param request
	 * @return map
	 * @throws Exception
	 */
	public  Map<String, String> parseXml(HttpServletRequest request) throws Exception {
		// 将解析结果存储在HashMap中
		Map<String, String> map = new HashMap<String, String>();
		// 从request中取得输入流
		InputStream inputStream = request.getInputStream();
	//	System.out.println("获取输入流");
		// 读取输入流
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);
		// 得到xml根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		@SuppressWarnings("unchecked")
		List<Element> elementList = root.elements();

		// 遍历所有子节点
		for (Element e : elementList) {
			//System.out.println(e.getName() + "|" + e.getText());
			map.put(e.getName(), e.getText());
		}

		// 释放资源
		inputStream.close();
		inputStream = null;
		return map;
	}

	// 根据消息类型 构造返回消息
	public  String buildXml(Map<String, String> map) {
		String result;
		String msgType = map.get("MsgType").toString();
		//System.out.println("MsgType:" + msgType);
		//根据tenant_code查询出自动回复与关注后回复；
		//Tenant tenant= tenantService.
		
		
		if (msgType.toUpperCase().equals("TEXT")) {
			//result = buildTextMessage(map, "请稍等，客服小二正快马加鞭赶来回复您...或直接拨打【送水热线】0358-7850529");// TODO:将回复消息写进数据库，商户对应的那条
			result = buildTextMessage(map, tenant.getAuto_reply());// TODO:将回复消息写进数据库，商户对应的那条

		} else {
			String fromUserName = map.get("FromUserName");
			// 开发者微信号
			String toUserName = map.get("ToUserName");
			result = String.format(
					"<xml>" + "<ToUserName><![CDATA[%s]]></ToUserName>" + "<FromUserName><![CDATA[%s]]></FromUserName>"
							+ "<CreateTime>%s</CreateTime>" + "<MsgType><![CDATA[text]]></MsgType>"
							+ "<Content><![CDATA[%s]]></Content>" + "</xml>",
					fromUserName, toUserName, getUtcTime(),
					tenant.getFollow_reply());
					//"欢迎关注好水上府！好水上府，您身边的健康好水专家，支持一键订水，送水上门。真正把健康和便捷送您府上【送水热线】0358-7850529");// TODO:将回复消息写进数据库，在掮客端进行设置；
		}

		return result;
	}

	/**
	 * 构造文本消息
	 *
	 * @param map
	 * @param content
	 * @return
	 */
	private static String buildTextMessage(Map<String, String> map, String content) {
		// 发送方帐号
		String fromUserName = map.get("FromUserName");
		// 开发者微信号
		String toUserName = map.get("ToUserName");
		/**
		 * 文本消息XML数据格式 <xml> <ToUserName><![CDATA[toUser]]></ToUserName>
		 * <FromUserName><![CDATA[fromUser]]></FromUserName>
		 * <CreateTime>1348831860</CreateTime> <MsgType><![CDATA[text]]></MsgType>
		 * <Content><![CDATA[this is a test]]></Content> <MsgId>1234567890123456</MsgId>
		 * </xml>
		 */
		return String.format(
				"<xml>" + "<ToUserName><![CDATA[%s]]></ToUserName>" + "<FromUserName><![CDATA[%s]]></FromUserName>"
						+ "<CreateTime>%s</CreateTime>" + "<MsgType><![CDATA[text]]></MsgType>"
						+ "<Content><![CDATA[%s]]></Content>" + "</xml>",
				fromUserName, toUserName, getUtcTime(), content);
	}

	private static String getUtcTime() {
		Date dt = new Date();// 如果不需要格式,可直接用dt,dt就是当前系统时间
		DateFormat df = new SimpleDateFormat("yyyyMMddhhmm");// 设置显示格式
		String nowTime = df.format(dt);
		long dd = (long) 0;
		try {
			dd = df.parse(nowTime).getTime();
		} catch (Exception e) {

		}
		return String.valueOf(dd);
	}
}