package com.uuz.ubot;

import cn.hutool.json.JSONUtil;
import com.uuz.ubot.utils.JsonToEntity;
import com.uuz.ubot.vo.BotReportVO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author hey
 */
@SpringBootApplication
@EnableAsync
public class UbotApplication {

	public static void main(String[] args) {
		SpringApplication.run(UbotApplication.class, args);
	}

}
