package com.rhine.demo.client;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.alibaba.fastjson.JSON;
import com.rhine.demo.server.Server;
import com.rhine.demo.utils.AES;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 模拟客户端
 */
public class Client {

    private Server server;

    private String pubKey;
    private String AESKey;

    public Client(Server server) {
        this.server = server;
    }

    /**
     * 第一步 获取服务器公钥
     **/
    public void getPubKey() {
        this.pubKey = server.getPubKey();
    }

    /**
     * 第二步 加密数据
     **/
    public String encrypt() {
        this.AESKey = getRandomString(16);

        // 加密AESKey为RSAkey
        RSA rsa = new RSA(null, pubKey);
        byte[] encrypt = rsa.encrypt(StrUtil.bytes(AESKey), KeyType.PublicKey);
        // 这里将byte转换为十六进制
        String RSAKey = HexUtil.encodeHexStr(encrypt);

        // 使用AESKey加密数据
        String data = "hello server";
        System.out.println("明文数据："+data);
        String encryptedData = AES.encrypt(AESKey, data);

        // 封装数据
        Map<String, String> map = new HashMap<>();
        map.put("key", RSAKey);
        map.put("value", encryptedData);
        return JSON.toJSONString(map);
    }

    /**
     * 第三步 发送数据
     **/
    public void send(String encryptData) {
        System.out.println("客户端实际发送数据："+encryptData);
        String response = server.handler(encryptData);
        String data = AES.dncrypt(AESKey, response);
        System.out.println("客户端解密响应数据："+data);
    }

    /**
     * 获取随机字符串
     **/
    private static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
