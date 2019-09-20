package com.kys.client.message.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 요청전문
 *
 * @author kys0213
 * @since  2019. 1. 4.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ReqMessage {

	/**
	 * rPading시 채워넣을 문자
	 * @return
	 */
	String rightPaddingStr() default "";
	
	/**
	 * lPading시 채워넣을 문자
	 * @return
	 */
	String leftPaddingStr() default "";
}
