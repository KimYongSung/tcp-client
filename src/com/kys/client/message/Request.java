package com.kys.client.message;

import com.kys.client.exception.TcpClientException;

/**
 * 모빌리언스 요청 처리 interface
 *
 * @author kys0213
 * @since  2018. 7. 4.
 */
public interface Request {

	/**
	 * SMS GW 요청 전문 파라미터 validation 체크
	 * @throws TcpClientException
	 */
	void validationCheck() throws TcpClientException;
}
