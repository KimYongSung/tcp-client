package com.kys.client.message;

import com.kys.client.constants.ErrorCode;

/**
 * 모빌리언스 응답 처리 interface
 *
 * @author kys0213
 * @since  2018. 7. 4.
 */
public interface Response{

	/**
	 * 결과 코드 리턴
	 * @return 결과 코드
	 */
	String getResultCode();
	
	/**
	 * 결과 메시지 리턴
	 * @return 결과 메시지
	 */
	String getResultMessage();

	/**
	 * 응답 결과 성공 여부
	 * @return
	 */
	boolean isSuccess();
}
