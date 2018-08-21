package com.shidaiyintong.cn.utils;

import com.shidaiyintong.cn.common.SystemConstants;

import java.text.MessageFormat;
import java.util.List;
import java.util.regex.Pattern;

public class StringUtil {

	/**
	 * @param str  String
	 * @param len  int
	 * @param type int 0:后补space；1：后补0；2：前补space；3：前补0
	 * @return String
	 */
	public static String formatString(String str, int len, int type) {
		StringBuffer buffer = new StringBuffer(str);
		int length = 0;
		for (int i = 0; i < str.length(); i++) {
			length++;
			char c = str.charAt(i);
			if (Pattern.matches("[\u0391-\uFFE5]", String.valueOf(c))) {
				length++;
			}
		}
		if (str.length() > len) {
			return str.substring(str.length() - len, str.length());
		}
		for (int i = 0; i < len - length; i++) {
			switch (type) {
				case 0:
					buffer.append(' ');
					break;
				case 1:
					buffer.append('0');
					break;
				case 2:
					buffer.insert(0, ' ');
					break;
				case 3:
					buffer.insert(0, '0');
					break;
			}
		}
		return buffer.toString();
	}

	public static String formatAccount(String account) {
		String temp = account.trim();
		if (temp.length() == 18) {
			if (temp.substring(7, 11).equals("0188")) {
				return temp.substring(0, 7) + "880101"
						+ temp.substring(11, temp.length()) + "00";
			} else {
				return account;
			}
		} else // if (temp.length() == 22)
		{
			return account;
		}
	}

	public static String stringOfchar(char ch, int len) {
		StringBuffer buffer = new StringBuffer(0);
		for (int i = 0; i < len; i++) {
			buffer.append(ch);
		}
		return buffer.toString();
	}

	/**
	 * @param amount 金额
	 * @param zlen   整数位数
	 * @param dlen   小数位数
	 * @return
	 */
	public static String formatAmount(String amount, int zlen, int dlen) {
		String reg = "0." + formatString("0", dlen, 3);
		String fm1 = "{0,NUMBER," + reg + "}";
		amount = MessageFormat.format(fm1, new Object[]{Double.parseDouble(amount)});
		StringBuffer buffer = new StringBuffer();
		int index = amount.indexOf(".");
		if (index == -1) {
			buffer.append(formatString(amount, zlen, 3));
			buffer.append(stringOfchar('0', dlen));
		} else {
			buffer.append(formatString(amount.substring(0, index), zlen, 3));
			buffer.append(formatString(amount.substring(index + 1, amount.length()), dlen, 1));
		}
		return buffer.toString();
	}

	public static String string2HexString(String strPart) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < strPart.length(); i++) {
			int ch = (int) strPart.charAt(i);
			String strHex = Integer.toHexString(ch);
			hexString.append(strHex);
		}
		return hexString.toString();
	}

	public static byte[] String2Bytes(String strPart) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < strPart.length(); i++) {
			int ch = (int) strPart.charAt(i);
			String strHex = Integer.toHexString(ch);
			hexString.append(strHex);
		}
		int l = hexString.length() / 2;
		byte[] ret = new byte[l];
		for (int i = 0; i < l; i++) {
			ret[i] = (byte) Integer
					.valueOf(hexString.substring(i * 2, i * 2 + 2), 16).byteValue();
		}
		return ret;
	}

	/**
	 * @param @param  num
	 * @param @return
	 * @return byte[]
	 * @throws
	 * @Title: integerToHex
	 * @Description: 将int型数转换为hex字符
	 */
	public static byte[] integerToHex(int num) {
		String temp = Integer.toHexString(num).toUpperCase();
		temp = StringUtil.formatString(temp, 4, 3);
		char[] data = temp.toCharArray();
		int len = data.length;
		if ((len & 0x01) != 0) {
			throw new RuntimeException("Odd number of characters.");
		}

		byte[] out = new byte[len >> 1];
		// two characters form the hex value.
		for (int i = 0, j = 0; j < len; i++) {
			int f = toDigit(data[j], j) << 4;
			j++;
			f = f | toDigit(data[j], j);
			j++;
			out[i] = (byte) (f & 0xFF);
		}
		return out;
	}

	/**
	 * @param @param  ch
	 * @param @param  index
	 * @param @return
	 * @return int
	 * @throws
	 * @Title: toDigit
	 * @Description: 将十六进制字符转换成一个整数
	 */
	protected static int toDigit(char ch, int index) {
		int digit = Character.digit(ch, 16);
		if (digit == -1) {
			throw new RuntimeException("Illegal hexadecimal character " + ch
					+ " at index " + index);
		}
		return digit;
	}

	public static String getMsgBody(String[] headLengthStr, List<String> list) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < headLengthStr.length; i++) {
			//获取填写报文的长度
			String headLength = headLengthStr[i];
			//获取填写的报文
			String param = list.get(i);
			String substring = headLength.substring(headLength.indexOf("(") + SystemConstants.ONE, headLength.indexOf(")"));
			if (headLength.startsWith(SystemConstants.NUM_X)) {
				// X（）-字段类型为字符，不足左对齐右补空格。
				param = StringUtil.formatString(param, Integer.parseInt(substring), SystemConstants.ZERO);
			} else if (headLength.startsWith(SystemConstants.NUM_9)) {
				if (headLength.endsWith(")")) {
					// 9（）-字段类型为数字，不足右对齐左补零
					param = StringUtil.formatString(param, Integer.parseInt(substring), SystemConstants.THREE);
				} else if (headLength.substring(headLength.indexOf(SystemConstants.NUM_V) + SystemConstants.ONE, headLength.length()).length() == SystemConstants.TWO) {
					//9（18）V99  -代表数字+2位小数点
					param = StringUtil.formatAmount(param, Integer.parseInt(substring), SystemConstants.TWO);
				} else if (headLength.substring(headLength.indexOf(SystemConstants.NUM_V) + SystemConstants.ONE, headLength.length()).length() == SystemConstants.FOUR) {
					//9（18）V9999  -代表数字+4位小数点
					param = StringUtil.formatAmount(param, Integer.parseInt(substring), SystemConstants.FOUR);
				}
			} else if (headLength.startsWith(SystemConstants.NUM_S)) {
				//S9()-字段类型为符号位+数字
				//todo
			}
			sb.append(param);
		}
		return sb.toString();
	}
}
