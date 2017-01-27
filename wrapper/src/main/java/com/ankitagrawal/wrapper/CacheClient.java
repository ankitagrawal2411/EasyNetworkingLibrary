/**
 *
 */
package com.ankitagrawal.wrapper;

import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.text.TextUtils;

import com.ankitagrawal.wrapper.exception.NoClientFoundException;
import com.ankitagrawal.wrapper.exception.NoConverterFoundException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ankitagrawal on 6/7/16. yay
 */
public class CacheClient implements CacheRequest {

    private MemoryCache mMemoryCache;
    private static CacheClient mInstance;
    private ArrayList<Client> clients;
    private ArrayList<Converter> converters;
    private RetryPolicy retryPolicy;
    private int memoryPolicy;
    private int networkPolicy;
    private String mBaseUrl;
    private HashMap<String, Integer> mRequestInQueue;
    private HashMap<String, String> mHeaders;
    private boolean singleRequestMode;

    static CacheClient getInstance() {
        if (mInstance == null) {
            mInstance = new CacheClient();
        }
        return mInstance;
    }

    private CacheClient() {
        mMemoryCache = new MemoryCache();
        singleRequestMode = true;
    }

    public MemoryCache getMemoryCache() {
        return mMemoryCache;
    }

    @Override
    public <T, F> void makeJsonRequest(Context context,Client client, int method, String URL, JSONObject
            jsonObject,
                                       HashMap<String, String> header, RetryPolicy retryPolicy, String reqTAG, int memoryPolicy, int networkPolicy, long cacheTime, BaseResponseListener<T, F> responseListener, int logLevel, boolean mCancel, Class<F> aClass) {
        if (!checkRequestQueue(reqTAG, responseListener, mCancel)) {
            return;
        }
        Logger.getInstance().setLocalLevel(logLevel);
        if (memoryPolicy == 0) {
            memoryPolicy = this.memoryPolicy;
        }
        if (networkPolicy == 0) {
            networkPolicy = this.networkPolicy;
        }
        boolean offlineOnly = NetworkPolicy.isOfflineOnly(networkPolicy);
        if (MemoryPolicy.shouldReadFromMemoryCache(memoryPolicy) || offlineOnly) {
            Cache.CacheEntry entry = mMemoryCache.get(reqTAG);
            String data = entry.getData();
            JSONObject jsonObject1 = parseJson(data);
            if (jsonObject1 != null) {
                onResponse(context, new Response<>(jsonObject1, Response.LoadedFrom.MEMORY), reqTAG, cacheTime, aClass, memoryPolicy, networkPolicy, responseListener);
                return;
            }
        }
        if (NetworkPolicy.shouldReadFromDiskCache(networkPolicy) || offlineOnly) {
            String response = CacheRequestManager.getInstance(context).getCacheResponse(reqTAG);
            JSONObject jsonObject1 = parseJson(response);
            if (jsonObject1 != null) {
                onResponse(context, new Response<>(jsonObject1, Response.LoadedFrom.DISK), reqTAG, cacheTime, aClass, memoryPolicy, networkPolicy, responseListener);
                return;
            }
        }
        if (offlineOnly) {
            if (responseListener != null) {
                responseListener.onRequestErrorCode(ErrorCode.OFFLINE_ONLY_ERROR);
            }
            return;
        }
        if (!checkForInternetConnection(context)) {
            if (responseListener != null) {
                responseListener.onRequestErrorCode(ErrorCode.NO_CONNECTION_ERROR);
            }
            return;
        }
        boolean requestHandled = false;
        if(client!=null){
            if (client.canHandleRequest(URL, method)) {
                sendJsonRequest(context, client, method, URL, jsonObject, header, responseListener, retryPolicy, reqTAG, memoryPolicy, networkPolicy, cacheTime, aClass);
                return;
            }else{
                throw new IllegalArgumentException("the provided client cannot handle " +
                        "this type of request, please set  client that can " +
                        "handle this type of request" +
                        " " + "by Changing the client.canHandleRequest(URL, method) method");
            }
        }
        if ((clients == null || clients.size() == 0)) {
            throw new NoClientFoundException("no client set, please set one at least " +
                    "through GlobalBuilder class");
        }

        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).canHandleRequest(URL, method)) {
                sendJsonRequest(context, clients.get(i), method, URL, jsonObject, header, responseListener, retryPolicy, reqTAG, memoryPolicy, networkPolicy, cacheTime, aClass);
                requestHandled = true;
                break;
            }
        }
        if (!requestHandled) {
            throw new IllegalStateException("no client found that can handle " +
                    "this type of request, please set one at least request manager that can " +
                    "handle this type of request" +
                    " " + "through GlobalBuilder class");
        }

    }

    @Override
    public <T, F> void makeJsonArrayRequest(Context context,Client client, int method, String URL, JSONObject jsonObject, HashMap<String, String> header, RetryPolicy retryPolicy, String reqTAG, int memoryPolicy, int networkPolicy, long cacheTime, BaseResponseListener<T, F> responseListener, int logLevel, boolean mCancel, Class<F> aClass) {
        if (!checkRequestQueue(reqTAG, responseListener, mCancel)) {
            return;
        }
        Logger.getInstance().setLocalLevel(logLevel);
        if (memoryPolicy == 0) {
            memoryPolicy = this.memoryPolicy;
        }
        if (networkPolicy == 0) {
            networkPolicy = this.networkPolicy;
        }
        boolean offlineOnly = NetworkPolicy.isOfflineOnly(networkPolicy);
        if (MemoryPolicy.shouldReadFromMemoryCache(memoryPolicy) || offlineOnly) {
            Cache.CacheEntry entry = mMemoryCache.get(reqTAG);
            String data = entry.getData();
            JSONObject jsonObject1 = parseJson(data);
            if (jsonObject1 != null) {
                onResponse(context, new Response<>(jsonObject1, Response.LoadedFrom.MEMORY), reqTAG, cacheTime, aClass, memoryPolicy, networkPolicy, responseListener);
                return;
            }
        }
        if (NetworkPolicy.shouldReadFromDiskCache(networkPolicy) || offlineOnly) {
            String response = CacheRequestManager.getInstance(context).getCacheResponse(reqTAG);
            JSONObject jsonObject1 = parseJson(response);
            if (jsonObject1 != null) {
                onResponse(context, new Response<>(jsonObject1, Response.LoadedFrom.DISK), reqTAG, cacheTime, aClass, memoryPolicy, networkPolicy, responseListener);
                return;
            }
        }
        if (offlineOnly) {
            if (responseListener != null) {
                responseListener.onRequestErrorCode(ErrorCode.OFFLINE_ONLY_ERROR);
            }
            return;
        }
        if (!checkForInternetConnection(context)) {
            if (responseListener != null) {
                responseListener.onRequestErrorCode(ErrorCode.NO_CONNECTION_ERROR);
            }
            return;
        }
        boolean requestHandled = false;
        if(client!=null){
            if (client.canHandleRequest(URL, method)) {
                sendJsonRequest(context, client, method, URL, jsonObject, header, responseListener, retryPolicy, reqTAG, memoryPolicy, networkPolicy, cacheTime, aClass);
                return;
            }else{
                throw new IllegalArgumentException("the provided client cannot handle " +
                        "this type of request, please set  client that can " +
                        "handle this type of request" +
                        " " + "by Changing the client.canHandleRequest(URL, method) method");
            }
        }
        if (clients == null || clients.size() == 0) {
            throw new NoClientFoundException("no client set, please set one at least " +
                    "through GlobalBuilder class");
        }
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).canHandleRequest(URL, method)) {
                sendJsonArrayRequest(context, clients.get(i), method, URL, jsonObject, header, responseListener, retryPolicy, reqTAG, memoryPolicy, networkPolicy, cacheTime, aClass);
                requestHandled = true;
                break;
            }
        }
        if (!requestHandled) {
            throw new IllegalStateException("no client found that can handle " +
                    "this type of request, please set one at least request manager that can " +
                    "handle this type of request" +
                    " " + "through GlobalBuilder class");
        }

    }

    private void clearRequest(String reqTAG) {
        if (clients != null) {
            for (int i = 0; i < clients.size(); i++) {
                clients.get(i).cancelPendingRequests(reqTAG);
            }
        }
    }

    private <T> T parseDataToModel(Object jsonObject1,
                                   Class<T> mClass) {

        if (converters != null && converters.size() > 0) {
            boolean converted = false;
            for (int i = 0; i < converters.size(); i++) {
                if (converters.get(i).canConvert(jsonObject1)) {
                    try {
                        return converters.get(i).convert(jsonObject1,
                                mClass);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    converted = true;
                    break;
                }
            }
            if (!converted) {
                throw new IllegalArgumentException("no converter found that can handle " +
                        "this type of response, please set at least one converter that can " +
                        "handle this type of request" +
                        " " + "through GlobalBuilder class");
            }
        } else {
            throw new NoConverterFoundException("no converter set, please set one at least " +
                    "through GlobalBuilder class");

        }
        return null;
    }

    private <T, F> void sendJsonArrayRequest(final Context context, Client client, int method, String url, JSONObject jsonObject, HashMap<String, String> header, final BaseResponseListener<T, F> jsonRequestFinishedListener, RetryPolicy retryPolicy, final String reqTAG, final int memoryPolicy, final int networkPolicy, final long cacheTime, final Class<F> aClass) {
        if (header == null) {
            header = mHeaders;
        }
        if (retryPolicy == null) {
            retryPolicy = this.retryPolicy;
        }
        if (mBaseUrl != null) {
            if (!url.contains("://")) {
                url = mBaseUrl + url;
            }
        } else if (TextUtils.isEmpty(url)) {
            throw new NullPointerException(" url is empty or null");
        } else if (!url.contains("://")) {
            throw new NullPointerException("baseUrl is not set, either pass full url or set base " +
                    "url");
        }


        client.makeJsonArrayRequest(method, url, jsonObject, new
                Client.IRequest<Response<JSONArray>>() {
                    @Override
                    public void onRequestSuccess(final Response<JSONArray> response) {
                        onJsonArrayResponse(context, response, reqTAG, cacheTime, aClass, memoryPolicy, networkPolicy, jsonRequestFinishedListener);

                    }

                    @Override
                    public void onRequestErrorCode(int errorCode) {
                        jsonRequestFinishedListener.onRequestErrorCode(errorCode);
                    }

                }, header, retryPolicy, reqTAG);
    }

    private <T, F> void sendJsonRequest(final Context context, Client client, int method, String url, JSONObject jsonObject, HashMap<String, String> header, final BaseResponseListener<T, F> jsonRequestFinishedListener, RetryPolicy retryPolicy, final String reqTAG, final int memoryPolicy, final int networkPolicy, final long cacheTime, final Class<F> aClass) {
        if (header == null) {
            header = mHeaders;
        }
        if (retryPolicy == null) {
            retryPolicy = this.retryPolicy;
        }
        if (mBaseUrl != null) {
            if (!url.contains("://")) {
                url = mBaseUrl + url;
            }
        } else if (TextUtils.isEmpty(url)) {
            throw new NullPointerException(" url is empty or null");
        } else if (!url.contains("://")) {
            throw new NullPointerException("baseUrl is not set, either pass full url or set base " +
                    "url");
        }


        client.makeJsonRequest(method, url, jsonObject, new
                Client.IRequest<Response<JSONObject>>() {
                    @Override
                    public void onRequestSuccess(final Response<JSONObject> response) {
                        onResponse(context, response, reqTAG, cacheTime, aClass, memoryPolicy, networkPolicy, jsonRequestFinishedListener);

                    }

                    @Override
                    public void onRequestErrorCode(int errorCode) {
                        jsonRequestFinishedListener.onRequestErrorCode(errorCode);
                    }

                }, header, retryPolicy, reqTAG);
    }

    private <T, F> boolean checkRequestQueue(String reqTAG, BaseResponseListener<T, F> jsonRequestFinishedListener, boolean mCancel) {
        if (mRequestInQueue == null) {
            mRequestInQueue = new HashMap<>();
        }
        if (mCancel) {
            clearRequest(reqTAG);
            return true;
        }
        Integer count = mRequestInQueue.get(reqTAG);
        if (count == null || count == 0) {
            mRequestInQueue.put(reqTAG, 1);
            return true;
        } else {

            if (singleRequestMode) {
                jsonRequestFinishedListener.onRequestErrorCode(ErrorCode.REQUEST_ALREADY_QUEUED);
                return false;
            } else {
                mRequestInQueue.put(reqTAG, count + 1);
                return true;
            }

        }
    }

    private <T, F> void onJsonArrayResponse(final Context context, final Response<JSONArray> response, final
    String
            reqTAG, final long cacheTime, final Class<F> aClass, final int memoryPolicy, final int networkPolicy, final BaseResponseListener<T, F> jsonRequestFinishedListener) {
        if (response.response == null) {
            jsonRequestFinishedListener.onRequestErrorCode(ErrorCode.RESPONSE_NULL);
            return;
        }
        final long time = SystemClock.elapsedRealtime();
        ParserTask<F> parserTask = new ParserTask<>(reqTAG, new ParserTask.IParserListener<F>() {
            @Override
            public void onParseSuccess(String requestTag, Response<F> parseData) {
                if (jsonRequestFinishedListener != null) {
                    parseData.parseTime = SystemClock.elapsedRealtime() - time;
                    jsonRequestFinishedListener.onParseSuccess(parseData);
                    removeFromQueue(reqTAG);
                    mRequestInQueue.remove(reqTAG);
                }
            }

            @Override
            public void onParseError(String requestTag, int errorCode) {
                jsonRequestFinishedListener.onRequestErrorCode(ErrorCode.PARSE_ERROR);
            }

            @Override
            public Response<F> onParse(String requestTag) {
                if (response.loadedFrom != Response.LoadedFrom.DISK &&
                        response.loadedFrom != Response.LoadedFrom.MEMORY) {
                    if (MemoryPolicy.shouldWriteToMemoryCache(memoryPolicy)) {
                        getMemoryCache().put(reqTAG, new
                                Cache.CacheEntry(new Response<>(response.response.toString(), response.headers, response.statusCode, response.networkTimeMs, response.loadedFrom), cacheTime, reqTAG, SystemClock.elapsedRealtime()));
                    }
                    if (NetworkPolicy.shouldWriteToDiskCache(networkPolicy)) {
                        CacheRequestManager.getInstance(context).cacheResponse(new
                                Cache.CacheEntry(new Response<>(response.response.toString(), response.headers, response.statusCode, response.networkTimeMs, response.loadedFrom), cacheTime, reqTAG, SystemClock
                                .elapsedRealtime()));
                    }
                }
                if (jsonRequestFinishedListener != null) {
                    if (jsonRequestFinishedListener instanceof ResponseListener) {
                        return new Response<>((((ResponseListener<JSONArray, F>) jsonRequestFinishedListener).onRequestSuccess(response.response)), response.headers, response.statusCode, response.networkTimeMs, response.loadedFrom);
                    } else {
                        return new Response<>
                                (parseDataToModel(response.response, aClass), response.headers, response.statusCode, response.networkTimeMs, response.loadedFrom);
                    }
                }
                return new Response<>(response.loadedFrom);
            }
        });
        if (Utils.hasHoneycomb()) {
            parserTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            parserTask.execute();
        }
    }

    private <T, F> void onResponse(final Context context, final Response<JSONObject> response, final
    String
            reqTAG, final long cacheTime, final Class<F> aClass, final int memoryPolicy, final int networkPolicy, final BaseResponseListener<T, F> jsonRequestFinishedListener) {
        if (response.response == null) {
            jsonRequestFinishedListener.onRequestErrorCode(ErrorCode.RESPONSE_NULL);
            return;
        }
        final long time = SystemClock.elapsedRealtime();
/*        if (jsonRequestFinishedListener != null) {
            if (jsonRequestFinishedListener instanceof ResponseListener) {
                Response<F> response1 = new Response<>((((ResponseListener<JSONObject,F>) jsonRequestFinishedListener).onRequestSuccess(response.response)),response.headers,response.statusCode,response.networkTimeMs,response.loadedFrom);
                response1.parseTime =  SystemClock.elapsedRealtime()-time;
                jsonRequestFinishedListener.onParseSuccess(response1);
            } else {
                Response<F> response1 = new Response<>
                        (parseDataToModel(response.response.toString(), aClass), response.headers, response.statusCode, response.networkTimeMs, response.loadedFrom);
                response1.parseTime =  SystemClock.elapsedRealtime()-time;
                jsonRequestFinishedListener.onParseSuccess(response1);
            }
        }*/
        ParserTask<F> parserTask = new ParserTask<>(reqTAG, new ParserTask.IParserListener<F>() {
            @Override
            public void onParseSuccess(String requestTag, Response<F> parseData) {
                if (jsonRequestFinishedListener != null) {
                    parseData.parseTime = SystemClock.elapsedRealtime() - time;
                    jsonRequestFinishedListener.onParseSuccess(parseData);
                    removeFromQueue(reqTAG);
                    mRequestInQueue.remove(reqTAG);
                }
            }

            @Override
            public void onParseError(String requestTag, int errorCode) {
                jsonRequestFinishedListener.onRequestErrorCode(ErrorCode.PARSE_ERROR);
            }

            @Override
            public Response<F> onParse(String requestTag) {
                if (response.loadedFrom != Response.LoadedFrom.DISK &&
                        response.loadedFrom != Response.LoadedFrom.MEMORY) {
                    if (MemoryPolicy.shouldWriteToMemoryCache(memoryPolicy)) {
                        getMemoryCache().put(reqTAG, new
                                Cache.CacheEntry(new Response<>(response.response.toString(), response.headers, response.statusCode, response.networkTimeMs, response.loadedFrom), cacheTime, reqTAG, SystemClock.elapsedRealtime()));
                    }
                    if (NetworkPolicy.shouldWriteToDiskCache(networkPolicy)) {
                        CacheRequestManager.getInstance(context).cacheResponse(new
                                Cache.CacheEntry(new Response<>(response.response.toString(), response.headers, response.statusCode, response.networkTimeMs, response.loadedFrom), cacheTime, reqTAG, SystemClock
                                .elapsedRealtime()));
                    }
                }
                if (jsonRequestFinishedListener != null) {
                    if (jsonRequestFinishedListener instanceof ResponseListener) {
                        return new Response<>((((ResponseListener<JSONObject, F>) jsonRequestFinishedListener).onRequestSuccess(response.response)), response.headers, response.statusCode, response.networkTimeMs, response.loadedFrom);
                    } else {
                        return new Response<>
                                (parseDataToModel(response.response, aClass), response.headers, response.statusCode, response.networkTimeMs, response.loadedFrom);
                    }
                }
                return new Response<>(response.loadedFrom);
            }
        });
        if (Utils.hasHoneycomb()) {
            parserTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            parserTask.execute();
        }
    }

    private void removeFromQueue(String reqTAG) {
        if (mRequestInQueue == null) {
            mRequestInQueue = new HashMap<>();
        }
        Integer count = mRequestInQueue.get(reqTAG);
        if (count == null) {
            Logger.getInstance().e(reqTAG, "invalid state maybe mode changed");
        } else {
            if (singleRequestMode && count > 1) {
                Logger.getInstance().e(reqTAG, "invalid state maybe mode changed");
            } else {
                if (count == 1) {
                    mRequestInQueue.remove(reqTAG);
                } else {
                    mRequestInQueue.put(reqTAG, count - 1);
                }
            }

        }
    }

    private <T, F> void onResponseString(final Context context, final Response<String> response, final
    String reqTAG, final long cacheTime, final Class<F> aClass, final int memoryPolicy, final int networkPolicy, final BaseResponseListener<T, F> jsonRequestFinishedListener) {
        if (response.response == null) {
            jsonRequestFinishedListener.onRequestErrorCode(ErrorCode.RESPONSE_NULL);
            return;
        }
        final long time = SystemClock.elapsedRealtime();
        ParserTask<F> parserTask = new ParserTask<>(reqTAG, new ParserTask.IParserListener<F>() {
            @Override
            public void onParseSuccess(String requestTag, Response<F> parseData) {
                if (jsonRequestFinishedListener != null) {
                    parseData.parseTime = SystemClock.elapsedRealtime() - time;
                    jsonRequestFinishedListener.onParseSuccess(parseData);
                    removeFromQueue(reqTAG);
                }
            }

            @Override
            public void onParseError(String requestTag, int errorCode) {
                jsonRequestFinishedListener.onRequestErrorCode(ErrorCode.PARSE_ERROR);
            }

            @Override
            public Response<F> onParse(String requestTag) {
                if (response.loadedFrom != Response.LoadedFrom.DISK &&
                        response.loadedFrom != Response.LoadedFrom.MEMORY) {
                    if (MemoryPolicy.shouldWriteToMemoryCache(memoryPolicy)) {
                        getMemoryCache().put(reqTAG, new
                                Cache.CacheEntry(response, cacheTime, reqTAG, SystemClock.elapsedRealtime()));
                    }
                    if (NetworkPolicy.shouldWriteToDiskCache(networkPolicy)) {
                        CacheRequestManager.getInstance(context).cacheResponse(new
                                Cache.CacheEntry(response, cacheTime, reqTAG, SystemClock.elapsedRealtime()));
                    }
                }
                if (jsonRequestFinishedListener != null) {
                    if (jsonRequestFinishedListener instanceof ResponseListener) {
                        return new Response<>((((ResponseListener<String, F>) jsonRequestFinishedListener).onRequestSuccess(response.response)), response.headers, response.statusCode, response.networkTimeMs, response.loadedFrom);
                    } else {
                        return new Response<>
                                (parseDataToModel(response.response, aClass), response.headers, response.statusCode, response.networkTimeMs, response.loadedFrom);
                    }
                }
                return new Response<>(response.loadedFrom);
            }
        });
        if (Utils.hasHoneycomb()) {
            parserTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            parserTask.execute();
        }
    }

    private JSONObject parseJson(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                return new JSONObject(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    private boolean checkForInternetConnection(Context context) {
        return Utils.isConnected(context);

    }

    @Override
    public <T, F> void makeStringRequest(final Context context,Client client, int method, final String URL, JSONObject jsonObject, final HashMap<String, String> header, final RetryPolicy retryPolicy, final String reqTAG, int memoryPolicy, int networkPolicy, long cacheTime, BaseResponseListener<T, F> jsonRequestFinishedListener, int logLevel, boolean cancel, final Class<F> aClass) {
        if (!checkRequestQueue(reqTAG, jsonRequestFinishedListener, cancel)) {
            return;
        }
        Logger.getInstance().setLocalLevel(logLevel);
        if (memoryPolicy == 0) {
            memoryPolicy = this.memoryPolicy;
        }
        if (networkPolicy == 0) {
            networkPolicy = this.networkPolicy;
        }
        boolean offlineOnly = NetworkPolicy.isOfflineOnly(networkPolicy);
        if (MemoryPolicy.shouldReadFromMemoryCache(memoryPolicy) || offlineOnly) {
            Cache.CacheEntry response = mMemoryCache.get(reqTAG);
            if (response != null) {
                String data = response.getData();
                if (!TextUtils.isEmpty(data)) {
                    onResponseString(context, new Response<>(data, Response.LoadedFrom.MEMORY), reqTAG, cacheTime, aClass, memoryPolicy, networkPolicy, jsonRequestFinishedListener);
                    return;
                }
            }
        }
        if (NetworkPolicy.shouldReadFromDiskCache(networkPolicy) || offlineOnly) {
            String response = CacheRequestManager.getInstance(context).getCacheResponse(reqTAG);
            if (!TextUtils.isEmpty(response)) {
                onResponseString(context, new Response<>(response, Response.LoadedFrom.DISK), reqTAG, cacheTime, aClass, memoryPolicy, networkPolicy, jsonRequestFinishedListener);
                return;
            }
        }
        if (offlineOnly) {
            if (jsonRequestFinishedListener != null) {
                jsonRequestFinishedListener.onRequestErrorCode(ErrorCode.OFFLINE_ONLY_ERROR);
            }
            return;
        }
        if (!checkForInternetConnection(context)) {
            if (jsonRequestFinishedListener != null) {
                jsonRequestFinishedListener.onRequestErrorCode(ErrorCode.NO_CONNECTION_ERROR);
            }
            return;
        }
        boolean requestHandled = false;
        if(client!=null){
            if (client.canHandleRequest(URL, method)) {
                sendJsonRequest(context, client, method, URL, jsonObject, header, jsonRequestFinishedListener, retryPolicy, reqTAG, memoryPolicy, networkPolicy, cacheTime, aClass);
                return;
            }else{
                throw new IllegalArgumentException("the provided client cannot handle " +
                        "this type of request, please set  client that can " +
                        "handle this type of request" +
                        " " + "by Changing the client.canHandleRequest(URL, method) method");
            }
        }
        if (clients == null || clients.size() == 0) {
            throw new NoClientFoundException("no client set, please set one at least " +
                    "through GlobalBuilder class");
        }
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).canHandleRequest(URL, method)) {
                sendStringRequest(context, clients.get(i), method, URL, jsonObject, header, jsonRequestFinishedListener, retryPolicy, reqTAG, memoryPolicy, networkPolicy, cacheTime, aClass);
                requestHandled = true;
                break;
            }
        }
        if (!requestHandled) {
            throw new IllegalArgumentException("no client found that can handle " +
                    "this type of request, please set one at least client that can " +
                    "handle this type of request" +
                    " " + "through GlobalBuilder class");
        }

    }


    private <T, F> void sendStringRequest(final Context context, Client client, int method, String url, JSONObject jsonObject, HashMap<String, String> header, final BaseResponseListener<T, F> jsonRequestFinishedListener, RetryPolicy retryPolicy, final String reqTAG, final int memoryPolicy, final int networkPolicy, final long cacheTime, final Class<F> aClass) {
        if (header == null) {
            header = mHeaders;
        }
        if (retryPolicy == null) {
            retryPolicy = this.retryPolicy;
        }
        if (mBaseUrl != null) {
            if (!url.contains("://")) {
                url = mBaseUrl + url;
            }
        } else if (!url.contains("://")) {
            throw new NullPointerException("baseUrl is not set, either pass full url or set base " +
                    "url");
        }
        client.makeStringRequest(method, url, jsonObject, new Client.IRequest<Response<String>>
                () {
            @Override
            public void onRequestSuccess(final Response<String> response) {
                onResponseString(context, response, reqTAG, cacheTime, aClass, memoryPolicy, networkPolicy, jsonRequestFinishedListener);
            }

            @Override
            public void onRequestErrorCode(int errorCode) {
                jsonRequestFinishedListener.onRequestErrorCode(errorCode);
            }
        }, header, retryPolicy, reqTAG);

    }

    public void setConverters(ArrayList<Converter> converters) {
        this.converters = converters;
    }

    public void setClients(ArrayList<Client> client) {
        this.clients = client;
    }

    public void setNetworkPolicy(int networkPolicy) {
        this.networkPolicy = networkPolicy;
    }

    public void setMemoryPolicy(int memoryPolicy) {
        this.memoryPolicy = memoryPolicy;
    }

    public void setRetryPolicy(RetryPolicy retryPolicy) {
        this.retryPolicy = retryPolicy;
    }

    public void setHeaders(HashMap<String, String> mHeaders) {
        this.mHeaders = mHeaders;
    }

    public void setBaseUrl(String baseUrl) {
        this.mBaseUrl = baseUrl;
    }

    public void invalidateCacheResponse(String tag) {
        getMemoryCache().remove(tag);
    }

    public void clearCache() {
        getMemoryCache().clear();
    }

    public void setSingleRequestMode(boolean singleRequestMode) {
        clearQueue();
        this.singleRequestMode = singleRequestMode;
    }

    private void clearQueue() {
        if (mRequestInQueue != null) {
            mRequestInQueue.clear();
        }
    }
}
