package com.shidaiyintong.cn.utils;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

/**
 * 字符串与16进制数字互转
 */
public class StringTo16 {

	private static String hexString = "0123456789ABCDEF";

	/**
	 * 将字符串编码成16进制数字,适用于所有字符（包括中文）
	 *
	 * @param str
	 * @return
	 */
	public static String encode(String str) {
		// 根据默认编码获取字节数组
		byte[] bytes = str.getBytes();
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		// 将字节数组中每个字节拆解成2位16进制整数
		// 0110
		// 0xf0
		//
		/**
		 * byte呢是一个字节，也就是8位，如：0010 0100 而0xf0呢，也是8位：1111 0000
		 * 而byte&0xf0呢，就是按位与操作(0&1=0,0&0=0,1&1=1)，这样呢就得到0010 0000这样八位表示的字节，
		 * 然后">>4"操作是向右移四位，最高位用0补，就得到0000 0010
		 */
		for (int i = 0; i < bytes.length; i++) {
			sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
			sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
		}
		return sb.toString();
	}

	/**
	 * 将16进制数字解码成字符串,适用于所有字符（包括中文）
	 *
	 * @param bytes
	 * @return
	 */
	public static String decode(String bytes) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length() / 2);
		// 将每2位16进制整数组装成一个字节
		for (int i = 0; i < bytes.length(); i += 2) {
//			System.out.println((hexString.indexOf(bytes.charAt(i)) << 4 | hexString.indexOf(bytes.charAt(i + 1))));
			if ((hexString.indexOf(bytes.charAt(i)) << 4 | hexString.indexOf(bytes.charAt(i + 1))) < 20) {
				baos.write(120);
			} else {
				baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString.indexOf(bytes.charAt(i + 1))));
			}
		}
		return new String(baos.toByteArray());
	}

	/**
	 * 将16进制数字解码成字符串,适用于所有字符（包括中文）【长度特殊处理】
	 *
	 * @param bytes
	 * @return
	 */
	public static String strToString(String str) {
		String hexString = "0123456789ABCDEF";
		ByteArrayOutputStream baos = new ByteArrayOutputStream(str.length() / 2);
		// 将每2位16进制整数组装成一个字节
		for (int i = 0; i < str.length(); i += 2) {
			if (i > 49 && i < 58) {
				baos.write(63);
			} else {
				baos.write((hexString.indexOf(str.charAt(i)) << 4 | hexString.indexOf(str.charAt(i + 1))));
			}
		}
		return new String(baos.toByteArray());
	}
}
