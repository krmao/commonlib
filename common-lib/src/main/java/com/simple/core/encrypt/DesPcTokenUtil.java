package com.simple.core.encrypt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

/**
 * pc用户登录Token
 * @author hejinguo
 * @version $Id: DesUserIdUtil.java, v 0.1 2019年11月17日 下午5:04:32
 */
public class DesPcTokenUtil {
    private static final Logger logger    = LoggerFactory.getLogger(DesPcTokenUtil.class);
    /**加密方式*/
    private final static String DES       = "DES";
    /**编码格式*/
    private final static String ENCODE    = "GBK";
    /**加密秘钥*/
    private final static String SECRETKEY = "pcMeetingLive";

    /**
     * 使用DES进行加密
     * @param data
     * @return
     */
    public static String encrypt(String data) {
        String encryptStr = "";
        try {
            byte[] bt = encrypt(data.getBytes(ENCODE), SECRETKEY.getBytes(ENCODE));
            encryptStr = byte2hex(bt);
        } catch (UnsupportedEncodingException e) {
            logger.error("使用EDS加密时编码格式不支持!", e);
        } catch (Exception e) {
            logger.error("使用EDS加密时异常!", e);
        }
        return encryptStr;
    }

    /**
     * 使用 默认key 解密
     * @param data 待解密数据
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static String decrypt(String data) {
        if (data == null) {
            return null;
        }
        String decryptStr = "";
        try {
            byte[] buf = data.getBytes();
            byte[] bt = decrypt(hex2byte(buf), SECRETKEY.getBytes(ENCODE));
            decryptStr = new String(bt, ENCODE);
        } catch (IOException e) {
            logger.error("使用EDS解密时IO异常!", e);
        } catch (Exception e) {
            logger.error("使用EDS解密时异常!", e);
        }
        return decryptStr;
    }

    /**
     * 根据键值进行加密
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        SecureRandom sr = new SecureRandom();
        //创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        //创建密钥工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance(DES);
        //初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
        return cipher.doFinal(data);
    }

    /**
     * Description 根据键值进行解密
     * 
     * @param data
     * @param key 加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        SecureRandom sr = new SecureRandom();
        //创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        //创建密钥工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance(DES);
        //初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
        return cipher.doFinal(data);
    }

    /**
     * 
     * @param b
     * @return
     */
    private static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs.toUpperCase();
    }

    private static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0)
            throw new IllegalArgumentException("长度不是偶数");
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

    /**
     * 程序入口
     * @param args
     */
    public static void main(String[] args) {
        String jiami = encrypt("123456");
        String jiemi = decrypt("A65D1539B99E815ECC555FC41A56F538");
        System.out.println(jiami);
        System.out.println(jiemi);
    }
}
