package com.ankit.wrapper;

/**
 * Created by ankitagrawal on 11/7/16.
 */
/** Designates the policy to use when dealing with memory cache. */
@SuppressWarnings("PointlessBitwiseExpression")
public enum MemoryPolicy {

    /** Skips memory cache lookup when processing a request. */
    CACHE(1 << 0),
    /**
     * Skips storing the final result into memory cache. Useful for one-off requests
     * to avoid evicting other bitmaps from the cache.
     */
    STORE(1 << 1);

    static boolean shouldReadFromMemoryCache(int memoryPolicy) {
        return (memoryPolicy & MemoryPolicy.CACHE.index) != 0;
    }

    static boolean shouldWriteToMemoryCache(int memoryPolicy) {
        return (memoryPolicy & MemoryPolicy.STORE.index) != 0;
    }

    final int index;

    private MemoryPolicy(int index) {
        this.index = index;
    }
}

