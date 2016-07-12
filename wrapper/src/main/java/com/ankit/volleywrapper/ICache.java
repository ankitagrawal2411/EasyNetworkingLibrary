package com.ankit.volleywrapper;

/**
 * Created by asifmujteba on 07/08/15.
 */
public interface ICache {
    CacheEntry get(String key);
    void put(String key, CacheEntry entry);
    void initialize();
    void remove(String key);
    void clear();
    int getCurrentSize();
    int getMaxSize();
    void setMaxSize(int maxSize);

    class CacheEntry {
        private static final String TAG = CacheEntry.class.getName();

        private String mData;
        private long mTtl;
        private String mKey;
        private long timeStampMillis;

        public CacheEntry(String data, long time, String url, long timeStampMillis) {
            this.mData = data;
            this.mTtl = time;
            this.mKey = url;
            this.timeStampMillis = timeStampMillis;
        }

        public String getData() {
            return mData;
        }

        public long getCacheDuration() {
            return mTtl;
        }

        public String getUrl() {
            return mKey;
        }

        public long getTimeStampMillis() {
            return timeStampMillis;
        }
    }
}
