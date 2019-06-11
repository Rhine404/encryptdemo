package com.rhine.demo.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rhine.demo.Redis.Redis;
import com.rhine.demo.utils.AESUtil;
import com.rhine.demo.utils.RSAUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 模拟服务器端
 */
public class Server {

    private Redis redis;

    private String AESKey;

    public Server(Redis redis) {
        this.redis = redis;
    }

    /**
     * 第一步 返回服务器端公钥
     **/
    public String getPubKey() {
        // 生成公钥与私钥
        String[] keyPair = RSAUtil.generateKeyPair();
        // 存储到Redis中
        redis.set("pubKey", keyPair[0]);
        redis.set("pvtKey", keyPair[1]);
        return keyPair[0];
    }

    /**
     * 第二步 处理客户端请求
     **/
    public String handler(String encryptData) {
        // 解析参数
        JSONObject jsonObj = JSON.parseObject(encryptData);
        String RSAKey = jsonObj.getString("key");
        String encryptedData = jsonObj.getString("value");

        String pvtKey = redis.get("pvtKey");
        // 解密RSAKey得到AESKey
        this.AESKey = RSAUtil.dncrypt(RSAKey, pvtKey);
        // 解密encryptedData得到data
        String data = AESUtil.dncrypt(AESKey, encryptedData);
        System.out.println("服务端解密数据：" + data);

        // 封装响应数据
        Map<String, String> map = new HashMap<>();
        map.put("msg", "hi client");
        map.put("code", "200");
        String encryptedResponse = AESUtil.encrypt(AESKey, JSON.toJSONString(map));
        System.out.println("服务端响应数据：" + encryptedResponse);

        // 响应
        return encryptedResponse;
    }
}
