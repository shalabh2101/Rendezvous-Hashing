package com.teameleven.reverselecture2.server.repository;

import com.teameleven.reverselecture2.server.entities.Entry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class ChronicleMapService implements ServiceInterface {
    private final Map<Long, Entry> chronicleMap;

    public ChronicleMapService(Map<Long, Entry> entries) {
        chronicleMap = entries;
    }

    @Override
    public Entry save(Entry newEntry) {
        checkNotNull(newEntry, "newEntry instance cannot be null");
        chronicleMap.putIfAbsent(newEntry.getKey(), newEntry);

        return newEntry;
    }

    @Override
    public Entry get(Long key) {
        checkArgument(key > 0, "Key is %s but expected greater than 0 value", key);
        return chronicleMap.get(key);
    }

    @Override
    public List<Entry> getAll() {
        return new ArrayList<>(chronicleMap.values());
    }
}
