/**
 * 
 */
package com.qianke.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

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
public class WxUtil {
	/**
	 * 参数排序
	 * 
	 * @param token
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	public static String sort(String token, String timestamp, String nonce) {
		String[] strArray = { token, timestamp, nonce };
		Arrays.sort(strArray);
		StringBuilder sb = new StringBuilder();
		for (String str : strArray) {
			sb.append(str);
		}
		return sb.toString();
	}

	
	/**
	 * 生成随机串
	 * 
	 * @param token
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	public static String createRanDomStr() {

		String str = "";
		// 生成随机串
		for (int i = 0; i <= 100; i++) {
			String sources = "0a1b2c3d4e5f6g7h8i9"; // 加上一些字母，
			Random rand = new Random();
			StringBuffer flag = new StringBuffer();
			for (int j = 0; j < 6; j++) {
				flag.append(sources.charAt(rand.nextInt(9)) + "").toString();
			}
			str = flag.toString();
		}
		return str;
	}
	
	
	
	public static String getSignature(String timestamp, String nonceStr, String url, String jsapiTicket) {
        MessageDigest md = null;
        String string1 = "jsapi_ticket=" + jsapiTicket
                + "&noncestr=" + nonceStr + "×tamp=" + timestamp + "&url="
                + url;
 
        String signature = "";
        try {
            md = MessageDigest.getInstance("SHA");
            byte[] digest = md.digest(string1.getBytes());
            signature = byteToStr(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        log.info("【WXutil生成的getSignature】"+signature);
        return signature;
    }
    
	
	private static String byteToStr(byte[] digest) {
        String strDigest = "";
        for (int i = 0; i < digest.length; i++) {
            strDigest = strDigest + byteToHexStr(digest[i]);
        }
        return strDigest;
    }
	
	public static String byteToHexStr(byte bs) {
		return Integer.toHexString(bs);
	}
	
	
	 /**
     * 将字符串xml转换成Map
     *
     * @param xml
     * @return Map<String,String>
     */
    public static Map<String, String> xmlToMap(String xml) {
        if (StringUtils.isEmpty(xml)) {
            throw new RuntimeException("解析微信请求返回的xml为空");
        }
        Map<String, String> map = new TreeMap<String, String>();
        try {
            // 将传入的字符串xml转换成Document对象
            Document doc = DocumentHelper.parseText(xml);
            // 获取根节点
            Element root = doc.getRootElement();
            // 获取所有的子节点
            @SuppressWarnings("rawtypes")
            List list = root.elements();
            // 遍历添加到map中
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    Element child = (Element) list.get(i);
                    map.put(child.getName(), child.getTextTrim());
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return map;
    }


	

}
