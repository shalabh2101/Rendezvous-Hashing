package com.teameleven.reverselecture2.server.client;

import com.google.common.hash.HashFunction;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings({"UnstableApiUsage", "unused"})
public class SkeletonRendezvousHash {
    private int fanout;
    private HashFunction hashFunction;
    private int targetClusterSize;
    private int minClusterSize;
    private LinkedList<List<String>> clusters;
    private int virtualLevelCount;

    public SkeletonRendezvousHash(int fanout, HashFunction hashFunction, int targetClusterSize, int minClusterSize) {
        this.fanout = fanout;
        this.hashFunction = hashFunction;
        this.targetClusterSize = targetClusterSize;
        this.minClusterSize = minClusterSize;
    }

    private int logX(int value, int fanout) {
        return (int) (Math.log(value) / Math.log(fanout));
    }

    private int getVirtualLevelCount(int value, int fanout) {
        return (int) Math.ceil(this.logX(value, fanout));
    }

    private long hash(String key) {
        return hashFunction.hashString(key, StandardCharsets.UTF_8)
                .padToLong();
    }

    private List<String> getSites() {
        List<String> sites = new ArrayList<>();
        for (List<String> cluster : clusters) {
            sites.addAll(cluster);
        }
        return sites;
    }

    public void setSites(List<String> sitesToSet) {
        this.generateClusters(sitesToSet);
    }

    public void addSites(List<String> sitesToAdd) {
        List<String> sites = this.getSites();
        sites.addAll(sitesToAdd);
        this.generateClusters(sites);
    }

    public void removeSites(List<String> sitesToRemove) {
        final Map<String, Boolean> removeSiteLookup = new HashMap<>();

        for (String site : sitesToRemove) {
            removeSiteLookup.put(site, true);
        }

        List<String> sites = this.getSites().stream().filter(n -> !removeSiteLookup.get(n)).collect(Collectors.toList());
        this.generateClusters(sites);
    }

    public String findSite(String key, Integer salt) {
        String saltString;
        if (null != salt) {
            saltString = salt.toString();
        } else {
            salt = 0;
            saltString = "";
        }
        StringBuilder path = new StringBuilder();

        for (int i = 0; i < this.virtualLevelCount; i++) {
            long highestHash = Long.MIN_VALUE;
            int targetVirtualGroup = 0;

            for (int j = 0; j < this.fanout; j++) {
                long currentHash = this.hash(key + saltString + i + j);
                if (currentHash > highestHash) {
                    highestHash = currentHash;
                    targetVirtualGroup = j;
                }
            }
            path.append(targetVirtualGroup);
        }
        int targetClusterIndex = Integer.parseInt(path.toString(), this.fanout);
        List<String> targetCluster = this.clusters.get(targetClusterIndex);

        if (null == targetCluster) {
            if (targetClusterIndex == 0) {
                return null;
            }
            return this.findSite(key, salt + 1);
        }

        int keyIndexWithinCluster = this.findIndexWithHighestRandomWeight(key + salt + path, targetCluster);
        String targetSite = targetCluster.get(keyIndexWithinCluster);
        if (null == targetSite) {
            return this.findSite(key, salt + 1);
        }
        return targetSite;
    }

    private int findIndexWithHighestRandomWeight(String item, List<String> list) {
        int targetIndex = 0;
        long highestHash = Long.MIN_VALUE;

        if (null != list) {
            for (int index = 0; index < list.size(); index++) {
                String candidate = list.get(index);
                long currentHash = this.hash(item + candidate);
                if (currentHash > highestHash) {
                    highestHash = currentHash;
                    targetIndex = index;
                }
            }
        }
        return targetIndex;
    }

    private void generateClusters(List<String> sites) {
        final Map<String, Boolean> siteLookup = new HashMap<>();

        for (String site : sites) {
            siteLookup.put(site, false);
        }

        sites = sites.stream().filter(n -> {
            if (!siteLookup.get(n)) {
                siteLookup.put(n, true);
                return true;
            }
            return false;
        }).collect(Collectors.toList());

        Collections.sort(sites);

        this.clusters = new LinkedList<>();
        int clusterCount = (int) Math.ceil((double) sites.size() / (double) this.targetClusterSize);
        for (int i = 0; i < clusterCount; i++) {
            this.clusters.set(i, new ArrayList<>());
        }

        int clusterIndex = 0;
        for (String site : sites) {
            List<String> cluster = this.clusters.get(clusterIndex);
            cluster.add(site);
            if (cluster.size() >= targetClusterSize) {
                clusterIndex++;
            }
        }

        if (clusterCount > 1) {
            List<String> lastCluster = this.clusters.get(clusterCount - 1);

            if (lastCluster.size() < this.minClusterSize) {
                this.clusters.pop();
                clusterCount--;
                clusterIndex = 0;

                for (String site : lastCluster) {
                    List<String> cluster = this.clusters.get(clusterIndex);
                    cluster.add(site);
                    clusterIndex = (clusterIndex + 1) % clusterCount;
                }
            }
        }
        this.virtualLevelCount = this.getVirtualLevelCount(clusterCount, this.fanout);
    }


}
