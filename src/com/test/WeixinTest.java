package com.test;

import com.alibaba.fastjson.JSON;
import com.weixin.util.WeixinUtil;
import net.sf.json.JSONObject;
import org.springframework.web.client.RestTemplate;

/**
 * 菜单创建以及查询
 */
public class WeixinTest {


	private static final String PAY_URL = "";

	public static void main(String[] args) {

		String access_token_url = "http://nwxoa.u-coupon.cn/Weixin/userinfo/access_token.do";
		RestTemplate restTemplate = new RestTemplate();
		String token = JSON.parseObject(restTemplate.getForObject(access_token_url, String.class)).getString("token");

		/**
		 * 创建菜单
		 */
//		String menu = JSONObject.fromObject(WeixinUtil.initMenu3()).toString();
//		/**
//		 * 维护菜单
//		 */
////		String menu = JSONObject.fromObject(WeixinUtil.fixMenu()).toString();
//
//		int result = WeixinUtil.creatMenu(token, menu);
//		if(result == 0){
//			System.out.println("菜单创建成功");
//		}
//		else{
//			System.out.println("错误码是: " + result);
//		}
		
		/**
		 * 查询菜单
		 */

		System.out.println(WeixinUtil.queryMenu(token));
		
		/** 
		 * 设置行业信息 及查询
		 */
//		ModelMessageUtil.setIndustry(token.getToken(), "11", "12");
//		System.out.println(ModelMessageUtil.queryIndustry(token.getToken()));
		
		/**
		 * 支付消息推送
		 */
//		PayService payService = new PayService();
//		String returncode = payService.sendPayMessage("oAC51s6M-XKhjKHbMyApTMB3gm64");
//		System.out.println(returncode);
	}

}
