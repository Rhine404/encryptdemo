package com.rhine.demo.main;

import com.rhine.demo.Redis.Redis;
import com.rhine.demo.client.Client;
import com.rhine.demo.server.Server;

/**
 * @Author rhine
 * @Description TODO
 * @Date Created in 15:13 2019/6/11
 */
public class Main {
    public static void main(String[] args) {
        Redis redis = new Redis();
        Server server = new Server(redis);
        Client client = new Client(server);

        // ��һ�� ��ȡ����˹�Կ
        client.getPubKey();
        // �ڶ��� ��������
        String encryptData = client.encrypt();
        // ������ �������ݣ�������Ӧ��Ϣ
        client.send(encryptData);
    }
}
