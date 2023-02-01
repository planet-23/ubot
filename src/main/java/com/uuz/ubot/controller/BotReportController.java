package com.uuz.ubot.controller;

import cn.hutool.json.JSONObject;
import com.uuz.ubot.holder.RequestCountHolder;
import com.uuz.ubot.service.PictureService;
import com.uuz.ubot.utils.HttpUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.http.client.methods.HttpPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author hey
 */
@RestController
@RequestMapping("/report")
public class BotReportController {
	
	@Resource
	private PictureService pictureService;
	
	private static final Logger logger = LoggerFactory.getLogger(BotReportController.class);

	/**
	 * 上传值
	 * {
	 *     "post_type": "message",
	 *     "message_type": "group",
	 *     "time": 1675046378,
	 *     "self_id": 1453775757,
	 *     "sub_type": "normal",
	 *     "raw_message": "--z",
	 *     "sender": {
	 *         "age": 0,
	 *         "area": "",
	 *         "card": "",
	 *         "level": "",
	 *         "nickname": "hey",
	 *         "role": "owner",
	 *         "sex": "unknown",
	 *         "title": "",
	 *         "user_id": 2313510316
	 *     },
	 *     "user_id": 2313510316,
	 *     "message_id": -909499835,
	 *     "anonymous": null,
	 *     "group_id": 527073430,
	 *     "message": "--z",
	 *     "message_seq": 1433,
	 *     "font": 0
	 * }
	 * @param request
	 */
	@PostMapping("/handler")
	public void messageHandler(HttpServletRequest request){
		Map<String, Object> reportData = new JSONObject(HttpUtils.getData(request));
		String message = (String) reportData.get("message");
		Integer groupId = (Integer) reportData.get("group_id");
		if (groupId == null){
			return;
		}
		if (message != null){
			if (message.startsWith("-pic ")){
				message = message.replace("-pic ", "");
				pictureService.sendPictureToGroup(message,groupId);
			}
			else if (message.startsWith("-AIpic ")){
				if (!RequestCountHolder.AI_FLAG){
					RequestCountHolder.AI_FLAG = true;
					message = message.replace("-AIpic ", "");
					pictureService.sendAIPictureToGroup(message,groupId);
					HttpPost httpPost = new HttpPost("http://127.0.0.1:5700/send_group_msg?group_id=" + groupId + "&message=" + "正在画图中(约1分钟)...");
					HttpUtils.sendRequest(httpPost);
				}else {
					HttpPost httpPost = new HttpPost("http://127.0.0.1:5700/send_group_msg?group_id=" + groupId + "&message=" + "当前存在未画完的请求，请稍候重试！");
					HttpUtils.sendRequest(httpPost);
				}
				
			}
		}
		
	}
}
