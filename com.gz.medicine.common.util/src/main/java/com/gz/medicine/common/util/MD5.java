package com.gz.medicine.common.util;

/**
 * File Name: ShA.java
 * Package: com.enviveus.test
 * Date: 2017年2月22日 上午10:46:13
 * Version: V1.0
 * Copyright (c) 2016, enviveus.com All Rights Reserved.
 */


import java.security.MessageDigest;

public class MD5 {
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	/**
	 * 转换字节数组为16进制字串
	 * 
	 * @param b
	 *            字节数组
	 * @return 16进制字串
	 */
	public static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	/**
	 * J 转换byte到16进制
	 * 
	 * @param b
	 * @return
	 */
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n = 256 + n;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	/**
	 * J 编码
	 * 
	 * @param origin
	 * @return
	 */

	// MessageDigest 为 JDK 提供的加密类
	public static String MD5Encode(String origin) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(resultString
					.getBytes()));
		} catch (Exception ex) {
		}
		return resultString;
	}
	
	// MessageDigest 为 JDK 提供的加密类
	public static String MD5Encode(String origin,String charsetName) {
		origin =origin.trim();
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(resultString
					.getBytes(charsetName)));
		} catch (Exception ex) {
		}
		return resultString;
	}
	
	public static void main(String[] args) {
//		System.err.println(MD5Encode("<?xml version='1.0' encoding='GBK' ?><AP><oprCd>SYTXJSK</oprCd><mchntCd>8183310f0254562</mchntCd><salesType>2</salesType><payType>2</payType><totalMoney>9.99</totalMoney><acturalMoney>9.99</acturalMoney><payMoney>9.99</payMoney><exchange>0</exchange><rowCrtUsr>fuiou</rowCrtUsr><reserved1>备注</reserved1></AP>|11111111111111111111111111111111"));
		System.err.println(MD5Encode("8f16771f9f8851b26f4d460fa17de93e2711c7e51337cb8a608a0f81e1c1b6ae"));
	}
}
