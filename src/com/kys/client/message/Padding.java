package com.kys.client.message;

/**
 * 전문 패딩 방식
 *
 * @author kys0213
 * @since  2019. 1. 3.
 */
public enum Padding {

	/** 오른쪽에 패딩처리 */
	RIGHT, 
	
	/** 왼쪽에 패딩처리 */
	LEFT;
	
	public static boolean isRight(Padding padding){
		return Padding.RIGHT.equals(padding);
	}
}
