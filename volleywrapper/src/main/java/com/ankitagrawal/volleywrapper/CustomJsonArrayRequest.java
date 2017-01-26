package com.ankitagrawal.volleywrapper;

import com.android.volley.Response;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by ankitagrawal on 1/25/17.
 */
public abstract class CustomJsonArrayRequest<T> extends CustomJsonRequest<T> {

    /**
     * Constructor which defaults to <code>GET</code> if <code>jsonRequest</code> is
     * <code>null</code>, <code>POST</code> otherwise.
     *
     */
    public CustomJsonArrayRequest(String url, Map<String, String> headers, JSONObject jsonObject, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(url, headers, jsonObject, listener, errorListener);
    }
    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param url URL of the request to make
     * @param headers Map of request headers
     */
    public CustomJsonArrayRequest(int method, String url, Map<String, String> headers,
                             JSONObject jsonObject,
                             Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, headers, (jsonObject == null) ? null : jsonObject.toString(), listener, errorListener);

    }
    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param url URL of the request to make
     * @param headers Map of request headers
     */
    public CustomJsonArrayRequest(int method, String url, Map<String, String> headers,
                             String jsonObject,
                             Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url,headers,jsonObject,listener, errorListener);
    }


}
