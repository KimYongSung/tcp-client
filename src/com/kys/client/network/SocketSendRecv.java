package com.kys.client.network;

import java.io.IOException;
import java.nio.charset.Charset;

import com.kys.client.log.ConsoleLogger;
import com.kys.client.log.TcpClientLoggerBridge;
import com.kys.client.message.Request;
import com.kys.client.message.Response;
import com.kys.client.message.mapper.MessageMapper;
import com.kys.client.util.ReflectionUtil;
import com.kys.client.constants.ErrorCode;
import com.kys.client.constants.StubCommonErrorCode;
import com.kys.client.exception.ConnectFailException;
import com.kys.client.exception.TcpClientException;
import com.kys.client.exception.RecvFailException;
import com.kys.client.exception.SendFailException;

/**
 * 소켓 통신 송수신 처리
 *
 * @author kys0213
 * @since  2018. 7. 4.
 */
public final class SocketSendRecv {
	
	/**
	 * 로거
	 */
	private final TcpClientLoggerBridge logger;
	
	/**
	 * 서버의 Charset
	 */
	private final Charset serverCharset;
	
	/**
	 * 클라이언트의 Charset
	 */
	private final Charset clientCharset;
	
	/**
	 * 요청, 응답 전문 파서
	 */
	private final MessageMapper mapper = MessageMapper.getInstance();
	
	/**
	 * 소켓 통신 송수신 처리 클래스 생성자
	 * @param logger
	 * @param serverCharset
	 * @param clientCharset
	 */
	public SocketSendRecv(TcpClientLoggerBridge logger, Charset serverCharset, Charset clientCharset) {
		super();
		// logger가 null 일 경우 기본 consoleLogger 출력
		this.logger = ( logger == null  ) ? new ConsoleLogger() : logger;
		this.serverCharset = serverCharset;
		this.clientCharset = clientCharset;
	}

	/**
	 * 모빌리언스 소켓 송수신 처리
	 * @param req		  요청 전문
	 * @param resClazz 	  응답 전문 클래스
	 * @param ip    	  요청 IP
	 * @param port  	  요청 PORT
	 * @param switchIp    요청 IP
	 * @param switchPort  요청 PORT
	 * @return
	 */
	public <T extends Request, R extends Response> R execute(T req, Class<R> resClazz, String ip, int port, String switchIp, int switchPort) throws TcpClientException{
		
		ClientSocket socket = null;
		logger.info("[ START ] " + req.toString());
		try {
			
			// 1. 요청 전문 유효성 검사
			req.validationCheck();
			logger.debug(req.getClass().getSimpleName() + " Validation Check Success");
			
			// 2. 요청 전문 생성
			String requestMessage = mapper.objectToRequestMessage(req);
			
			logger.debug("request : "+requestMessage);
			
			// 3. 모빌리언스 서버 접속
			socket = connect(ip, port, switchIp, switchPort);
			
			// 4. 요청전문 송신
			write(socket, requestMessage);
			
			// 5. 응답전문 수신
			String resMessage = recv(socket);
			
			logger.debug("response : "+resMessage);
			
			// 6. 응답전문 파싱
			R res = mapper.responseMessageToObject(resClazz, resMessage.getBytes());
			
			logger.info("[ END ] " + res.toString());
			
			return res;
			
		} finally{
			if(socket != null) socket.close();
		}
	}
	
	/**
	 * 모빌리언스 응답 수신
	 * @param socket
	 * @return
	 * @throws RecvFailException
	 */
	private String recv(ClientSocket socket) throws TcpClientException {
		try {
			logger.debug("server recv start");
			String recv = new String(socket.recvStreamToByte(), clientCharset);
			logger.debug("server recv end");
			return recv;
		} catch (IOException e) {
			throw new RecvFailException(e);
		}
	}
	
	/**
	 * 모빌리언스 요청 송신
	 * @param socket
	 * @param requestMessage
	 * @throws SendFailException
	 */
	private void write(ClientSocket socket, String requestMessage) throws TcpClientException {
		try {
			logger.debug("server send start");
			socket.write(requestMessage);
			logger.debug("server send end");
		} catch (IOException e) {
			throw new SendFailException(e);
		}
	}
	
	/**
	 * 모빌리언스 서버 접속
	 * @param ip
	 * @param port
	 * @param switchIp
	 * @param switchPort
	 * @return
	 * @throws ConnectFailException
	 */
	private ClientSocket connect(String ip, int port, String switchIp, int switchPort) throws TcpClientException {
		
		ClientSocket socket = new ClientSocket(serverCharset, clientCharset);
		try {
			logger.debug("server connect start !! [ ip : " + ip + ", port : " + port + " ] ");
			socket.connect(ip, port);
			logger.debug("server connect end");
			return socket;
		} catch (IOException e) {
			logger.waring("server connect fail !! [ ip : " + ip + ", port : " + port + " ] - " + e.toString());
			
			try {
				logger.waring("switch server connect start !! [ ip : " + switchIp + ", port : " + switchPort + " ] ");
				socket.connect(switchIp, switchPort);
				logger.waring("switch server connect end");
				return socket;
			} catch (IOException e1) {
				logger.error("switch server connect fail !! [ ip : " + switchIp + ", port : " + switchPort + " ] - " + e1.toString());
				throw new ConnectFailException(e1.initCause(e));
			}
		} 
	}
}
