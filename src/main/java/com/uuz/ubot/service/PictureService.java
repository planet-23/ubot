package com.uuz.ubot.service;

/**
 * @author hey
 */
public interface PictureService {

	/**
	 * 发送图片到群组
	 * @param keyword
	 * @param groupId
	 */
	public void  sendPictureToGroup(String keyword,Integer groupId);
	
	
	/**
	 * 发送ai生成的图片到群组
	 * @param keyword
	 * @param groupId
	 */
	public void  sendAIPictureToGroup(String keyword,Integer groupId);
}
