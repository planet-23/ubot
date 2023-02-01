package com.uuz.ubot.utils;

import cn.hutool.core.util.RandomUtil;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.apache.http.client.methods.HttpGet;
import org.w3c.dom.Document;
import org.xhtmlrenderer.context.AWTFontResolver;
import org.xhtmlrenderer.swing.Java2DRenderer;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.awt.Font.TRUETYPE_FONT;

public class FreemarkerUtils {

	public String getTemplate(String template, Map<String, Object> map) throws IOException, TemplateException {
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_25);
		cfg.setTemplateLoader(new ClassTemplateLoader(
				FreemarkerUtils.class.getClass().getClassLoader(), "/templates/static/html"));
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		cfg.setLogTemplateExceptions(false);
		Template temp = cfg.getTemplate(template, "UTF-8");
		StringWriter stringWriter = new StringWriter();
		temp.process(map, stringWriter);
		stringWriter.flush();
		stringWriter.close();
		String resutl = stringWriter.getBuffer().toString();
		return resutl;
	}

	public String turnImage(String template, Map<String, Object> map) throws Exception {

		String html = getTemplate(template, map);
		byte[] bytes = html.getBytes("UTF-8");

		ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(bin);

		//加载自定义字体，解决生成图片title处汉字展示不正常问题
//		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("font/simsun.ttf");
//		Font font = Font.createFont(TRUETYPE_FONT, inputStream);
		AWTFontResolver awtFontResolver = new AWTFontResolver();
//		awtFontResolver.setFontMapping("simsun", font);

		Java2DRenderer renderer = new Java2DRenderer(document, 750, 1286);
		renderer.getSharedContext().setFontResolver(awtFontResolver);

		BufferedImage img = renderer.getImage();
		// 转成流上传至服务器
		ByteArrayOutputStream dataOutputStream = new ByteArrayOutputStream();
		String filePath = "E:/tmp/genshin/"+System.currentTimeMillis() + RandomUtil.randomInt(1000)+".png";
		ImageIO.write(img, "png", new File(filePath));
		return filePath;
	}

	public static void main(String[] args) throws Exception {
//		HashMap<String, Object> map = new HashMap<>();
//		HashMap<String, Object> a = new HashMap<>();
//		a.put("num","");
//		a.put("name","");
//		
//		map.put("resPath","");
//		map.put("imgBasePath","E:/开源项目/ubot/src/main/resources/templates/static");
//		map.put("uid","");
//		map.put("time","");
//		map.put("max_floor","");
//		map.put("total_star","");
//		map.put("list",new ArrayList<Object>());
//		map.put("total_battle_times","");
//		map.put("defeat",a);
//		map.put("take_damage",a);
//		map.put("normal_skill",a);
//		map.put("damage",a);
//		map.put("energy_skill",a);
//		new FreemarkerUtils().turnImage("/abyss/abyss.ftl", map);
		Map<String, Object> map = HttpUtils.sendRequest(new HttpGet("https://www.pixiv.net/ajax/search/illustrations/%E5%8E%9F%E7%A5%9E?word=%E5%8E%9F%E7%A5%9E&order=date_d&mode=all&p=1&s_mode=s_tag_full&type=illust_and_ugoira&lang=zh&version=c21c3e4b1b88511ca5abf2129cec4c5f244af4bc"));
		return;
	}

}