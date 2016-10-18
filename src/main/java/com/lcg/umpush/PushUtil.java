package com.lcg.umpush;

import java.util.Calendar;

import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;

public class PushUtil {
	private static String androidAppkey = "xxxxxxxxxx";
	private static String androidAppMasterSecret = "xxxxxxxxxxxxxxxxxxxx";
	private static String iosAppkey = "xxxxxxxxx";
	private static String iosAppMasterSecret = "xxxxxxxxxxxxxxxxxxxxxxxxx";
	private static String serverURL="http://msg.umeng.com/api/send";  
	
	public static String getSign(String url, String postBody , String masterSecret){
		try {
			String sign = DigestUtils.md5Hex(("POST" + url + postBody + masterSecret).getBytes("utf8"));
	       	return sign;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return null;
	}
	
	public static void broadCast(String appkey, String type, String tokens, String dtype, String title, String text, String masterSecret){
		JSONObject json=new JSONObject();
		json.put("appkey", appkey);
		json.put("timestamp", Calendar.getInstance().getTimeInMillis() );
		json.put("type", type);
		json.put("device_tokens", tokens);
		
		JSONObject ply=new JSONObject();
		ply.put("display_type", dtype);
		
		
		JSONObject body=new JSONObject();
		body.put("ticker", title);
		body.put("title", title);
		body.put("text", text);
		body.put("after_open", "go_app");
		
		ply.put("body", body.toString());
		
		json.put("payload", ply.toString());
		
		json.put("production_mode", "false");
		
		System.out.println(json.toString());
		
		String sign=getSign(serverURL, json.toString(), masterSecret);
		String url=serverURL+"?sign="+sign;
		String res=HttpUtil.post(url, json.toString());
		System.out.println(res);
		
	}
	
	public static void main(String[] args) {
		broadCast(androidAppkey, "unicast", "AnnqhE4Bu4ClBBVhNOyTTVZcyy8Flr_dQJbTEF01JPtY", "notification", "通知", "消息测试", androidAppMasterSecret); 
	
		
	}
}
