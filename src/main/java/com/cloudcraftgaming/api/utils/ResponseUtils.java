package com.cloudcraftgaming.api.utils;

/**
 * Created by Nova Fox on 11/6/17.
 * Website: www.cloudcraftgaming.com
 * For Project: DisCalWebAPI
 */
public class ResponseUtils {
	public static String getJsonResponseMessage(String msg) {
		return "{\"Message\": \"" + msg + "\"}";
	}
}