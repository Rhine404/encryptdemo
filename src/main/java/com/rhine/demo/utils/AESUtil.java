package com.rhine.demo.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

/**
 * @Author rhine
 * @Description TODO
 * @Date Created in 15:52 2019/6/11
 */
public class AESUtil {

    public static String encrypt(String key, String content) {
        byte[] aesKey = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue(), key.getBytes()).getEncoded();
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, aesKey);
        String encryptHex = aes.encryptHex(content);
        return encryptHex;
    }

    public static String dncrypt(String key, String content) {
        byte[] aesKey = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue(), key.getBytes()).getEncoded();
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, aesKey);
        String decryptStr = aes.decryptStr(content, CharsetUtil.CHARSET_UTF_8);
        return decryptStr;
    }
}
