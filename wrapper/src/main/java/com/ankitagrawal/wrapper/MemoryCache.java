package com.ankitagrawal.wrapper;



import android.os.SystemClock;

import java.util.HashMap;

/** caching class
 * Created by ankit.agrawal on 11/07/16.
 */
  class MemoryCache implements Cache {
    private static final String TAG = MemoryCache.class.getName();

    /*
    * Get max available VM memory, exceeding this amount will throw an OutOfMemory exception.
    * and use 1/30th of the available memory for this memory cache.
    */
    public static final int DEFAULT_CACHE_SIZE = (int) (Runtime.getRuntime().maxMemory() / 30);

    private int maxSize = DEFAULT_CACHE_SIZE;
    private HashMap<String, CacheEntry> mEntries;

    protected MemoryCache() {
        initialize();
    }

    @Override
    public CacheEntry get(String key) {
        if (mEntries.containsKey(key)) {
            CacheEntry entry = mEntries.get(key);
            Logger.getInstance().d("cache valid",(entry.getCacheDuration() + entry.getTimeStampMillis() > SystemClock.elapsedRealtime())+" time");
            if (entry.getCacheDuration() + entry.getTimeStampMillis() > SystemClock.elapsedRealtime() || entry.getCacheDuration() == 0) {
                return entry;
            } else {
                remove(key);
                return null;
            }
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
