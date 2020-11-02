package com.teameleven.reverselecture2.server.client;

import com.google.common.hash.Funnel;
import com.google.common.hash.Funnels;
import com.google.common.hash.Hashing;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"UnstableApiUsage", "unchecked", "rawtypes"})
public class Call {

    public void hash(List<ApiService> services, Map<Long, String> pair) {

        Funnel<CharSequence> strFunnel = Funnels.stringFunnel(Charset.defaultCharset());

        Funnel<Long> longFunnel = Funnels.longFunnel();

        ArrayList<String> serverUrls = new ArrayList<>();
        for (ApiService service : services) {
            ApiServiceImpl d = (ApiServiceImpl) service;
            serverUrls.add(d.getUrl());
        }

        RendezvousHash<Long, String> rendezvousHash = new RendezvousHash(Hashing.md5(), longFunnel, strFunnel, serverUrls);

        for (Map.Entry<Long, String> entry : pair.entrySet()) {
            // returns url of cache
            String url = rendezvousHash.get(entry.getKey());
            services.get(serverUrls.indexOf(url)).put(entry.getKey(), entry.getValue());
            System.out.println("put(" + entry.getKey() + " => " + entry.getValue() + ") ==> routed to :" + rendezvousHash.get(entry.getKey()));
        }

        for (Map.Entry<Long, String> entry : pair.entrySet()) {
            String url = rendezvousHash.get(entry.getKey());
            System.out.println("get(" + entry.getKey() + ") => " + services.get(serverUrls.indexOf(url)).get(entry.getKey()));
        }

        System.out.println("\nValue Distribution:");
        for (int i = 0; i < services.size(); i++) {
            ApiServiceImpl service = (ApiServiceImpl) services.get(i);
            System.out.println("\nServer " + (char) ('A' + i) + " " + service.getUrl() + " =>\n" + services.get(i).getValues());
        }
    }
}
