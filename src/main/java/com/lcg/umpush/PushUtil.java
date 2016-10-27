package com.lcg.umpush;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
/**
 * 
 *<dl>
 *<dt>类名：PushUtil.java</dt>
 *<dd>描述: 友盟推送工具类 </dd> 
 *<dd>创建时间：2016年10月27日 下午3:32:48</dd>
 *<dd>创建人： Caigen</dd>
 *</dl>
 */
public class PushUtil {
	private String androidAppkey = "xxx";
	private String androidAppMasterSecret = "xxx";
	private String iosAppkey = "xxxxxxxxx";
	private String iosAppMasterSecret = "xxxxxxxxxxxxxxxxxxxxxxxxx";
	private String serverURL="http://msg.umeng.com/api/send";  
	private String queryURL="http://msg.umeng.com/api/status";  
	// 是否开启生产模式，false 非生产模式时需将设备device_id加入umeng系统中
	private String production_mode="false";
	
	public PushUtil(){} 
	
	public PushUtil(String androidAppkey, String androidAppMasterSecret,
			String iosAppkey, String iosAppMasterSecret, String production_mode) {
		super();
		this.androidAppkey = androidAppkey;
		this.androidAppMasterSecret = androidAppMasterSecret;
		this.iosAppkey = iosAppkey;
		this.iosAppMasterSecret = iosAppMasterSecret;
		this.production_mode = production_mode;
	} 

	public String getAndroidAppkey() {
		return androidAppkey;
	}

	public void setAndroidAppkey(String androidAppkey) {
		this.androidAppkey = androidAppkey;
	}

	public String getAndroidAppMasterSecret() {
		return androidAppMasterSecret;
	}

	public void setAndroidAppMasterSecret(String androidAppMasterSecret) {
		this.androidAppMasterSecret = androidAppMasterSecret;
	}

	public String getIosAppkey() {
		return iosAppkey;
	}

	public void setIosAppkey(String iosAppkey) {
		this.iosAppkey = iosAppkey;
	}

	public String getIosAppMasterSecret() {
		return iosAppMasterSecret;
	}

	public void setIosAppMasterSecret(String iosAppMasterSecret) {
		this.iosAppMasterSecret = iosAppMasterSecret;
	}

	public String getProduction_mode() {
		return production_mode;
	}

	public void setProduction_mode(String production_mode) {
		this.production_mode = production_mode;
	}

	
	
	/**
	 * 根据alias发送
	 * @param aliasType
	 * @param alias
	 * @param dtype
	 * @param extra
	 * @param title
	 * @param text
	 */
	public void customCast(String aliasType, String alias, String dtype,JSONObject extra, String title, String text){
		customCastAndroid(aliasType, alias, dtype, extra, title, text);
		customCastIOS(aliasType, alias, dtype, extra, title, text);
		 
	}
	
	/**
	 * 单播(安卓&苹果设备)
	 * @param appkey
	 * @param tokens
	 * @param dtype
	 * @param extra
	 * @param title
	 * @param text
	 * @param masterSecret
	 */
	public void uniCast(String tokens, String dtype,JSONObject extra, String title, String text){
		uniCastAndroid(tokens, dtype, extra, title, text);
		uniCastIOS(tokens, dtype, extra, title, text);
	}
	
	
	
	/**
	 * 广播
	 * @param dtype
	 * @param extra
	 * @param title
	 * @param text
	 */
	public void broadCast(String dtype,JSONObject extra, String title, String text){
		broadCastAndroid(dtype, title, text, extra);
		broadCastIOS(dtype, title, text, extra);
		 
	}
	
	private String getSign(String url, String postBody , String masterSecret){
		try {
			String sign = DigestUtils.md5Hex(("POST" + url + postBody + masterSecret).getBytes("utf8"));
	       	return sign;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return null;
	}
	
	/**
	 * 根据alias发送到安卓设备
	 * @param aliasType
	 * @param alias
	 * @param dtype
	 * @param extra
	 * @param title
	 * @param text
	 */
	private void customCastAndroid(String aliasType, String alias, String dtype,JSONObject extra, String title, String text){
		JSONObject json=new JSONObject(); 
		String timestamp = Integer.toString((int)(System.currentTimeMillis() / 1000));
		json.put("timestamp", timestamp);
		json.put("production_mode", production_mode);
		json.put("appkey", androidAppkey); 		
//		json.put("timestamp", Calendar.getInstance().getTimeInMillis());
		json.put("type", "customizedcast");
		json.put("alias_type", aliasType);
		json.put("alias", alias);
		
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
		nowTime.add(Calendar.MINUTE, 1);
		System.out.println(sdf.format(nowTime.getTime()));
		  
		JSONObject policy=new JSONObject();
		//policy.put("start_time", ""	);
		policy.put("expire_time", sdf.format(nowTime.getTime()));
		policy.put("out_biz_no", sdf2.format(nowTime.getTime()));
		
		json.put("policy", policy.toString()); 
		
		
//		System.out.println(json.toString());
		
		String sign=getSign(serverURL, json.toString(), androidAppMasterSecret);
		String url=serverURL+"?sign="+sign;
		String res=HttpUtil.post(url, json.toString());
		System.out.println(res);
		
	}
	
	/**
	 * 根据alias发送到苹果设备
	 * @param aliasType
	 * @param alias
	 * @param dtype
	 * @param extra
	 * @param title
	 * @param text
	 */
	private void customCastIOS(String aliasType, String alias, String dtype,JSONObject extra, String title, String text){
		JSONObject json=new JSONObject(); 
		String timestamp = Integer.toString((int)(System.currentTimeMillis() / 1000));
		json.put("timestamp", timestamp);
		json.put("production_mode", production_mode);
		json.put("appkey", iosAppkey); 		
//		json.put("timestamp", Calendar.getInstance().getTimeInMillis());
		json.put("type", "customizedcast");
		json.put("alias_type", aliasType);
		json.put("alias", alias);
		
		JSONObject ply=new JSONObject();
		ply.put("display_type", dtype);
		 
		if(extra!=null){
			// 用extra扩展
		}else{
			extra=new JSONObject();
		}
		
		
		
		JSONObject body=new JSONObject();
		
		body.put("alert", text);  
		
		extra.put("aps", body.toString());
		
		json.put("payload", extra.toString());

		  
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
//		Calendar nowTime = Calendar.getInstance();
//		nowTime.add(Calendar.MINUTE, 1);
//		System.out.println(sdf.format(nowTime.getTime()));
//		  
//		JSONObject policy=new JSONObject();
//		//policy.put("start_time", ""	);
//		policy.put("expire_time", sdf.format(nowTime.getTime()));
//		policy.put("out_biz_no", sdf2.format(nowTime.getTime()));
//		
//		json.put("policy", policy.toString()); 
		
		
//		System.out.println(json.toString());
		
		String sign=getSign(serverURL, json.toString(), iosAppMasterSecret);
		String url=serverURL+"?sign="+sign;
		String res=HttpUtil.post(url, json.toString());
		System.out.println(res);
		
	}
	
	/**
	 * 安卓设备单播
	 * @param appkey
	 * @param tokens
	 * @param dtype
	 * @param extra
	 * @param title
	 * @param text
	 * @param masterSecret
	 */
	private void uniCastAndroid(String tokens, String dtype,JSONObject extra, String title, String text){
		JSONObject json=new JSONObject(); 
		String timestamp = Integer.toString((int)(System.currentTimeMillis() / 1000));
		json.put("timestamp", timestamp);
		json.put("production_mode", production_mode);
		json.put("appkey", androidAppkey); 		
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
		nowTime.add(Calendar.MINUTE, 1);
		System.out.println(sdf.format(nowTime.getTime()));
		  
		JSONObject policy=new JSONObject();
		//policy.put("start_time", ""	);
		policy.put("expire_time", sdf.format(nowTime.getTime()));
		policy.put("out_biz_no", sdf2.format(nowTime.getTime()));
		
		json.put("policy", policy.toString()); 
		
		
//		System.out.println(json.toString());
		
		String sign=getSign(serverURL, json.toString(), androidAppMasterSecret);
		String url=serverURL+"?sign="+sign;
		String res=HttpUtil.post(url, json.toString());
		System.out.println(res);
		
	}
	
	/**
	 * 苹果设备单播
	 * @param appkey
	 * @param tokens
	 * @param dtype
	 * @param extra
	 * @param title
	 * @param text
	 * @param masterSecret
	 */
	private void uniCastIOS(String tokens, String dtype,JSONObject extra, String title, String text){
		JSONObject json=new JSONObject(); 
		String timestamp = Integer.toString((int)(System.currentTimeMillis() / 1000));
		json.put("timestamp", timestamp);
		json.put("production_mode", production_mode);
		json.put("appkey", iosAppkey); 		
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
		nowTime.add(Calendar.MINUTE, 1);
		System.out.println(sdf.format(nowTime.getTime()));
		  
		JSONObject policy=new JSONObject();
		//policy.put("start_time", ""	);
		policy.put("expire_time", sdf.format(nowTime.getTime()));
		policy.put("out_biz_no", sdf2.format(nowTime.getTime()));
		
		json.put("policy", policy.toString()); 
		
		
//		System.out.println(json.toString());
		
		String sign=getSign(serverURL, json.toString(), iosAppMasterSecret);
		String url=serverURL+"?sign="+sign;
		String res=HttpUtil.post(url, json.toString());
		System.out.println(res);
		
	}
	
	
	
	/**
	 * 广播给安卓设备
	 * @param dtype
	 * @param title
	 * @param text
	 * @param extra
	 */
	private void broadCastAndroid(String dtype, String title, String text, JSONObject extra){
		JSONObject json=new JSONObject();
		String timestamp = Integer.toString((int)(System.currentTimeMillis() / 1000));
		json.put("timestamp", timestamp);
		json.put("production_mode", production_mode);
		json.put("appkey", androidAppkey); 
		
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
		
		String sign=getSign(serverURL, json.toString(), androidAppMasterSecret);
		String url=serverURL+"?sign="+sign;
		
		String res=HttpUtil.post(url, json.toString());
		System.out.println(res);
		
	}
	
	/**
	 * 广播给苹果设备
	 * @param dtype
	 * @param title
	 * @param text
	 * @param extra
	 */
	private void broadCastIOS(String dtype, String title, String text, JSONObject extra){
		JSONObject json=new JSONObject();
		String timestamp = Integer.toString((int)(System.currentTimeMillis() / 1000));
		json.put("timestamp", timestamp);
		json.put("production_mode", production_mode);
		json.put("appkey", iosAppkey); 
		
//		json.put("timestamp", Calendar.getInstance().getTimeInMillis()); 
		 
		
		if(extra!=null){
			
		}else{
			extra=new JSONObject();
		}
		
		JSONObject body=new JSONObject(); 
		body.put("alert", text);  
		
		extra.put("aps", body.toString());
		
		json.put("payload", extra.toString());
		
		
		
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
//		Calendar nowTime = Calendar.getInstance();
//		nowTime.add(Calendar.MINUTE, 5);
//		System.out.println(sdf.format(nowTime.getTime()));
//		  
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
		
		String sign=getSign(serverURL, json.toString(), iosAppMasterSecret);
		String url=serverURL+"?sign="+sign;
		
		String res=HttpUtil.post(url, json.toString());
		System.out.println(res);
		
	}
	
	/**
	 * 状态查询
	 * @param appkey
	 * @param taskId
	 * @param masterSecret
	 */
	public void query(String appkey, String taskId, String masterSecret){
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
		
//		PushUtil util=new PushUtil();
		
//		{"ret":"SUCCESS","data":{"task_id":"us36163147755844832201"}}
		
		
		String androidAppkey="x";
		String androidAppMasterSecret="x";
		String iosAppkey="x";
		String iosAppMasterSecret="";
			
		PushUtil util=new PushUtil(androidAppkey, androidAppMasterSecret, iosAppkey, iosAppMasterSecret, "false");
		
		String token="AqPNzyOOATn4XefAdGOM9beQy0bqiJrPE5iIW6OpiKij";
		String title="中文广播test";
		String msg="广播test msg";
		JSONObject extra=new JSONObject();
		extra.put("pos", "a2222222");
//		util.uniCast( token, "notification", extra, title, msg); 
		
		
		String aliasType="U_I_ID";
		String alias="20161024790441195288395776";
//		util.customCast(aliasType, alias, "notification", extra, title, msg);
		
		util.broadCast("notification", extra, title, msg);
		// 	{"ret":"SUCCESS","data":{"task_id":"us74882147754655708701","thirdparty_id":"20161027134057"}}
		String taskId="us74882147754655708701";
//		query(androidAppkey, taskId, androidAppMasterSecret);
	}
}
