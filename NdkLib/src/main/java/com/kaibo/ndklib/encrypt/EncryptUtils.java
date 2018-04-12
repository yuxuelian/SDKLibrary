package com.kaibo.ndklib.encrypt;

/**
 * @author Administrator
 * @date 2018/4/11 0011 下午 3:18
 * GitHub：
 * email：
 * description：
 */
public class EncryptUtils {

    static {
        System.loadLibrary("encrypt");
    }

    /**
     * 加密
     */
    public static native String encrypt(String text);

    /**
     * 解密
     */
    public static native String decrypt(String text);

}
