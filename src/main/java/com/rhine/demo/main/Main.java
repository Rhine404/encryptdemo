package com.rhine.demo.main;

import com.rhine.demo.Redis.Redis;
import com.rhine.demo.client.Client;
import com.rhine.demo.server.Server;

/**
 * 启动类
 */
public class Main {
    public static void main(String[] args) {
        Redis redis = new Redis();
        Server server = new Server(redis);
        Client client = new Client(server);

        // 第一步 获取服务端公钥
        client.getPubKey();
        // 第二步 加密数据
        String encryptData = client.encrypt();
        // 第三步 发送数据，接收响应信息
        client.send(encryptData);
    }
}
