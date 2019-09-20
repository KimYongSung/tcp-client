package com.kys.client.exception;

import com.kys.client.constants.StubCommonErrorCode;

/**
 * SmsGw 응답 수신 실패시 발생
 *
 * @author kys0213
 * @since  2018. 7. 5.
 */
@SuppressWarnings("serial")
public class RecvFailException extends TcpClientException {

	public RecvFailException(Throwable e) {
		super(e);
	}
}
