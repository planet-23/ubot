package com.uuz.ubot.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author hey
 */

@Data
public class BotReportVO implements Serializable {
	
	private String postType;

	private String messageType;

	private Integer time;

	private Integer selfId;

	private String subType;

	private Map<String,Object> sender;

	private Integer messageId;

	private Integer font;

	private String message;

	private Integer messageSeq;

	private Long userId;

	private String anonymous;

	private Integer groupId;

	private String rawMessage;
}

