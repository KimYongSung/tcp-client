package com.kys.client.log;

/**
 * Default ConsoleLogger
 *
 * @author kys0213
 * @since  2018. 7. 4.
 */
public final class ConsoleLogger implements TcpClientLoggerBridge {

	@Override
	public void info(String msg) {
		System.out.println(msg);
	}

	@Override
	public void debug(String msg) {
		System.out.println(msg);
	}

	@Override
	public void waring(String msg) {
		System.out.println(msg);
	}

	@Override
	public void error(String msg) {
		System.out.println(msg);
	}

	@Override
	public void error(String msg, Throwable e) {
		System.out.println(msg);
		System.out.println(e);
	}

	@Override
	public void error(Throwable e) {
		System.out.println(e);
	}
}
