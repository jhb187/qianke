/**
 *
 */
package com.qianke.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.qianke.controller.base.RespCode;
import com.qianke.model.Product;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lly835.bestpay.model.PayResponse;
import com.qianke.controller.base.BaseResponse;
import com.qianke.model.CardBank;
import com.qianke.model.Order;
import com.qianke.model.WxPayDto;
import com.qianke.service.ProductService;
import com.qianke.service.WXPayService;
import com.qianke.util.DateUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * Copyright © 2019 易联云计算有限公司. All rights reserved.
 *
 * @Description:
 * @author: jin.hui.bin
 * @date:
 * @version: V1.0
 */
@Slf4j
@RestController
@RequestMapping(value = "/pay")
@CrossOrigin
public class WXPayController {

    @Resource
    WXPayService wXPayService;

    @Resource
    ProductService productService;

    /**
     * @param 当前页面的html url
     * @return appId: '', // 必填，公众号的唯一标识 timestamp: , // 必填，生成签名的时间戳 nonceStr: '',
     * // 必填，生成签名的随机串 signature: '',// 必填，签名
     * @throws Exception
     * @throws IOException
     * @Description 生成js-sdk用于签名的Signature
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/union/order")
    public BaseResponse<PayResponse> unionOrder(@RequestBody Map<String, Object> datas) throws Exception {

        log.info("【/union/order调用开始" + datas);
        WxPayDto payParam = new WxPayDto();
        payParam.setMoney(Double.parseDouble(datas.get("price").toString()));
        payParam.setOpenid(datas.get("openId").toString());
        if ("0.00".equals(datas.get("price").toString())) {
            log.info("价格不合法");
            BaseResponse<PayResponse> payresult = new BaseResponse<PayResponse>(RespCode.RESP_CODE_999999);
            return payresult;
        }

        Map<String, Object> user = (Map<String, Object>) datas.get("orderId");
        //TODO:根据user  与orderName处理订单，减库存，通知买家，在支付回执成功的时候


        String timeStamp = Long.toString(new Date().getTime());

        String cellphone = user.get("mobile").toString();
        String orderId = timeStamp + cellphone.substring(cellphone.length() - 4);
        payParam.setOrderId(orderId);
        payParam.setOrderName(datas.get("orderName").toString());
        /**生成订单**/
        Order order = new Order();//,
        order.setContact(user.get("contact").toString());
        order.setMobile(user.get("mobile").toString());
        order.setDelivery_address(user.get("address").toString());
        order.setOrderid(orderId);
        order.setMoney(datas.get("price").toString());
        order.setOpenid(datas.get("openId").toString());
        List<Product> list = JSON.parseArray(datas.get("orderName").toString(), Product.class);
        String infos = "";
        for (Product product : list) {
            if (product.getCount() != 0) {
                infos = infos + product.getName() + "  ×  " + product.getCount() + "<br>";
            }
        }
        order.setProduct_info(infos);
        order.setCreate_time(DateUtil.now());
        order.setTenant_code(datas.get("tenantCode").toString());
        productService.creatOrder(order);
        PayResponse payResponse = wXPayService.wxpay(payParam);
        BaseResponse<PayResponse> payresult = new BaseResponse<PayResponse>(payResponse);
        log.info("【union/order】返回" + payresult);
        return payresult;

    }

    //TODO:根据  与支付订单的  orderId,money(amount)金额，来匹配支付是否完成，匹配与支付订单与支付回调；


    /**
     * @param
     * @return
     * @Description 购买水卡的提单提交接口
     */
    @RequestMapping(value = "/union/card/order")
    public BaseResponse<PayResponse> waterCardOrder(@RequestBody Map<String, Object> datas) throws Exception {

        log.info("【/card/order调用开始" + datas);
        WxPayDto payParam = new WxPayDto();
        payParam.setMoney(Double.parseDouble(datas.get("price").toString()));
        payParam.setOpenid(datas.get("openId").toString());


        Map<String, Object> user = (Map<String, Object>) datas.get("orderId");
        //TODO:根据user  与orderName处理订单，减库存，通知买家，在支付回执成功的时候


        String timeStamp = Long.toString(new Date().getTime());

        String cellphone = user.get("mobile").toString();
        String orderId = "C" + timeStamp + cellphone.substring(cellphone.length() - 4);
        log.info("【card/生成的卡号】{}", orderId);
        payParam.setOrderId(orderId);
        payParam.setOrderName(datas.get("orderName").toString());
        /**生成订单**/
        Order order = new Order();//,
        order.setContact(user.get("contact").toString());
        order.setMobile(user.get("mobile").toString());

        order.setOrderid(orderId);
        order.setMoney(datas.get("price").toString());
        order.setOpenid(datas.get("openId").toString());
        order.setProduct_info(datas.get("orderName").toString());
        order.setCreate_time(DateUtil.now());
        order.setTenant_code(datas.get("tenantCode").toString());
        productService.creatOrder(order);
        PayResponse payResponse = wXPayService.wxpay(payParam);
        BaseResponse<PayResponse> payresult = new BaseResponse<PayResponse>(payResponse);
        log.info("【card/order】返回" + payresult);
        return payresult;

    }


    /**
     * @param TODO:通知商家管理js端，扣减库存，生成订单，这里执行
     * @return
     * @throws Exception
     * @throws IOException
     * @Description wx支付异步回调
     */
    @RequestMapping(value = "/async/notify")
    public String asyncNotify(HttpServletRequest httpServletRequest) throws Exception {

        log.info("【/async/notify调用开始");
        String payresult = wXPayService.asyncNotify(httpServletRequest);
        log.info("【支付回调】返回给微信：" + payresult);
        return payresult;

    }

}
