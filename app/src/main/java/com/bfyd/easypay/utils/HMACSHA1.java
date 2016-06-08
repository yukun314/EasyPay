package com.bfyd.easypay.utils;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by zyk on 2016/5/31.
 */
public class HMACSHA1 {
	private static final String MAC_NAME = "HmacSHA1";
	private static final String ENCODING = "UTF-8";

	/**
	 * 使用 HMAC-SHA1 签名方法对对encryptText进行签名
	 *
	 * @param encryptText 被签名的字符串
	 * @param encryptKey  密钥
	 * @return
	 * @throws Exception
	 */
	public static String HmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception {
		//先对秘钥进行MD5加密
		encryptKey = MD5.md5(encryptKey);
		byte[] data = encryptKey.getBytes(ENCODING);
		//根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
		SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
		//生成一个指定 Mac 算法 的 Mac 对象
		Mac mac = Mac.getInstance(MAC_NAME);
		//用给定密钥初始化 Mac 对象
		mac.init(secretKey);

		byte[] text = encryptText.getBytes(ENCODING);
		//完成 Mac 操作
		return new String(Base64.encode(mac.doFinal(text),Base64.DEFAULT),ENCODING);
	}


	//php的写法
//	/**
//	 * @brief 使用HMAC-SHA1算法生成oauth_signature签名值
//	 *
//	 * @param $key 密钥
//	 * @param $str 源串
//	 *
//	 * @return 签名值
//	 */
//	function getSignature($str, $key) {
//		$signature = "";
//		if (function_exists('hash_hmac')) {
//			$signature = base64_encode(hash_hmac("sha1", $str, $key, true));
//		} else {
//			$blocksize = 64;
//			$hashfunc = 'sha1';
//			if (strlen($key) > $blocksize) {
//				$key = pack('H*', $hashfunc($key));
//			}
//			$key = str_pad($key, $blocksize, chr(0x00));
//			$ipad = str_repeat(chr(0x36), $blocksize);
//			$opad = str_repeat(chr(0x5c), $blocksize);
//			$hmac = pack(
//					'H*', $hashfunc(
//							($key ^ $opad) . pack(
//									'H*', $hashfunc(
//											($key ^ $ipad) . $str
//									)
//							)
//					)
//			);
//			$signature = base64_encode($hmac);
//		}
//		return $signature;
//	}
	public static void main(String[] args) {
		try {
			String result = HmacSHA1Encrypt("this is a test sha1","zyk+mc+function+test+abc");
			System.out.println("str:"+result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
