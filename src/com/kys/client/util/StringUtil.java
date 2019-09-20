package com.kys.client.util;

import java.util.regex.Pattern;


public final class StringUtil {

	/**
	 * 문자열이 null or empty인지 체크
	 * @param src
	 * @return
	 */
	public static boolean isNullOrEmpty(String src) {
		return src == null || src.length() == 0;
	}

	/**
	 * 문자열이 null 일 경우 공백리턴
	 * @param src
	 * @return
	 */
	public static String nullToStr(String src){
		return isNullOrEmpty(src) ? "" : src;
	}
	
	/**
	 * 문자열이 null or empty 아닌지 체크
	 * @param src
	 * @return
	 */
	public static boolean isNotNullOrNotEmpty(String src){
		return !isNullOrEmpty(src);
	}

	/**
	 * 정수형 여부 확인
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str){ 
    	if (isNullOrEmpty(str)) return false;
    	return Pattern.matches("[0-9]+", str);
    }

	/**
	 * 정수형 여부
	 * @param str
	 * @return
	 */
	public static boolean isNotNumberic(String str){
		return !isNumeric(str);
	}
	
	/**
	 * string 문자열의 바이트 배열 길이
	 * @param str
	 * @return 길이
	 */
	public static int stringByteSize(String str) {
		if (isNullOrEmpty(str)) {
			return 0;
		}

		int size = 0;

		for (int i = 0; i < str.length(); i++) {
			size += charByteSize(str.charAt(i));
		}
		
		return size;
	}
	
	/**
	 * 한글은 2byte를 리턴해준다.
	 * @param ch
	 * @return
	 */
	public static int charByteSize(char ch) {
		if (ch <= 0x00007F) {
			return 1;
		} else if (ch <= 0x0007FF) {
			return 2;
		} else if (ch <= 0x00FFFF) {
			return 2;
		} else {
			return 4;
		}
	}

	/**
	 * 오른쪽 문자열 패딩 처리
	 * @param temp
	 * @param maxLength
	 * @param str
	 * @return
	 */
	public static String rightPadding(String temp, int maxLength, String str) {
		
		temp = nullToStr(temp);

		StringBuilder strBuilder = new StringBuilder(temp);

		for (int i = temp.length(); i < maxLength; i++)
			strBuilder.append(str);

		return strBuilder.toString();
	}

	/**
	 * 외쪽으로 문자열 패딩 처리
	 * @param temp
	 * @param maxLength
	 * @param str
	 * @return
	 */
	public static String leftPadding(String temp, int maxLength, String str) {

		temp = nullToStr(temp);

		StringBuilder strBuffer = new StringBuilder();

		for (int i = temp.length(); i < maxLength; i++)
			strBuffer.append(str);

		strBuffer.append(temp);
		
		return strBuffer.toString();
	}
}
