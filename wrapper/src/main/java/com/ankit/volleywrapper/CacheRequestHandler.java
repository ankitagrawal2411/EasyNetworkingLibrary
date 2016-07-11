/**
 *
 */
package com.ankit.volleywrapper;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ankitagrawal on 6/7/16. yay
 */
public class CacheRequestHandler implements ICacheRequest {

    private  MemoryCache mMemoryCache;
    private static CacheRequestHandler mInstance;
     static CacheRequestHandler getInstance()
    {
        if(mInstance==null) {
            mInstance = new CacheRequestHandler();
        }
        return mInstance;
    }
    private CacheRequestHandler(){
        mMemoryCache = new MemoryCache();
    }
    public MemoryCache getMemoryCache() {
        return mMemoryCache;
    }
  @Override
    public void makeJsonRequest(final Context context,int method, final String URL, JSONObject jsonObject,final HashMap<String,String> header, final IRequestListener<JSONObject> jsonRequestFinishedListener,final RetryPolicy retryPolicy,final String reqTAG, final int memoryPolicy,final int networkPolicy)
    {
        if(!checkForInternetConnection(context)){
            jsonRequestFinishedListener.onRequestErrorCode(null);
            return;
        }
        if(MemoryPolicy.shouldReadFromMemoryCache(memoryPolicy)){
            ICache.CacheEntry response =   mMemoryCache.get(reqTAG);
            String data = response.getData();
            if (!TextUtils.isEmpty(data)) {
                try {
                    JSONObject jsonObject1 = new JSONObject(data);
                    jsonRequestFinishedListener.onRequestSuccess(jsonObject1);
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        if(NetworkPolicy.shouldReadFromDiskCache(networkPolicy)){
            String response = BaseCacheRequestManager.getInstance(context).getCacheResponse(reqTAG);
            if (!TextUtils.isEmpty(response)) {
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    jsonRequestFinishedListener.onRequestSuccess(jsonObject1);
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        RequestHandler.getInstance(context).makeJsonRequest(method, URL, jsonObject, new IRequestListener<JSONObject>() {
            @Override
            public Object onRequestSuccess(final JSONObject response) {
                if(response==null){
                    jsonRequestFinishedListener.onRequestErrorCode(new VolleyError("null " +
                            "response"));
                    return null;
                }
                ParserTask parserTask =new ParserTask(reqTAG, new IParserListener() {
                    @Override
                    public void onParseSuccess(String requestTag, Object parseData) {
                        jsonRequestFinishedListener.onParseSuccess(parseData);
                    }

                    @Override
                    public void onParseError(String requestTag, int errorCode) {
                        jsonRequestFinishedListener.onRequestErrorCode(null);
                    }

                    @Override
                    public Object onParse(String requestTag) {
                        if (NetworkPolicy.shouldWriteToDiskCache(networkPolicy)) {
                            BaseCacheRequestManager.getInstance(context).cacheResponse(new
                                    ICache.CacheEntry(response.toString(),0,reqTAG,System.currentTimeMillis
                                    ()));
                            BaseCacheRequestManager.getInstance(context).cacheResponse(reqTAG, response);
                        }
                        return jsonRequestFinishedListener.onRequestSuccess(response);
                    }
                });

                parserTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                // this is useless too so return null from it as we are returning data from
                // parser task callbacks
                return null;
            }

            @Override
            public String onNetworkResponse(NetworkResponse response) {
                return jsonRequestFinishedListener.onNetworkResponse(response);
            }

            @Override
            public void onParseSuccess(Object response) {
                // this is never called dont use it
            }

            @Override
            public void onRequestErrorCode(VolleyError volleyError) {
                jsonRequestFinishedListener.onRequestErrorCode(volleyError);
            }
        }, header, retryPolicy, reqTAG);

    }


    private boolean checkForInternetConnection(Context context) {
        return Utils.isConnected(context);

    }
    @Override
    public void makeStringRequest(final Context context,int method, final String URL, String jsonObject,final HashMap<String,String> header, final IRequestListener<String> jsonRequestFinishedListener,final RetryPolicy retryPolicy,final String reqTAG,final int memoryPolicy,final int networkPolicy)
    {
        if(!checkForInternetConnection(context)){
            jsonRequestFinishedListener.onRequestErrorCode(null);
            return;
        }
        if(NetworkPolicy.shouldReadFromDiskCache(networkPolicy)){
            String response = BaseCacheRequestManager.getInstance(context).getCacheResponse(reqTAG);
            if (!TextUtils.isEmpty(response)) {
                    jsonRequestFinishedListener.onRequestSuccess(response);
                    return;
            }
        }
        RequestHandler.getInstance(context).makeStringRequest(method, URL, jsonObject, new IRequestListener<String>() {
            @Override
            public Object onRequestSuccess(final String response) {

                new ParserTask(reqTAG, new IParserListener() {
                    @Override
                    public void onParseSuccess(String requestTag, Object parseData) {
                        jsonRequestFinishedListener.onParseSuccess(parseData);
                    }

                    @Override
                    public void onParseError(String requestTag, int errorCode) {
                        jsonRequestFinishedListener.onRequestErrorCode(null);
                    }

                    @Override
                    public Object onParse(String requestTag) {
                        if(NetworkPolicy.shouldReadFromDiskCache(networkPolicy)){
                            BaseCacheRequestManager.getInstance(context).cacheResponse(reqTAG, response);
                        }
                        return jsonRequestFinishedListener.onRequestSuccess(response);
                    }
                }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                // this is useless too so return null from it as we are returning data from
                // parser task callbacks
                return null;
            }

            @Override
            public void onParseSuccess(Object response) {
                // this is never called dont use it
            }

            @Override
            public String onNetworkResponse(NetworkResponse response) {
                return jsonRequestFinishedListener.onNetworkResponse(response);
            }

            @Override
            public void onRequestErrorCode(VolleyError volleyError) {
                jsonRequestFinishedListener.onRequestErrorCode(volleyError);
            }
        }, header, retryPolicy, reqTAG);

    }


}