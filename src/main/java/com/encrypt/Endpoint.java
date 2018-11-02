package com.encrypt;

import com.encrypt.sha256.EncSha256;

public class Endpoint {
	public static void main(String[] args) throws Exception{
		EncSha256 enc = new EncSha256();
		System.out.println(enc.getEncSHA256("ttttt"));
	}
}
