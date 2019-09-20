package com.kys.client.constants;

/**
 * Stub 공통 에러코드
 *
 * @author kys0213
 * @since  2018. 12. 7.
 */
public enum StubCommonErrorCode implements ErrorCode{
	INTERNAL_SERVER_ERROR   ("S999","시스템에러")
	,ERR_CONNECT			("S991","서버 연결 실패")
	,ERR_RECV				("S992","패킷 수신 실패")
	,ERR_SEND				("S993","패킷 송신 실패")
	;
	private String code;
	
	private String message;

	private StubCommonErrorCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
