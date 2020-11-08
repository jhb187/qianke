/**
 * 
 */
package com.qianke;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 
 * Copyright © 2019 易联云计算有限公司. All rights reserved.
 * 
 * @Description:
 * @author: jin.hui.bin
 * @date:
 * @version: V1.0
 */
public class MyTest {

	/**
	 * @Description
	 * @param
	 * @return
	 */
	public static void main(String[] args) {
		/*//System.out.println(creatRandom(4));
		String info="[{id=4, count=1}, {id=6, count=1}, {id=7, count=1}]";
		List<String> lis = Arrays.asList(info.split(","));
		
		for(String ss:lis) {
			System.out.println(ss);
			
		}*/
		
		 String cellphone="18735445908";
		 String timeStamp=Long.toString(new Date().getTime());
		 String cardId=timeStamp+cellphone.substring(cellphone.length() - 4);
		
		System.out.println(cardId);
		
		
	}
	
	public static String creatRandom(int len) {
		Random r = new Random();
		StringBuilder rs = new StringBuilder();
		for (int i = 0; i < len; i++) {
			rs.append(r.nextInt(10));
		}
		return rs.toString();
	}
	
	 public static String createValiCode() {
		    
	        String valiCode = "";
	        // 生成随机验证码
	        for (int i = 0; i <= 100; i++) {
	            String sources = "0123456789"; 
	            Random rand = new Random();
	            StringBuffer flag = new StringBuffer();
	            for (int j = 0; j < 6; j++) {
	                flag.append(sources.charAt(rand.nextInt(9)) + "").toString();
	            }
	            valiCode = flag.toString();
	        }
	         return valiCode;
	    }
	
	

}
