package com.encrypt.sha256;
/**
 * 솔팅과 키 스트레칭으로 구성된 암호화 시스템을 구현하려고 한다면 이미 검증된 암호화 시스템을 사용할 것을 권장
 * */
import java.security.MessageDigest;
/**
 * @category 
 * salt로 보완된 sha256
 * @see
 * - 솔팅(salting)을 사용해서 보완하기
 * @see
 * - 출처 : https://d2.naver.com/helloworld/318732
 * @see
 * - 보안방법
 * 1) 솔팅
 * 2) 키 스트레칭
 * 3) Adaptive Key Derivation Functions -- 여기 밑으론 안읽어봤다 뭔지도 몰라
 * 4) PBKDF2
 * 5) bcrypt
 * 6) scrypt
 * */
public class EncSha256 {
	private final String SALT = "ORANGE___SOUR___SWEET";
	
	public String getEncSHA256(String txt) throws Exception{
		//0. salt 생성
	    String rawString = SALT + txt + SALT;
	     
	    //1. Digest 인스턴스 생성
	    MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
	    
	    //2. byte로 변환된 텍스트 바인딩
	    mDigest.update(rawString.getBytes());
	     
	    //3. hash값으로 변환된 byte array 리턴 (SHA-256으로 해싱)
	    byte[] msgStr = mDigest.digest() ;
	    
	     
	    return this.bytesToHex(msgStr);
	}
	
	/**
	 * byte를 16진수로 변경
	 * */
	private String bytesToHex(byte[] bytes) {
	    StringBuffer sbuf = new StringBuffer();
	    
	     //4. 해시된 데이터는 바이트 배열의 바이너리 데이터이므로 16진수 문자열로 변환
	    for(int i=0; i < bytes.length; i++){
	        byte byteData = bytes[i];
	        // byteData를 HexString(16진수)으로 변환
	        // Integer.toString(int값,  16) 메소드를 통해서 정수값을 16진수로 변환하는 것은 소스코드를 보고도 명확히 알 수 있다.
	        String tmpEncTxt = Integer.toString((byteData & 0xff) + 0x100, 16).substring(1);
	         
	        sbuf.append(tmpEncTxt) ;
	    }
	    return sbuf.toString();
	}
}
