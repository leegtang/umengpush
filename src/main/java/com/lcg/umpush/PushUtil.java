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
	private static String queryURL="http://msg.umeng.com/api/status";   
	
	private static String production_mode="true";
	
	public static String getSign(String url, String postBody , String masterSecret){
		try {
			String sign = DigestUtils.md5Hex(("POST" + url + postBody + masterSecret).getBytes("utf8"));
	       	return sign;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return null;
	}
	
	public static void uniCast(String appkey, String tokens, String dtype,JSONObject extra, String title, String text, String masterSecret){
		JSONObject json=new JSONObject();
		
		String timestamp = Integer.toString((int)(System.currentTimeMillis() / 1000));
		json.put("timestamp", timestamp);
		json.put("production_mode", production_mode);
		json.put("appkey", appkey); 
		
//		json.put("timestamp", Calendar.getInstance().getTimeInMillis());
		json.put("type", "unicast");
		json.put("device_tokens", tokens);
		
		JSONObject ply=new JSONObject();
		ply.put("display_type", dtype);
		if(extra!=null){
			ply.put("extra", extra.toString());
		}
		
		
		JSONObject body=new JSONObject();
		
		body.put("title", title);
		body.put("ticker", text+"ticker");
//		body.put("builder_id", 1);
		body.put("text", text);
		body.put("after_open", "go_activity");
		
		ply.put("body", body.toString());
		
		json.put("payload", ply.toString());

		  
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar nowTime = Calendar.getInstance();
		nowTime.add(Calendar.MINUTE, 5);
		System.out.println(sdf.format(nowTime.getTime()));
		  
		JSONObject policy=new JSONObject();
		//policy.put("start_time", ""	);
		policy.put("expire_time", sdf.format(nowTime.getTime()));
		policy.put("out_biz_no", sdf2.format(nowTime.getTime()));
		
		json.put("policy", policy.toString());
		
		
		
//		System.out.println(json.toString());
		
		String sign=getSign(serverURL, json.toString(), masterSecret);
		String url=serverURL+"?sign="+sign;
		String res=HttpUtil.post(url, json.toString());
		System.out.println(res);
		
	}
	
	public static void broadCast(String appkey, String dtype, String title, String text, JSONObject extra, String masterSecret){
		JSONObject json=new JSONObject();
		String timestamp = Integer.toString((int)(System.currentTimeMillis() / 1000));
		json.put("timestamp", timestamp);
		json.put("production_mode", production_mode);
		json.put("appkey", appkey); 
		
//		json.put("timestamp", Calendar.getInstance().getTimeInMillis());
		
		
		JSONObject ply=new JSONObject();
		
		
		
		
		JSONObject body=new JSONObject();
		body.put("text", text);
		body.put("title", title);
		body.put("ticker", text+"ticker");
//		body.put("builder_id", 1);
		
		body.put("after_open", "go_app");
		
		ply.put("body", body.toString());
		
		ply.put("display_type", dtype);
		
		if(extra!=null){
			ply.put("extra", extra.toString());
		}
		
		json.put("payload", ply.toString());
		
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar nowTime = Calendar.getInstance();
		nowTime.add(Calendar.MINUTE, 5);
		System.out.println(sdf.format(nowTime.getTime()));
		  
//		JSONObject policy=new JSONObject();
//		//policy.put("start_time", ""	);
//		policy.put("expire_time", sdf.format(nowTime.getTime()));
//		policy.put("out_biz_no", sdf2.format(nowTime.getTime()));
//		
//		json.put("policy", policy.toString());
		
		
		
		
		
		
		json.put("type", "broadcast"); 
		
//		json.put("description", policy.get("out_biz_no"));
//		json.put("thirdparty_id", policy.get("out_biz_no"));
//		System.out.println(json.toString());
		
		String sign=getSign(serverURL, json.toString(), masterSecret);
		String url=serverURL+"?sign="+sign;
		
		String res=HttpUtil.post(url, json.toString());
		System.out.println(res);
		
	}
	
	
	public static void query(String appkey, String taskId, String masterSecret){
		JSONObject json=new JSONObject();
		json.put("appkey", appkey); 
		json.put("timestamp", Calendar.getInstance().getTimeInMillis());
		json.put("task_id", taskId); 
		
		System.out.println(json.toString());
		
		String sign=getSign(queryURL, json.toString(), masterSecret);
		String url=queryURL+"?sign="+sign;
		String res=HttpUtil.post(url, json.toString());
		System.out.println(res);
		
	}
	
	public static void main(String[] args) {
		String token="AqPNzyOOATn4XefAdGOM9beQy0bqiJrPE5iIW6OpiKij";
		String title="中文标题a";
		String msg="android test msg";
		JSONObject extra=new JSONObject();
		//extra.put("pos", "a2222222");
		uniCast(androidAppkey, token, "notification", extra, title, msg, androidAppMasterSecret); 
		
//		broadCast(androidAppkey,"notification", title, msg, extra, androidAppMasterSecret); 
		
		// 	{"ret":"SUCCESS","data":{"task_id":"us74882147754655708701","thirdparty_id":"20161027134057"}}
		String taskId="us74882147754655708701";
//		query(androidAppkey, taskId, androidAppMasterSecret);
	}
	
}
