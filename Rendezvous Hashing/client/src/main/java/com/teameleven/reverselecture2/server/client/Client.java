package com.teameleven.reverselecture2.server.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Client {

    public static void main(String[] args) {
        System.out.println("\nStarting Client...\n");

        List<ApiService> nodes = new ArrayList<>();

        Map<Long, String> pair = new HashMap<>();

        Call call = new Call();

        nodes.add(new ApiServiceImpl("http://localhost:3000"));
        nodes.add(new ApiServiceImpl("http://localhost:3001"));
        nodes.add(new ApiServiceImpl("http://localhost:3002"));
        nodes.add(new ApiServiceImpl("http://localhost:3003"));
        nodes.add(new ApiServiceImpl("http://localhost:3004"));
        nodes.add(new ApiServiceImpl("http://localhost:3005"));
        nodes.add(new ApiServiceImpl("http://localhost:3006"));

        for (int i = 1; i <= 10000; i++) {
            pair.put((long) i, "Node" + i);
        }

        call.hash(nodes, pair);
    }


}