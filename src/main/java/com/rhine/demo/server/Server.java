package com.rhine.demo.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rhine.demo.Redis.Redis;
import com.rhine.demo.utils.AESUtil;
import com.rhine.demo.utils.RSAUtil;

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
        String[] keyPair = RSAUtil.generateKeyPair();
        // �洢��Redis��
        redis.set("pubKey", keyPair[0]);
        redis.set("pvtKey", keyPair[1]);
        return keyPair[0];
    }

    /**
     * �ڶ��� ����ͻ�������
     **/
    public String handler(String encryptData) {
        // ��������
        JSONObject jsonObj = JSON.parseObject(encryptData);
        String RSAKey = jsonObj.getString("key");
        String encryptedData = jsonObj.getString("value");

        String pvtKey = redis.get("pvtKey");
        // ����RSAKey�õ�AESKey
        this.AESKey = RSAUtil.dncrypt(RSAKey, pvtKey);
        // ����encryptedData�õ�data
        String data = AESUtil.dncrypt(AESKey, encryptedData);
        System.out.println("����˽������ݣ�" + data);

        // ��װ��Ӧ����
        Map<String, String> map = new HashMap<>();
        map.put("msg", "hi client");
        map.put("code", "200");
        String encryptedResponse = AESUtil.encrypt(AESKey, JSON.toJSONString(map));
        System.out.println("�������Ӧ���ݣ�" + encryptedResponse);

        // ��Ӧ
        return encryptedResponse;
    }
}
