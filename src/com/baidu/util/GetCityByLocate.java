package com.baidu.util;
import com.weixin.pay.domain.message.Template;
import com.weixin.pay.util.CommonUtils;
import com.weixin.util.WeixinUtil;
import net.sf.json.JSONObject;
import org.springframework.web.client.RestTemplate;

public class GetCityByLocate {
	
	private static final String API_BAIDU_URL = "http://api.map.baidu.com/geocoder/v2/?ak=AK&location=LOCATION&output=json";
	
	private static final String ak = "vH0fPZRoiGefNfrcbBa4rHRZ";

	private static RestTemplate restTemplate = new RestTemplate();
	/**
	 * 后期改成高德地图接口
	 *
	 * 使用 WxPayService中获取citycode的方法
	 * 目前存在问题是 城市列表接口中citycode未改成区号
	 *
	 * 百度地图和高德地图的 逆地址解析接口都有bug
	 * 百度:区号错误
	 * 高德:北京市 city为空 只在省份中显示
	 *
	 * 配额情况:
	 * 已升级成个人开发者 30w/day
	 * @param lng
	 * @param lat
     * @return
     */
	public static String getCityByLoc(String lng, String lat){
		String loc = "";
		loc = lat + "," + lng;
		String url = API_BAIDU_URL.replace("AK", ak).replace("LOCATION", loc);
		String result = restTemplate.getForObject(url, String.class);
		JSONObject data_orignal = JSONObject.fromObject(result);
		JSONObject data_result = data_orignal.getJSONObject("result");
		JSONObject data_addressComponent = data_result.getJSONObject("addressComponent");
		String city = data_addressComponent.getString("city");
		return city;
	}

	public static void main(String[] args) {
		String lng = "104.072845";
		String lat = "30.652312";
		System.out.println(getCityByLoc(lng, lat));
	}
	
}
