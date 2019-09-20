package com.kys.client.exception;

import com.kys.client.constants.ErrorCode;

/**
 * SmsGw 공통 Exception
 *
 * @author kys0213
 * @since  2018. 7. 5.
 */
@SuppressWarnings("serial")
public class TcpClientException extends RuntimeException {

	public TcpClientException(String message){
		super(message);
	}

	public TcpClientException(Throwable e){
		super(e);
	}

	public TcpClientException(String message, Throwable e){
		super(message, e);
	}
}
