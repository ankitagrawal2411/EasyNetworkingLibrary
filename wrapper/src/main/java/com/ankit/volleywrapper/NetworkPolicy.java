package com.ankit.volleywrapper;

/**
 * Created by ankitagrawal on 11/7/16.
 */
/** Designates the policy to use for network requests. */
@SuppressWarnings("PointlessBitwiseExpression")
public enum NetworkPolicy {

    /** Skips checking the disk cache and forces loading through the network. */
    CACHE(1 << 0),
   // 01 (1)

    /**
     * Skips storing the result into the disk cache.
     * <p>
     * <em>Note</em>: At this time this is only supported if you are using OkHttp.
     */
    STORE(1 << 1),
    //10 (2)

    /** Forces the request through the disk cache only, skipping network. */
    OFFLINE(1 << 2);
    //100 (4)

    public static boolean shouldReadFromDiskCache(int networkPolicy) {
        return (networkPolicy & NetworkPolicy.CACHE.index) != 0;
    }

    public static boolean shouldWriteToDiskCache(int networkPolicy) {
        return (networkPolicy & NetworkPolicy.STORE.index) != 0;
    }

    public static boolean isOfflineOnly(int networkPolicy) {
        return (networkPolicy & NetworkPolicy.OFFLINE.index) != 0;
    }

    final int index;

    private NetworkPolicy(int index) {
        this.index = index;
    }
}

