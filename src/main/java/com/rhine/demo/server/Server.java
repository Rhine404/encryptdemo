package com.rhine.demo.server;

import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rhine.demo.Redis.Redis;
import com.rhine.demo.utils.AES;

import java.util.HashMap;
import java.util.Map;

/**
 * ģ���������
 */
public class Server {

    private Redis redis;

    private String AESKey;

    public Server(Redis redis) {
        this.redis = redis;
    }

    /**
     * ��һ�� ���ط������˹�Կ
     **/
    public String getPubKey() {
        // ���ɹ�Կ��˽Կ
        RSA rsa = new RSA();
        // �洢��Redis��
        redis.set("pvtKey", rsa.getPrivateKeyBase64());
        redis.set("pubKey", rsa.getPublicKeyBase64());
        return rsa.getPublicKeyBase64();
    }

    /**
     * �ڶ��� ����ͻ�������
     **/
    public String handler(String encryptData) {
        // ��������
        JSONObject jsonObj = JSON.parseObject(encryptData);
        String RSAKey = jsonObj.getString("key");
        String encryptedData =  jsonObj.getString("value");

        // ����RSAKey�õ�AESKey������encryptedData�õ�data
        String pvtKey = redis.get("pvtKey");
        RSA rsa = new RSA(pvtKey, null);
        byte[] decrypt = rsa.decrypt(HexUtil.decodeHex(RSAKey), KeyType.PrivateKey);
        this.AESKey = new String(decrypt);
        String data = AES.dncrypt(AESKey, encryptedData);
        System.out.println("����˽������ݣ�"+data);

        // ��װ��Ӧ����
        Map<String, String> map = new HashMap<>();
        map.put("msg", "hi client");
        map.put("code", "200");
        String encryptedResponse = AES.encrypt(AESKey, JSON.toJSONString(map));
        System.out.println("�������Ӧ���ݣ�"+encryptedResponse);

        // ��Ӧ
        return encryptedResponse;
    }

}
