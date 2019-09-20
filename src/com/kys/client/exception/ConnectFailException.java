package com.kys.client.exception;

import com.kys.client.constants.StubCommonErrorCode;

/**
 * 서버 연결 실패시
 *
 * @author kys0213
 * @since  2018. 7. 5.
 */
@SuppressWarnings("serial")
public class ConnectFailException extends TcpClientException {

	public ConnectFailException(Throwable e) {
		super(e);
	}
}
