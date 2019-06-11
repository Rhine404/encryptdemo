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
 * ģ��ͻ���
 */
public class Client {

    private Server server;

    private String pubKey;
    private String AESKey;

    public Client(Server server) {
        this.server = server;
    }

    /**
     * ��һ�� ��ȡ��������Կ
     **/
    public void getPubKey() {
        this.pubKey = server.getPubKey();
    }

    /**
     * �ڶ��� ��������
     **/
    public String encrypt() {
        this.AESKey = getRandomString(16);

        // ����AESKeyΪRSAkey
        RSA rsa = new RSA(null, pubKey);
        byte[] encrypt = rsa.encrypt(StrUtil.bytes(AESKey), KeyType.PublicKey);
        // ���ｫbyteת��Ϊʮ������
        String RSAKey = HexUtil.encodeHexStr(encrypt);

        // ʹ��AESKey��������
        String data = "hello server";
        System.out.println("�������ݣ�"+data);
        String encryptedData = AES.encrypt(AESKey, data);

        // ��װ����
        Map<String, String> map = new HashMap<>();
        map.put("key", RSAKey);
        map.put("value", encryptedData);
        return JSON.toJSONString(map);
    }

    /**
     * ������ ��������
     **/
    public void send(String encryptData) {
        System.out.println("�ͻ���ʵ�ʷ������ݣ�"+encryptData);
        String response = server.handler(encryptData);
        String data = AES.dncrypt(AESKey, response);
        System.out.println("�ͻ��˽�����Ӧ���ݣ�"+data);
    }

    /**
     * ��ȡ����ַ���
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
