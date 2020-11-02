package com.teameleven.reverselecture2.server.client;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;

/**
 * Distributed service
 */
public class ApiServiceImpl implements ApiService {
    private final String serverUrl;

    public ApiServiceImpl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    /**
     * @see ApiService#get(long)
     */
    @Override
    public String get(long key) {
        HttpResponse<JsonNode> response = null;
        try {
            response = Unirest.get(this.serverUrl + "/cache/{key}")
                    .header("accept", "application/json")
                    .routeParam("key", Long.toString(key)).asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return response != null ? response.getBody().getObject().getString("value") : null;
    }

    /**
     * @see ApiService#put(long,
     * java.lang.String)
     */
    @Override
    public void put(long key, String value) {
        HttpResponse<JsonNode> response = null;
        try {
            response = Unirest
                    .put(this.serverUrl + "/cache/{key}/{value}")
                    .header("accept", "application/json")
                    .routeParam("key", Long.toString(key))
                    .routeParam("value", value).asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        if (null == response || response.getCode() != 200) {
            System.out.println("Failed to add to the cache.");
        }
    }

    @Override
    public String getValues() {
        HttpResponse<JsonNode> response = null;
        try {
            response = Unirest.get(this.serverUrl + "/cache")
                    .header("accept", "application/json").asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        StringBuilder valuesBuilder = new StringBuilder().append("Values:");
        StringBuilder keyBuilder = new StringBuilder().append("Keys:");
        int count = 0;
        if (null != response) {
            JSONArray array = response.getBody().getArray();
            for (int length = 0; length < array.length(); length++) {
                valuesBuilder.append(" ").append(array.getJSONObject(length).getString("value"));
                keyBuilder.append(" ").append(array.getJSONObject(length).getInt("key"));
                count++;
            }
        }

        return valuesBuilder.toString() + "\n" + keyBuilder.toString() + "\n" + count;
    }

    public String getUrl() {
        return serverUrl;
    }
}
