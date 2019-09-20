package com.kys.client.exception;

import com.kys.client.constants.StubCommonErrorCode;

/**
 * SmsGW 서버 전송 실패시 발생
 *
 * @author kys0213
 * @since  2018. 7. 5.
 */
@SuppressWarnings("serial")
public class SendFailException extends TcpClientException {

	public SendFailException(Throwable e) {
		super(e);
	}
}
