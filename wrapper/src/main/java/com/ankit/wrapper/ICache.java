package com.ankit.wrapper;



import java.util.Map;

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


        private long mTtl;
        private String mKey;
        private long timeStampMillis;
        /**  data from this response. */
        public final String response;

        /** The HTTP status code. */
        public final int statusCode;

        /** Response headers. */
        public final Map<String, String> headers;

        /** Network roundtrip time in milliseconds. */
        public final long networkTimeMs;


        /** Network roundtrip time in milliseconds. */
        public final int loadedFrom;



        public CacheEntry(Response<String> response, long cacheTime, String reqTAG, long timeStampMillis) {
            this.response = response.response;
            this.headers=response.headers;
            this.networkTimeMs= response.networkTimeMs;
            this.loadedFrom= response.loadedFrom;
            this.statusCode=response.statusCode;
            this.mTtl = cacheTime;
            this.mKey = reqTAG;
            this.timeStampMillis = timeStampMillis;
        }

        public String getData() {
            return this.response;
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
