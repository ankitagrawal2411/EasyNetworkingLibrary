package com.ankit.volleywrapper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RetryPolicy;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ankitagrawal on 1/7/16.
 */
public class RequestBuilder implements IBuild {
    private int method = Request.Method.POST;
    private String requestUrl;
    private JSONObject jsonObject;
    private IRequestListener<JSONObject> iRequestListener;
    private HashMap<String, String> requestHeader;
    private RetryPolicy retryPolicy;
    private String reqTAG;
    private boolean shouldCache;

    public RequestBuilder(String requestUrl, String reqTAG) {
        this.requestUrl = requestUrl;
        this.reqTAG = reqTAG;
    }

    public RequestBuilder() {

    }
    public RequestBuilder(String requestUrl) {
        this.requestUrl = requestUrl;
    }
    public RequestBuilder(RequestBuilder builder) {
        method = builder.method;
        requestUrl = builder.requestUrl;
        jsonObject = builder.jsonObject;
        iRequestListener = builder.iRequestListener;
        requestHeader = builder.requestHeader;
        retryPolicy = builder.retryPolicy;
        reqTAG = builder.reqTAG;
        shouldCache = builder.shouldCache;
    }

    public static RequestBuilder newBuilder(RequestBuilder copy) {
        RequestBuilder builder = new RequestBuilder();
        builder.shouldCache = copy.shouldCache;
        builder.reqTAG = copy.reqTAG;
        builder.retryPolicy = copy.retryPolicy;
        builder.requestHeader = copy.requestHeader;
        builder.iRequestListener = copy.iRequestListener;
        builder.jsonObject = copy.jsonObject;
        builder.requestUrl = copy.requestUrl;
        builder.method = copy.method;
        return builder;
    }

    /**
     * Sets the {@code shouldCache} and returns a reference to {@code IBuild}
     *
     * @param val the {@code shouldCache} to set
     * @return a reference to this Builder
     */
    @Override
    public IBuild withShouldCache(boolean val) {
        shouldCache = val;
        return this;
    }

    /**
     * Sets the {@code reqTAG} and returns a reference to {@code IShouldCache}
     *
     * @param val the {@code reqTAG} to set
     * @return a reference to this Builder
     */
    @Override
    public IBuild withReqTAG(@NonNull String val) {
        reqTAG = val;
        return this;
    }

    /**
     * Sets the {@code retryPolicy} and returns a reference to {@code IReqTAG}
     *
     * @param val the {@code retryPolicy} to set
     * @return a reference to this Builder
     */
    @Override
    public IBuild withRetryPolicy(@Nullable RetryPolicy val) {
        retryPolicy = val;
        return this;
    }

    /**
     * Sets the {@code requestHeader} and returns a reference to {@code IRetryPolicy}
     *
     * @param val the {@code requestHeader} to set
     * @return a reference to this Builder
     */
    @Override
    public IBuild withRequestHeader(@Nullable HashMap<String, String> val) {
        requestHeader = val;
        return this;
    }

    /**
     * Sets the {@code iFcRequestListener} and returns a reference to {@code IRequestHeader}
     *
     * @param val the {@code iFcRequestListener} to set
     * @return a reference to this Builder
     */
    @Override
    public IBuild withIFcRequestListener(@NonNull IRequestListener<JSONObject> val) {
        iRequestListener = val;
        return this;
    }

    /**
     * Sets the {@code jsonObject} and returns a reference to {@code IIFcRequestListener}
     *
     * @param val the {@code jsonObject} to set
     * @return a reference to this Builder
     */
    @Override
    public IBuild withJsonObject(@Nullable JSONObject val) {
        jsonObject = val;
        return this;
    }

    /**
     * Sets the {@code requestUrl} and returns a reference to {@code IJsonObject}
     *
     * @param val the {@code requestUrl} to set
     * @return a reference to this Builder
     */
    @Override
    public IBuild withRequestUrl(@NonNull String val) {
        requestUrl = val;
        return this;
    }

    @Override
    public IBuild withPostRequest(JSONObject val) {
        jsonObject = val;
        withMethod(Request.Method.POST);
        return this;
    }

    @Override
    public IBuild withGetRequest() {
        withMethod(Request.Method.GET);
        return this;
    }

    /**
     * Sets the {@code method} and returns a reference to {@code IRequestUrl}
     *
     * @param val the {@code method} to set
     * @return a reference to this Builder
     */
    @Override
    public IBuild withMethod(int val) {
        method = val;
        return this;
    }

    /**
            * Returns a {@code RequestBuilder} built from the parameters previously set.
            *
            * @return a {@code RequestBuilder} built with parameters of this {@code RequestBuilder.Builder}
    */
    public RequestBuilder build() {
         return this;
    }
    public void send(@NonNull Context context) {
        if(context==null){
            throw  new NullPointerException("context is null");
        }
        CacheRequestHandler.getInstance().makeJsonRequest(context, method, requestUrl,
                jsonObject, requestHeader, iRequestListener, retryPolicy, reqTAG,shouldCache);
    }
}

/*

    public RequestBuilder params(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }
    public RequestBuilder requestUrl( String requestUrl) {
        this.requestUrl = requestUrl;
        return this;
    }
    public RequestBuilder requestUrl( int method) {
        this.method = method;
        return this;
    }
    public RequestBuilder cache( boolean shouldCache) {
        this.shouldCache = shouldCache;
        return this;
    }
    public RequestBuilder listener( IFcRequestListener<JSONObject>  iFcRequestListener) {
        this.iFcRequestListener = iFcRequestListener;
        return this;
    }
    public RequestBuilder retryPolicy( RetryPolicy retryPolicy) {
        this.retryPolicy = retryPolicy;
        return this;
    }

    public RequestBuilder requestHeader(HashMap<String, String> requestHeader) {
        this.requestHeader = requestHeader;
        return this;
    }

    public void send(Context context) {
                 CacheRequestHandler.getInstance().makeJsonRequest(context, method, requestUrl,
                         jsonObject, requestHeader, iFcRequestListener, retryPolicy, reqTAG,shouldCache);
    }*/


