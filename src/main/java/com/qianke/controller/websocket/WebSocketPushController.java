/**
 * 
 */
package com.qianke.controller.websocket;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.qianke.service.WebSocketService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Copyright © 2019 易联云计算有限公司. All rights reserved.
 * @Description: 
 * @author: jin.hui.bin 
 * @date: 
 * @version: V1.0
 */
@RestController
@RequestMapping("ws")
@Slf4j

public class WebSocketPushController {
	
	@Resource
	WebSocketService webSocketService;
	
	/**
	   * 页面请求
	   * @param cid
	   * @return
	   */
	  @GetMapping("/socket/{cid}")
	  public ModelAndView socket(@PathVariable String cid) {
		  log.info("websocket连接进入");
	    ModelAndView mav=new ModelAndView("/socket");
	    mav.addObject("cid", cid);
	    return mav;
	  }
	  /**
	   * 推送数据接口    //TODO:用户下单之后，减库存，并调用该接口，
	   * @param cid
	   * @param message
	   * @return
	   */
	  @ResponseBody
	  @RequestMapping("/socket/push/{cid}")
	  public String pushToWeb(@PathVariable String cid,String message) {
		  log.info(cid+":【socket/push】调用开始:"+message);
		  //调用实例，1.引入该service，调用send方法，2.参数为消息和商户窗口id
		  //TODO:业务处理，如果service没有返回success，则进行其他平台的补发内容【在您未打开管理端时，有一个订单未处理，订单信息为】
		  return webSocketService.sendWsMsg(message, cid);
		  
	  }
	
	
	
	
}
