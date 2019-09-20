package com.kys.client.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * 클라이언트 소켓 통신
 * 
 * @author kys0213
 * @since  2018. 11. 28.
 */
final class ClientSocket implements Closeable{
	
	/**
	 * 서버 연결 타임아웃
	 */
	private final int connectionTimeout;
	
	/**
	 * 응답 대기 타임아웃
	 */
	private final int recvTimeout;
	
	/**
	 * 요청 charset
	 */
	private final Charset reqCharset;
	
	/**
	 * 응답 charset
	 */
	private final Charset recvCharset;
	
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private OutputStream outputStream;
	private InputStream inputStream;
	
	/**
	 * 서버 송수신 용 소켓 생성자
	 * @param reqCharset 요청 charset
	 * @param recvCharset 응답 charset
	 */
	public ClientSocket(Charset reqCharset, Charset recvCharset) {
		this.reqCharset = reqCharset;
		this.recvCharset = recvCharset;
		this.connectionTimeout = 3000;
		this.recvTimeout = 3000;
	}

	/**
	 * 서버 송수신 용 소켓 생성자
	 * @param reqCharset 요청 charset
	 * @param recvCharset 응답 charset
	 * @param connectionTimeout 서버 연결 타임아웃
	 * @param recvTimeout 응답 대기 타임아웃
	 */
	public ClientSocket(Charset reqCharset, Charset recvCharset, int connectionTimeout, int recvTimeout) {
		this.reqCharset = reqCharset;
		this.recvCharset = recvCharset;
		this.connectionTimeout = connectionTimeout; 
		this.recvTimeout = recvTimeout; 
	}

	/**
	 * Socket 연결
	 * @param ip 목적지 서버 ip
	 * @param port 목적이 서버 port
	 * @throws IOException 연결 중 예외 발생시
	 */
	public void connect(String ip, int port) throws IOException {
		try{
			socket = new Socket();
			socket.connect(new InetSocketAddress(ip, port), connectionTimeout);
			socket.setSoTimeout(recvTimeout);
		}catch(IOException e){
			socketClose();
			throw e;
		}
	}
	
	/**
	 * 소켓 데이터 recv
	 * @return 응답 문자열
	 * @throws IOException 수신 중 예외 발생시
	 */
	public String recvStreamToString() throws IOException{
		
		if(bufferedReader == null){
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), recvCharset.name()));
		}
		
		StringBuilder recv = new StringBuilder();
		char[] tempBuffer = new char[128];
		int readByteLength = -1;
		while((readByteLength = bufferedReader.read(tempBuffer)) > 0){
			recv.append(tempBuffer, 0, readByteLength);
		}
		return recv.toString();
	}
	
	/**
	 * 소켓 데이터 recv
	 * @param size 소켓에서 읽을 데이터 사이즈
	 * @return
	 * @throws IOException 수신 중 예외 발생시
	 */
	public byte[] recvStreamToByte(int size) throws IOException{
		
		if(inputStream == null){
			inputStream = socket.getInputStream();
		}
		
		byte[] temp = new byte[size];
		inputStream.read(temp);
		return temp;
	}
	
	/**
	 * 소켓 데이터 recv
	 * <li>소켓의 전체 데이터를 읽는다.</li><br>
	 * @return
	 * @throws IOException 수신 중 예외 발생시
	 */
	public byte[] recvStreamToByte() throws IOException{
		
		int read = 0;
		int tempLength = 128;
		
		FastByteArrayOutputStream buffer = new FastByteArrayOutputStream(tempLength);
		
		try{
			if(inputStream == null){
				inputStream = socket.getInputStream();
			}
			
			byte[] temp = new byte[tempLength];
			
			while((read = inputStream.read(temp, 0, temp.length)) != -1){
				buffer.write(temp, 0 , read);
			}
			
			return buffer.toByteArray();
		}finally{
			buffer.close();
		}
	}
	
	/**
	 * 소켓 데이터 send
	 * @param data	 	전송할 데이터
	 * @throws IOException
	 */
	public void write(String data) throws IOException{
		if(bufferedWriter == null){
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), reqCharset.name()));
		}
		bufferedWriter.write(data);
		bufferedWriter.flush();
	}
	
	/**
	 * 소켓 데이터 send
	 * @param data
	 * @throws IOException
	 */
	public void write(byte[] data) throws IOException{
		if(outputStream == null){
			outputStream = socket.getOutputStream();
		}
		outputStream.write(data);
		outputStream.flush();
	}
	
	/**
	 * Resource Close
	 */
	@Override
	public void close(){
		close(bufferedReader, bufferedWriter, inputStream, outputStream);
		socketClose();
	}

	private void socketClose() {
		if(socket != null){
			try {
				socket.close();
			} catch (IOException ignore) {
			}
		}
	}
	
	/**
	 * Resource Close
	 * @param closeables
	 */
	private void close(Closeable... closeables){
		if(closeables == null) return;
		
		for (Closeable closeable : closeables) {
			
			try {
				if(closeable == null){
					continue;
				}
				closeable.close();
			} catch (IOException ignore) {
			}
		}
	}
	
	/**
	 * 기존 ByteArrayOutputStream에 write와 toByteArray 의 성능 개선
	 * 
	 * <li>write 메소드의 synchronized 제거</li>
	 * <li>toByteArray 메소드의 synchronized 제거 및 buf 복제 로직 제거</li> <br>
	 * 
	 * @author kys0213
	 * @since 2019. 1. 22.
	 */
	private static class FastByteArrayOutputStream extends ByteArrayOutputStream {

		public FastByteArrayOutputStream(int size) {
			super(size);
		}

		@Override
		public void write(byte b[], int off, int len) {

			if ((off < 0) || (off > b.length) || (len < 0) || ((off + len) > b.length) || ((off + len) < 0)) {
				throw new IndexOutOfBoundsException();
			} else if (len == 0) {
				return;
			}

			int newCount = count + len;
			
			if (newCount > buf.length) {
				buf = Arrays.copyOf(buf, Math.max(buf.length << 1, newCount));
			}

			System.arraycopy(b, off, buf, count, len);

			count = newCount;
		}

		public byte toByteArray()[] {
			return buf;
		}
	}
}
