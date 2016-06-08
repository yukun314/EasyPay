package com.bfyd.easypay.utils;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created by zyk on 2016/5/31.
 * MD5加密
 */
public class MD5 {
	private static final String MD5_NAME = "MD5";

	public static String md5(String plain) {
		try {
			MessageDigest m = MessageDigest.getInstance(MD5_NAME);
			m.update(plain.getBytes());
			byte[] digest = m.digest();
			BigInteger bigInt = new BigInteger(1, digest);
			String hashtext = bigInt.toString(16);
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
		} catch (Exception e) {
			return "";
		}
	}

}
