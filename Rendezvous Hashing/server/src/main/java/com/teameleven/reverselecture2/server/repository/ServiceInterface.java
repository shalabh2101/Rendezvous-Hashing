package com.teameleven.reverselecture2.server.repository;

import com.teameleven.reverselecture2.server.entities.Entry;

import java.util.List;

/**
 * Entry repository interface.
 * <p>
 * What is repository pattern?
 *
 * @see http://martinfowler.com/eaaCatalog/repository.html
 */
public interface ServiceInterface {
    /**
     * Save a new entry in the repository
     *
     * @param newEntry a entry instance to be create in the repository
     * @return an entry instance
     */
    Entry save(Entry newEntry);

    /**
     * Retrieve an existing entry by key
     *
     * @param key a valid key
     * @return a entry instance
     */
    Entry get(Long key);

    /**
     * Retrieve all entries
     *
     * @return a list of entries
     */
    List<Entry> getAll();

}
