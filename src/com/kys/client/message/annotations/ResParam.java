package com.kys.client.message.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 응답 파라미터
 *
 * @author kys0213
 * @since  2019. 1. 4.
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface ResParam {

	/**
	 * 요청 전문 파라미터 순서
	 * @return
	 */
	int order();
	
	/**
	 * 요청 전문 파라미터 길이
	 * @return
	 */
	int size();
}
