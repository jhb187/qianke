/**
 * 
 */
package com.qianke.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * Copyright © 2019 . All rights reserved.
 * 
 * @Description:
 * @author: jin.hui.bin
 * @date:
 * @version: V1.0
 */
public class DateUtil {
	public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public static String now() {
		return LocalDateTime.now().format(dateTimeFormatter);
	}

	public static String nextTime() {
		SimpleDateFormat FORMAT2 = new SimpleDateFormat("HH:mm");
		Calendar can = Calendar.getInstance();
		can.add(Calendar.MINUTE, +60);
		return FORMAT2.format(can.getTime());

	}

	

}
