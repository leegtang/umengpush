package com.lcg.umpush;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.MediaType;
import okhttp3.OkHttpClient.Builder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtil {
	public static final MediaType JSON = MediaType
			.parse("application/json; charset=utf-8");
	
	static Builder builder=new OkHttpClient.Builder().addInterceptor(new LoggingInterceptor());
	static OkHttpClient client;
	 
	
	static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() { 
		@Override
		public boolean verify(String hostname, SSLSession session) { 
			return true;
		}
     };
      
     private static SSLSocketFactory createSSLSocketFactory() {

         SSLSocketFactory sSLSocketFactory = null;

         try {
             SSLContext sc = SSLContext.getInstance("TLS");
             sc.init(null, new TrustManager[]{new TrustAllManager()},
                     new SecureRandom());
             sSLSocketFactory = sc.getSocketFactory();
         } catch (Exception e) {
         }

         return sSLSocketFactory;
     }

     private static class TrustAllManager implements X509TrustManager {
         @Override
         public void checkClientTrusted(X509Certificate[] chain, String authType)
                 throws CertificateException {
         }

         @Override
         public void checkServerTrusted(X509Certificate[] chain, String authType)

                 throws CertificateException {
         }

         @Override
         public X509Certificate[] getAcceptedIssuers() {
        	 return new X509Certificate[0];
         }
     }
     
	public static String post(String url, String json) { 
		System.out.println(json);
		if(url.startsWith("https")){ 
//			client = new OkHttpClient.Builder().sslSocketFactory(createSSLSocketFactory()).hostnameVerifier(DO_NOT_VERIFY)
//			        .build(); 
			
			client = builder
					.sslSocketFactory(createSSLSocketFactory(),new TrustAllManager())
					.hostnameVerifier(DO_NOT_VERIFY)
			        .build(); 
		}else{
			client = builder.build();
		}
		
		try {  
			RequestBody body = RequestBody.create(JSON, json);
			Request request = new Request.Builder().url(url).post(body).build();
			Response response = client.newCall(request).execute();
			return response.body().string();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String get(String url, String json) {  
		if(url.startsWith("https")){ 
			client = builder
					.sslSocketFactory(createSSLSocketFactory(),new TrustAllManager())
					.hostnameVerifier(DO_NOT_VERIFY)
			        .build(); 
		}else{
			client = builder.build();
		}
		
		try {   
			Request request = new Request.Builder().url(url).build();
			Response response = client.newCall(request).execute();
			return response.body().string();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	 

    
    
	
	
	public static void main(String[] args) {
//		String url="https://114.55.36.16:8090/CreditFunc";
//		String json=String.format("{\"loginName\":\"%s\",\"pwd\":\"%s\",\"serviceName\":\"%s\","
//				+ "\"param\":{\"idCard\":\"%s\",\"name\":\"%s\"}"
//				+ "}","test", "123456", "IDNameCheck", "320743199101175520", "李丙付");
//		
//		String res=post(url, json);
//		System.out.println(res);
		 
	}
}
