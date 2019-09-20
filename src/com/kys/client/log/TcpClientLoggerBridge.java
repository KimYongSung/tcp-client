package com.kys.client.log;

/**
 * 클라이언트 모듈 로깅 브릿지 Interface
 * <br><br> 
 * 사용하는 logging 프레임워크 브릿지 용
 * 
 * @author kys0213
 * @since  2018. 6. 29.
 */
public interface TcpClientLoggerBridge {

	/**
	 * info 로그 출력
	 * @param msg
	 */
	void info(String msg);
	
	/**
	 * debug 로그 출력
	 * @param msg
	 */
	void debug(String msg);
	
	/**
	 * waring 로그 출력
	 * @param msg
	 */
	void waring(String msg);
	
	/**
	 * error 로그 출력
	 * @param msg
	 */
	void error(String msg);
	
	/**
	 * error 로그 출력
	 * @param msg
	 * @param e
	 */
	void error(String msg, Throwable e);
	
	/**
	 * error 로그 출력
	 * @param e
	 */
	void error(Throwable e);
}
