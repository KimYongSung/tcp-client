package com.kys.client.message.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
public @interface ListType {

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
	
	/**
	 * List의 갯수가 명시된 파라미터 순서
	 * @return
	 */
	int listCountParamIndex();
}
