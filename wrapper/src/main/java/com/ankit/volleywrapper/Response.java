package com.ankit.volleywrapper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ankitagrawal on 21/7/16.
 */
public class Response<T> {

    /**  data from this response. */
    private final T response;

    /** The HTTP status code. */
    public final int statusCode;

    /** Response headers. */
    public final Map<String, String> headers;

    /** Network roundtrip time in milliseconds. */
    public final long networkTimeMs;


    /** Network roundtrip time in milliseconds. */
    public final int loadedFrom;
    public Response(T response, Map<String, String> headers, int statusCode, long networkTimeMs,
                    int loadedFrom) {
        this.response = response;
        this.headers = headers;
        this.statusCode = statusCode;
        this.networkTimeMs = networkTimeMs;
        this.loadedFrom = loadedFrom;
    }
    public Response(T response, int loadedFrom) {
        this.response = response;
        this.headers = new HashMap<>();
        this.statusCode = 200;
        this.networkTimeMs = 0;
        this.loadedFrom = loadedFrom;
    }




    protected class LoadedFrom {
        public static final int MEMORY=0;
        public static final int DISK=1;
        public static final int NETWORK=2;
    }
}
