package com.adzuki.admin.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import org.apache.commons.lang3.StringUtils;

public class SecurityUtils {
	
	public static String MD5(String content) {
		return MD5(content, null);
	}
	
	public static String MD5(String content, String salt) {
		String text = content;
		if(StringUtils.isNotBlank(salt)) {
			text += salt;
		}
		
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		
		byte[] charArray = null;
		try {
			charArray = text.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}
	
	public static String encryptPassword(String content) {
		return MD5(content, content);
	}
	
	public static void main(String[] args) {
		
		System.out.println(MD5("123456"));
		System.out.println(encryptPassword("123456"));
	}

}
