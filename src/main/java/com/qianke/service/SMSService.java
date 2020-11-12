/**
 * 
 */
package com.qianke.service;

import java.util.Map;

import javax.annotation.Resource;

import com.qianke.dao.TenantDao;
import com.qianke.exception.BaseException;
import com.qianke.model.TencentConfigDTO;
import org.springframework.stereotype.Service;

import com.qianke.util.CodeUtil;
import com.qianke.util.SMSUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Copyright © 2019 易联云计算有限公司. All rights reserved.
 * 
 * @Description: 短信服务类
 * @author: jin.hui.bin
 * @date:
 * @version: V1.0
 */
@Service
@Slf4j
public class SMSService {
    @Resource
    TenantDao tenantDao;
	@Resource
	SMSUtil smsUtil;
	@Resource
	CacheService cacheService;

	/**
	 * @Description 根据手机和业务信息发送验证码短信
	 * @param mobile ，handle
	 * @return
	 */
	public Map<String, Object> send(String mobile, String handle) {
		String code = CodeUtil.creatRandom(4);
		log.info(mobile + "系统产生的验证码是:" + code);
		Map<String, Object> map = smsUtil.send(mobile, handle, code);
		cacheService.setCache("CODE_" + mobile, code, "1800");// 验证码缓存半个小时，新的可以替换旧的
		return map;
	}

	/**
	 * @Description 验证码校验
	 * @param mobile inputCode(已经是非空，如果空不会请求该接口)
	 * @return
	 */
	public String check(String mobile, String inputCode) {
		Object sysCode = cacheService.getCache("CODE_" + mobile);
		log.info(mobile + "校验时从缓存获取的验证码" + sysCode);
		if (sysCode != null && sysCode != "") {
			if (inputCode.equals(sysCode)) {
				return "success";
			} else {
				return "fail";
			}
		} else {
			return "sysCode is null";
		}
	}


	/**
	 * @Description 查询租户对应的短信参数配置
	 * @param mobile ，handle
	 * @return
	 */
	public TencentConfigDTO getSMSConfig(String tenantCode) {
        TencentConfigDTO tencentConfigDTO=tenantDao.getSMSConfigByCode(tenantCode);
        if(tencentConfigDTO!=null){
            return tencentConfigDTO;
        }
        throw new BaseException("00002","当前租户短信配置为空");
	}
}
