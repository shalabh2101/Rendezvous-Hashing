package com.teameleven.reverselecture2.server.client;

/**
 * Service Interface
 */
public interface ApiService {

    String get(long key);

    void put(long key, String value);

    String getValues();

}
