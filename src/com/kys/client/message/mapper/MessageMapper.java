package com.kys.client.message.mapper;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import com.kys.client.message.Padding;
import com.kys.client.message.annotations.ReqMessage;
import com.kys.client.message.annotations.ReqParam;
import com.kys.client.message.annotations.ResParam;
import com.kys.client.util.ReflectionUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class MessageMapper {
	
	public static MessageMapper getInstance(){
		return MessageMapperLazyHolder.instance;
	}
	
	private static class MessageMapperLazyHolder{
		private final static MessageMapper instance = new MessageMapper();
	}
	
	public String objectToRequestMessage(Object obj){
		
		// TODO Request 전문 파싱 로직 클래스 생성
		Map<Integer, String> messageMap = requestObjectParsing(obj);
		
		return generateRequestMessage(messageMap);
	}

	private String generateRequestMessage(Map<Integer, String> messageMap) {
		StringBuilder strBuilder = new StringBuilder("");
		for(int index = 1; index <= messageMap.size() ; index++){
			String str = messageMap.get(index);
			
			if(isNullString(str)){
				continue;
			}
			
			strBuilder.append(str);
		}
		return strBuilder.toString();
	}

	private Map<Integer, String> requestObjectParsing(Object obj) {
		
		Class<?> clazz = obj.getClass();
		
		ReqMessage reqMessage = ReflectionUtil.findAnnotation(clazz, ReqMessage.class);
		
		if(reqMessage == null){
			throw new IllegalAccessError("RequestMessage Annotation not found");
		}
		
		Field[] fields = ReflectionUtil.findFields(clazz);
		
		Map<Integer, String> messageMap = new HashMap<Integer, String>();
		
		for (Field field : fields) {
			ReqParam param = ReflectionUtil.findAnnotation(field, ReqParam.class);
			
			if(param == null){
				continue;
			}
			
			String value = nullValue(ReflectionUtil.getField(field, obj));
			messageMap.put(param.order(), Padding.isRight(param.padding()) ? rPading(value, param, reqMessage) : lPading(value, param, reqMessage));
		}
		
		return messageMap;
	}
	
	public <T> T responseMessageToObject(Class<T> clazz, byte[] response){
		
		Map<Integer, ResponseParamMapping> messageMap = responseObjectParsing(clazz);
		
		// TODO response 전문 매핑 로직 클래스 생성
		return responseMapping(clazz, response, messageMap);
	}

	private <T> Map<Integer, ResponseParamMapping> responseObjectParsing(Class<T> clazz) {
		Field[] fields = ReflectionUtil.findFields(clazz);
		
		Map<Integer, ResponseParamMapping> messageMap = new HashMap<Integer, ResponseParamMapping>();
		
		for (Field field : fields) {
			ResParam param = ReflectionUtil.findAnnotation(field, ResParam.class);
			
			if(param == null){
				continue;
			}
			
			messageMap.put(param.order(), new ResponseParamMapping(field, param));
		}
		return messageMap;
	}

	private <T> T responseMapping(Class<T> clazz, byte[] response, Map<Integer, ResponseParamMapping> messageMap){
		T resInstance = ReflectionUtil.newInstance(clazz);
		
		ByteBuffer byteBuffer = ByteBuffer.wrap(response);
		
		for (int index = 1; index <= messageMap.size(); index++) {
			
			ResponseParamMapping mappingInfo = messageMap.get(index);
			
			if(mappingInfo == null){
				continue;
			}
			
			byte[] temp = new byte[mappingInfo.param.size()];
			byteBuffer.get(temp);
			
			String value = new String(temp).trim();
			
			ReflectionUtil.setField(mappingInfo.field, resInstance, value);
		}
		
		return resInstance;
	}
	
	@Getter
	@AllArgsConstructor
	private class ResponseParamMapping{
		
		private Field field;
		
		private ResParam param;
		
	}
	
	private String nullValue(Object obj){
		return obj == null ? "" : obj.toString();
	}
	
	/**
	 * int형 길이 앞에 0을 최대 길이만큼 채운다.
	 * ex) makePacketInt(420, 10) => 0000000420
	 * 
	 * @param dataLangth
	 * @param length
	 * @return
	 */
	private String lPading(String dataLangth, ReqParam param, ReqMessage message) {
		
		StringBuilder strBuffer = new StringBuilder();
		
		int digitLength = dataLangth.length();
		
		for (int i = digitLength; i < param.size(); i++) {
			strBuffer.append(message.leftPaddingStr());
		}
		
		strBuffer.append(dataLangth);
		
		return strBuffer.toString();
	}

	private String rPading(String temp, ReqParam param, ReqMessage message) {
		
		temp = nullValue(temp);
		int tempLength = stringByteSize(temp);
		
		StringBuilder strBuffer = new StringBuilder();

		strBuffer.append(temp);

		for (int i = tempLength; i < param.size(); i++) {
			strBuffer.append(message.rightPaddingStr());
		}
		return strBuffer.toString();
	}
	
	/**
	 * 문자열이 null or empty인지 체크
	 * @param src
	 * @return
	 */
	private boolean isNullString(String src) {
		return src == null || src.length() == 0;
	}
	
	/**
	 * UTF-8 용 길이 체크
	 * @param str
	 * @return 길이
	 */
	private int stringByteSize(String str) {
		if (isNullString(str)) {
			return 0;
		}

		int size = 0;

		for (int i = 0; i < str.length(); i++) {
			size += charByteSize(str.charAt(i));
		}
		
		return size;
	}
	
	/**
	 * 한글은 2byte를 리턴해준다.
	 * @param ch
	 * @return
	 */
	private int charByteSize(char ch) {
		if (ch <= 0x00007F) {
			return 1;
		} else if (ch <= 0x0007FF) {
			return 2;
		} else if (ch <= 0x00FFFF) {
			return 2;
		} else {
			return 4;
		}
	}
	
}
