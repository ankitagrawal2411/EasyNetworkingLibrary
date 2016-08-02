package com.ankit.wrapper;



import java.util.HashMap;

/** caching class
 * Created by ankit.agrawal on 11/07/16.
 */
  class MemoryCache implements ICache {
    private static final String TAG = MemoryCache.class.getName();

    /*
    * Get max available VM memory, exceeding this amount will throw an OutOfMemory exception.
    * and use 1/8th of the available memory for this memory cache.
    */
    public static final int DEFAULT_CACHE_SIZE = (int) (Runtime.getRuntime().maxMemory() / 8);

    private int maxSize = DEFAULT_CACHE_SIZE;
    private HashMap<String, CacheEntry> mEntries;

    protected MemoryCache() {
        initialize();
    }

    @Override
    public CacheEntry get(String key) {
        if (mEntries.containsKey(key)) {
            return mEntries.get(key);
        }
        return null;
    }

    @Override
    public void put(String key, CacheEntry newEntry) {
        if (newEntry.getCacheDuration() > getMaxSize()) { // don't bother caching
            return;
        }

        int totalSize = getCurrentSize();
        while (totalSize + newEntry.getCacheDuration() > getMaxSize()) { // have to reduce cache size
            CacheEntry entry = findLeastUsedEntry();
            if (entry != null) {
                remove(entry.getUrl());
            }
            totalSize = getCurrentSize();
        }

        synchronized (mEntries) {
            mEntries.put(key, newEntry);
        }
    }

    private CacheEntry findLeastUsedEntry() {
        CacheEntry leastRecentEntry = null;
        for (CacheEntry entry : mEntries.values()) {
            if (leastRecentEntry == null) {
                leastRecentEntry = entry;
            }
            else if (entry.getTimeStampMillis() < leastRecentEntry.getTimeStampMillis()) {
                leastRecentEntry = entry;
            }
        }
        return leastRecentEntry;
    }

    @Override
    public void initialize() {
        mEntries = new HashMap<>();
    }

    @Override
    public void remove(String key) {
        synchronized (mEntries) {
            if (mEntries.containsKey(key)) {
                mEntries.remove(key);
            }
        }
    }

    @Override
    public void clear() {
        mEntries.clear();
    }

    public int getCurrentSize() {
        int total = 0;
        for (CacheEntry entry : mEntries.values()) {
            total += entry.getCacheDuration();
        }
        return total;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }
}
