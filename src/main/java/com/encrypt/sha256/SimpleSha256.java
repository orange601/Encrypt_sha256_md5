package com.encrypt.sha256;

import java.security.MessageDigest;

/**
 * @category
 * - 단순히 sha256으로 변환한다.
 * @see
 * - sha256은 단방향
 * @see 
 * 자세한 내용 
 * 출처 :: http://javaslave.tistory.com/59 
 * */
public class SimpleSha256 {
	/**
	 * 문자열을 SHA-256 방식으로 암호화
	 * @param txt 암호화 하려하는 문자열
	 * @return String
	 * @throws Exception
	 */
	public String getEncSHA256(String txt) throws Exception{
	    StringBuffer sbuf = new StringBuffer();
	     
	    //1. Digest 인스턴스 생성
	    MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
	    
	    //2. byte로 변환된 텍스트 바인딩
	    mDigest.update(txt.getBytes());
	     
	    //3. hash값으로 변환된 byte array 리턴 (SHA-256으로 해싱)
	    byte[] msgStr = mDigest.digest() ;
	    
	     //4. 해시된 데이터는 바이트 배열의 바이너리 데이터이므로 16진수 문자열로 변환
	    for(int i=0; i < msgStr.length; i++){
	        byte byteData = msgStr[i];
	        // byteData를 HexString(16진수)으로 변환
	        // Integer.toString(int값,  16) 메소드를 통해서 정수값을 16진수로 변환하는 것은 소스코드를 보고도 명확히 알 수 있다.
	        String tmpEncTxt = Integer.toString((byteData & 0xff) + 0x100, 16).substring(1);
	         
	        sbuf.append(tmpEncTxt) ;
	    }
	     
	    return sbuf.toString();
	}
	
	/** 
	 	[[[[   byte&0xff  ]]]]
	 
		 byte형은 8비트의 공간을 차지한다. int는 32비트(4바이트)의 공간을 차지한다. (( 1바이트에 8비트다. 공부좀해라.. 제발.. )) -- 나 자신에게 
		 비트연산자 &을 수행하는 경우 비트수가 넓은 곳에 맞춰서 낮은 비트를 가진 자료형을 확장한다. 
		 즉 byteData&0xff를 수행하는 경우 byte는 32비트의 int형으로 강제 형변환이 된다. 
		 이 때 byteData의 가장 앞에 비트가 0인 경우는 0으로 채운다. 
		 하지만 1인 경우는 비트 확장시 모든 비트를 1로 채운다. 
		 이 경우에 원본 값과는 전혀 다른 값이 되어버리기 때문에 &0xFF와 비트 연산을 수행하여 주는 것이다. 
		 즉 앞에 불필요하게 (정확히는 컴퓨터구조의 음수표현 때문에) 의도치 않게 채워진 1을 전부 0으로 바꿔주기 위해서 수행한다.
		 
		 1) byte의 첫번 째 비트가 0인 경우
			 int i = 1; // 00000000 00000000 00000000 00000001
		 	 System.out.println(Integer.toBinaryString(i)); // 00000000 00000000 00000000 00000001     
	        
			 byte b = (byte)i; // 00000001
			 System.out.println(Integer.toBinaryString(b)); // 00000000 00000000 00000000 00000001
			 
			 4번 line에서 int를 byte로 형변환 하는 경우 뒤에 마지막 8비트만 저장되고 앞에는 버려진다. 그래서 byte b = 00000001이 된다.
			 이때 5번 line에서 int로 다시 형변환 되어도 원래 숫자가 0으로 채워져서 처음 int i와 동일 한 결과가 나온다.
		 
		 
		 2) byte의 첫번 째 비트가 1인 경우
			 int i = 150; // 00000000 00000000 00000000 10010110
			 System.out.println(Integer.toBinaryString(i)); // 00000000 00000000 00000000 10010110     
	        
			 byte b = (byte)i; // 10010110
	 	 	 System.out.println(Integer.toBinaryString(b)); // 11111111 11111111 11111111 10010110
	 	 	 
	 	 	 byte의 첫번째 비트가 1인 경우는 5번 line에서 출력시 int로 변환 되면서 2의 보수법 처리 때문에 모든 비트가 1로 채워지는 것을 볼 수 있다. 
			 즉, 최초에 150의 값과는 전혀 다른 값으로 변형되기 때문에 문제가 발생하게 된다.
			 
			 int i = 150; // 00000000 00000000 00000000 10010110
			 System.out.println(Integer.toBinaryString(i)); // 00000000 00000000 00000000 10010110     
        
			 byte b = (byte)i; // 10010110
 			 System.out.println(Integer.toBinaryString(b&0xff)); // 00000000 00000000 00000000 10010110
 			 
 			  0xff ---- 00000000 0000000 0000000 11111111
			
			 (int)150   -----   00000000 0000000 0000000 10010110 
			 
			 (byte)150   -----  10010110  
			 
			 (int)(byte)150  -----  11111111 11111111 11111111 10010110 
			 
			 (byte)150 & 0xff	 ---- 00000000 0000000 0000000 10010110 
			 
			 두 값을 비트 연산 &를 하는경우 각 비트 자리가 모두 1이여야 1의 결과가 나오기 때문에 
			 00000000 0000000 0000000 10010110 이 출력되어 정상적으로 처리 되는 것을 알 수 있다.
			  물론 byte의 앞에 위치한 비트들이 0으로 채워지는 경우도 0&0 = 0 이기 때문에 위와 같이 &0xff를 
			  해주면 1인 경우와 0인 경우 모두 문제 없이 초기에 int형 데이터로 복원이 가능하다.
			  
			  
			  
		[[[[  (byte&0xff)+0x100  ]]]]
		
			결론부터 말하면 0x100을 해주어도 안해주어도 값 자체는 동일하게 나온다. 
			대신에 0x100을 Integer.toString(n, 16) 메소드의 결과값을 강제로 3자리로 만들어 준다. 
			int i = 1;
			System.out.println( Integer.toString((byte)i & 0xff, 16) ); // 16 진수 "1"이 출력
			        
			int j = 200;
			System.out.println( Integer.toString((byte)j & 0xff, 16) ); // 16 진수 "c8"이 출력
			
			위에 예제처럼 별다른 조작 없이 &0xff만 수행하는 경우 2진수 1~16까지의 16진수 변환 결과는 아래와 같다.
			
			1  -> 1
			2  -> 2
			3  -> 3
			4  -> 4
			5  -> 5
			6  -> 6
			7  -> 7
			8  -> 8
			9  -> 9
			10 -> a
			11 -> b
			12 -> c
			13 -> d
			14 -> e
			15 -> f
			16 -> 10
			
		[[[[ +0x100 ]]]]
		
			즉, 2진수 16이상의 숫자는 모두 2라의 String으로 리턴, 
			2진수 16미만의 숫자는 1자리의 String으로 리턴되기 때문에 추후에 
			String데이터를 저장할 때 다른 문제가 생기는 것을 방지하기 위해서 강제로 +0x100을 
			더하여 아래와 같이 변형하여 주는 것이다.
			int i = 1;
			System.out.println( Integer.toString(((byte)i & 0xff)+0x100, 16) ); // 16 진수 "101"이 출력
			int j = 200;
			System.out.println( Integer.toString(((byte)j & 0xff)+0x100, 16) ); // 16 진수 "1c8"이 출력
			
			1  -> 101
			2  -> 102
			3  -> 103
			4  -> 104
			5  -> 105
			6  -> 106
			7  -> 107
			8  -> 108
			9  -> 109
			10 -> 10a
			11 -> 10b
			12 -> 10c
			13 -> 10d
			14 -> 10e
			15 -> 10f
			16 -> 110
			
			
			
		[[[[ +0x100 ).substring(1) ]]]]
		
			0x100을 더하여 주어 강제로 3자리의 String으로 변경 하였다. 이제 불필요하게 붙은 제일 앞에 1만 제거해주면 된다.
			int i = 1;
			System.out.println( Integer.toString(((byte)i & 0xff)+0x100, 16).substring(1) ); // 16 진수 "01"
			int j = 200;
			System.out.println( Integer.toString(((byte)j & 0xff)+0x100, 16).substring(1) ); // 16 진수 "c8"
			
			위처럼 처리 결과에 substring(1)을 하여주면 

			1  -> 101 -> 01
			2  -> 102 -> 02
			3  -> 103 -> 03
			4  -> 104 -> 04
			5  -> 105 -> 05
			6  -> 106 -> 06
			7  -> 107 -> 07
			8  -> 108 -> 08
			9  -> 109 -> 09
			10 -> 10a -> 0a
			11 -> 10b -> 0b
			12 -> 10c -> 0c
			13 -> 10d -> 0d
			14 -> 10e -> 0e
			15 -> 10f -> 0f
			16 -> 110 -> 10
			
			으로 2자리에 맞게 16진수가 정상적으로 변경되어 출력되는 것을 확인 할 수 있다.
	 */
	
	
	/**
	 	[[ 비트연산자 ]]
			1) & : 비트 AND 연산자로 양쪽 비트가 모두 1일 때만 결과가 1이 되고 그렇지 않으면 0이 됨
		     	예)
		     	int num1 = 9;
				int num2 = 15;
				int num3 = num1 & num2;
				
				num1 =  0000 1001
				num2 =  0000 1111
				num3 =  0000 1001
				
				== num3 의 값은 9가 되겠죠
		

			2) | : 비트 OR 연산자로 양쪽 비트 중 어느 하나라도 1이면 결과가 1이 되고 모두 0일때만 0이 됨
    		예) (4 | 5) -> 결과 : 5 

			3) ^ : 비트 EXCLUSIVE OR(또는 XOR) 연산자는 양쪽 비트가 서로 다를 때만 1, 같을 때는 0이 됨
    		예) (4 ^ 5) -> 결과 : 1 

			4) ~ : 비트 NOT 연산자는 양쪽 비트 연산자와는 다르게 피연산자를 하나만 갖는 단항 연산자, 모든 비트값을 반대로 만든다 (0->1, 1->0)
    		예) (~5) -> 결과 : -6
	 */

}
