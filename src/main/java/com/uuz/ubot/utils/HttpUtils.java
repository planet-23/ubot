package com.uuz.ubot.utils;

import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * @author hey
 */
public class HttpUtils {

	public static final CloseableHttpClient HTTP_CLIENT = HttpClients.createDefault();

	public static Map<String, Object> sendRequest(HttpUriRequest request) {
		try {
			CloseableHttpResponse execute = HTTP_CLIENT.execute(request);
			HttpEntity entity = execute.getEntity();
			byte[] bytes = EntityUtils.toByteArray(entity);
			return JSONUtil.parseObj(bytes);
		} catch (Exception e) {
			throw new RuntimeException("发送失败");
		}
	}

	public static String getData(HttpServletRequest req){
		try{
			// 获取post参数
			StringBuffer sb = new StringBuffer();
			InputStream is = req.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String s = "";
			while ((s = br.readLine()) != null) {
				sb.append(s);
			}
			return sb.toString();
		}catch (Exception e){
			return "";
		}
	}
}
