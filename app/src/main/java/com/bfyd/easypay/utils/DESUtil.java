package com.bfyd.easypay.utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import android.annotation.SuppressLint;
import android.util.Base64;

public class DESUtil {

	 //算法名称 
    public static final String KEY_ALGORITHM = "DES";
    
    //长度大于8位
    private static final String key = "dnatech_wc_zyk";
     
    /**
     * Description 根据键值进行加密
     * @param data 
     */
    public static String encrypt(String data) {
        byte[] bt = encrypt(data.getBytes(), key.getBytes());
        return Base64.encodeToString(bt, Base64.DEFAULT);
    }
 
    /**
     * Description 根据键值进行解密
     * @param data
     * @return
     */
    public static String decrypt(String data){
        if (data == null)
            return null;
        byte[] buf = Base64.decode(data,Base64.DEFAULT);
        byte[] bt = decrypt(buf,key.getBytes());
        return new String(bt);
    }
 
    /**
     * Description 根据键值进行加密
     * @param data
     * @param key  加密键byte数组
     * @return
     */
    @SuppressLint("TrulyRandom")
	private static byte[] encrypt(byte[] data, byte[] key){
        
		try {
			// 生成一个可信任的随机数源
	        SecureRandom sr = new SecureRandom();
	        // 从原始密钥数据创建DESKeySpec对象
	        DESKeySpec dks = new DESKeySpec(key);
	        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
	        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
	        SecretKey securekey = keyFactory.generateSecret(dks);
	 
	        // Cipher对象实际完成加密操作
	        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
	 
	        // 用密钥初始化Cipher对象
	        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
	 
	        return cipher.doFinal(data);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
 
       return data;
    }
     
     
    /**
     * Description 根据键值进行解密
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
	private static byte[] decrypt(byte[] data, byte[] key) {

		try {
			// 生成一个可信任的随机数源
			SecureRandom sr = new SecureRandom();

			// 从原始密钥数据创建DESKeySpec对象
			DESKeySpec dks = new DESKeySpec(key);
			// 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
			SecretKeyFactory keyFactory = SecretKeyFactory
					.getInstance(KEY_ALGORITHM);
			SecretKey securekey = keyFactory.generateSecret(dks);

			// Cipher对象实际完成解密操作
			Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);

			// 用密钥初始化Cipher对象
			cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
			return cipher.doFinal(data);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return data;
	}


}
