package com.rhine.demo.utils;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

/**
 * @Author rhine
 * @Description TODO
 * @Date Created in 17:42 2019/6/11
 */
public class RSAUtil {

    public static String[] generateKeyPair() {
        String[] keyPair = new String[2];
        RSA rsa = new RSA();
        keyPair[0] = rsa.getPublicKeyBase64();
        keyPair[1] = rsa.getPrivateKeyBase64();
        return keyPair;
    }

    public static String encrypt(String data, String pubKey) {
        cn.hutool.crypto.asymmetric.RSA rsa = new cn.hutool.crypto.asymmetric.RSA(null, pubKey);
        byte[] encrypt = rsa.encrypt(StrUtil.bytes(data), KeyType.PublicKey);
        // 这里将byte转换为十六进制
        return HexUtil.encodeHexStr(encrypt);
    }

    public static String dncrypt(String data, String pvtKey) {
        RSA rsa = new RSA(pvtKey, null);
        byte[] decrypt = rsa.decrypt(HexUtil.decodeHex(data), KeyType.PrivateKey);
        return new String(decrypt);
    }

}
