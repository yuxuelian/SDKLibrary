package com.kaibo.ndklib.encrypt;

/**
 * @author:Administrator
 * @date:2018/4/11 0011 下午 3:18
 * GitHub:
 * email:
 * description:
 */
public class EncryptUtils {

    static {
        System.loadLibrary("encrypt");
    }

    private EncryptUtils() {

    }

    private static class InstanceHelper {
        private static EncryptUtils encryptUtils = new EncryptUtils();
    }

    public static EncryptUtils getInstance() {
        return InstanceHelper.encryptUtils;
    }

    /**
     * 加密
     */
    public native String encrypt(String text);

    /**
     * 解密
     */
    public native String decrypt(String text);

}
