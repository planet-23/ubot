package com.uuz.ubot.service.impl;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.uuz.ubot.holder.RequestCountHolder;
import com.uuz.ubot.service.PictureService;
import com.uuz.ubot.utils.HttpUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * @author hey
 */
@Service
public class PictureServiceImpl implements PictureService {
	private static final Logger logger = LoggerFactory.getLogger(PictureServiceImpl.class);


	@Override
	@Async
	public void sendPictureToGroup(String keyword, Integer groupId) {
		Map<String, Object> map = null;
		//统计总共查询的条数
		HttpGet countReq = new HttpGet("https://www.vilipix.com/api/v1/picture/public?limit=15&tags=" + keyword + "&sort=new&offset=" + RandomUtil.randomInt(100));
		map = HttpUtils.sendRequest(countReq);
		if (map.get("data") != null) {
			JSONObject data = new JSONObject(map.get("data"));
			Integer count = (Integer) data.get("count");
			if (count == null || count == 0) {
				HttpPost httpPost = new HttpPost("http://127.0.0.1:5700/send_group_msg?group_id=" + groupId + "&message=" + "未找到相关图片！");
				HttpUtils.sendRequest(httpPost);
				return;
			}
			if (count > 700){
				count = 700;
			}
			//根据查询的总条数随机查询
			HttpGet httpGet = new HttpGet("https://www.vilipix.com/api/v1/picture/public?limit=15&tags=" + keyword + "&sort=new&offset=" + RandomUtil.randomInt(count + 1));
			map = HttpUtils.sendRequest(httpGet);
			data = new JSONObject(map.get("data"));
			JSONArray objects = JSONUtil.parseArray(data.get("rows"));
			map = new JSONObject(objects.get(RandomUtil.randomInt(objects.size())));
			String originalUrl = (String) map.get("original_url");
			String replace = originalUrl.replace("/regular", "/original").replace("_master1200.", ".");
			//打印图片url
			System.out.println("原始url=============> " + replace);
			HttpGet exist = new HttpGet(replace);
			CloseableHttpResponse res = null;
			try {
				res = HttpUtils.HTTP_CLIENT.execute(exist);
			} catch (IOException e) {
				logger.error("图片请求错误", e);
				System.out.println("图片请求错误");
				return;
			}
			int statusCode = res.getStatusLine().getStatusCode();

			if (statusCode == 404) {
				replace = replace.substring(0, replace.length() - 3) + "png";
				HttpGet exist01 = new HttpGet(replace);
				try {
					res = HttpUtils.HTTP_CLIENT.execute(exist01);
				} catch (IOException e) {
					logger.error("图片请求错误", e);
					System.out.println("图片请求错误");
					return;
				}
				statusCode = res.getStatusLine().getStatusCode();
			}

			if (statusCode == 404) {
				replace = replace.substring(0, replace.length() - 3) + "PNG";
				HttpGet exist02 = new HttpGet(replace);
				try {
					res = HttpUtils.HTTP_CLIENT.execute(exist02);
				} catch (IOException e) {
					logger.error("图片请求错误", e);
					System.out.println("图片请求错误");
					return;
				}
				statusCode = res.getStatusLine().getStatusCode();
			}

			if (statusCode == 404) {
				replace = replace.substring(0, replace.length() - 3) + "jpg";
				HttpGet exist03 = new HttpGet(replace);
				try {
					res = HttpUtils.HTTP_CLIENT.execute(exist03);
				} catch (IOException e) {
					logger.error("图片请求错误", e);
					System.out.println("图片请求错误");
					return;
				}
				statusCode = res.getStatusLine().getStatusCode();
			}

			if (statusCode == 404) {
				replace = replace.substring(0, replace.length() - 3) + "JPG";
				HttpGet exist04 = new HttpGet(replace);
				try {
					res = HttpUtils.HTTP_CLIENT.execute(exist04);
				} catch (IOException e) {
					logger.error("图片请求错误", e);
					System.out.println("图片请求错误");
					return;
				}
				statusCode = res.getStatusLine().getStatusCode();
			}

			if (statusCode == 404) {
				replace = replace.substring(0, replace.length() - 3) + "JPEG";
				HttpGet exist05 = new HttpGet(replace);
				try {
					res = HttpUtils.HTTP_CLIENT.execute(exist05);
				} catch (IOException e) {
					logger.error("图片请求错误", e);
					System.out.println("图片请求错误");
					return;
				}
				statusCode = res.getStatusLine().getStatusCode();
			}

			if (statusCode == 404) {
				replace = replace.substring(0, replace.length() - 3) + "jpeg";
				HttpGet exist06 = new HttpGet(replace);
				try {
					res = HttpUtils.HTTP_CLIENT.execute(exist06);
				} catch (IOException e) {
					logger.error("图片请求错误", e);
					System.out.println("图片请求错误");
					return;
				}
				statusCode = res.getStatusLine().getStatusCode();
			}

			System.out.println("发送url=============> " + replace);
			System.out.println(statusCode);
			HttpPost httpPost = new HttpPost("http://127.0.0.1:5700/send_group_msg?group_id=" + groupId + "&message=" + "[CQ:image,file=" + replace + "]");
			HttpUtils.sendRequest(httpPost);
		}
	}

	@Override
	@Async
	public void sendAIPictureToGroup(String keyword, Integer groupId) {
		String msg = "画图失败！";
		try {
			if (StrUtil.isBlank(keyword)){
				msg = "关键字不能为空！";
				throw new RuntimeException();
			}
			HttpPost post = new HttpPost("http://127.0.0.1:6969/generate-stream");
			post.setHeader("content-type","application/json");
			String filePath = "";
			String seed = RandomUtil.randomInt(10000000)+"";
			String steps = RandomUtil.randomInt(30,50)+"";
			try {
				post.setEntity(new StringEntity("""
				{
				    "prompt": "masterpiece, best quality,%s",
				    "width": 512,
				    "height": 768,
				    "scale": 12,
				    "sampler": "k_euler_ancestral",
				    "steps": %s,
				    "seed": %s,
				    "n_samples": 1,
				    "ucPreset": 0,
				    "uc": "lowres, bad anatomy, bad hands, text, error, missing fingers, extra digit, fewer digits, cropped, worst quality, low quality, normal quality, jpeg artifacts, signature, watermark, username, blurry"
				}
				""".formatted(keyword,steps,seed)));
				CloseableHttpResponse execute = HttpUtils.HTTP_CLIENT.execute(post);
				HttpEntity entity = execute.getEntity();
				String en = EntityUtils.toString(entity);
				String substring = en.substring(en.indexOf("data:") + 5);
				filePath = "E:/tmp/novelai/"+System.currentTimeMillis()+RandomUtil.randomInt(100)+".png";
				try(FileOutputStream fileOutputStream = new FileOutputStream(new File(filePath))){
					fileOutputStream.write(Base64Decoder.decode(substring.getBytes()));
				}catch (Exception e){
					e.printStackTrace();
				}finally {
					RequestCountHolder.AI_FLAG = false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				RequestCountHolder.AI_FLAG = false;
			}
			msg = "[CQ:image,file=file:///" + filePath + "]";
			throw new RuntimeException();
		}catch (Exception e){
			HttpPost httpPost = new HttpPost("http://127.0.0.1:5700/send_group_msg?group_id=" + groupId + "&message=" + msg);
			HttpUtils.sendRequest(httpPost);
		}finally {
			RequestCountHolder.AI_FLAG = false;
		}
	}

	public static void main(String[] args) {
		System.out.println("string".substring("string".indexOf("in")+2));
	}
}
