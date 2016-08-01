/**
 *
 */
package com.ankit.wrapper;

import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ankitagrawal on 6/7/16. yay
 */
public class CacheRequestHandler implements ICacheRequest {

    private  MemoryCache mMemoryCache;
    private static CacheRequestHandler mInstance;
    private ArrayList<RequestHandler> requestHandlers;
    private ArrayList<Converter> converters;
    private RetryPolicy retryPolicy;
    private int memoryPolicy;
    private int networkPolicy;
    private HashMap<String, String> mHeaders;
     static CacheRequestHandler getInstance() {
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
    public <F>  void makeJsonRequest(Context context, int method, String URL, JSONObject
          jsonObject,
                                          HashMap<String, String> header, RetryPolicy retryPolicy, String reqTAG,  int memoryPolicy,  int networkPolicy, long cacheTime, IParsedResponseListener<JSONObject,F> responseListener, Class<F> mClass)
    {
        if(memoryPolicy==0){
            memoryPolicy = this.memoryPolicy;
        }
        if(networkPolicy==0){
            networkPolicy = this.networkPolicy;
        }
        boolean offlineOnly =NetworkPolicy.isOfflineOnly(networkPolicy);
        if(MemoryPolicy.shouldReadFromMemoryCache(memoryPolicy) || offlineOnly){
            ICache.CacheEntry entry =   mMemoryCache.get(reqTAG);
            String data = entry.getData();
            JSONObject jsonObject1 = parseJson(data);
            if (jsonObject1!=null) {
                if(responseListener !=null) {
                    if (responseListener instanceof IResponseListener) {
                        ((IResponseListener<JSONObject,F>) responseListener).onRequestSuccess
                                (jsonObject1);
                    } else {
                        responseListener.onParseSuccess(new Response<>(parseDataToModel(jsonObject1.toString(), mClass), Response.LoadedFrom.MEMORY));
                    }
                    return;
                }
            }
        }
        if(NetworkPolicy.shouldReadFromDiskCache(networkPolicy)|| offlineOnly) {
            String response = BaseCacheRequestManager.getInstance(context).getCacheResponse(reqTAG);
            JSONObject jsonObject1 = parseJson(response);
            if (jsonObject1 != null) {
                if (responseListener != null) {
                    if (responseListener instanceof IResponseListener) {
                        ((IResponseListener<JSONObject,F>) responseListener).onRequestSuccess
                                (jsonObject1);
                    return;
                } else {
                    responseListener.onParseSuccess(new Response<>
                            (parseDataToModel(jsonObject1.toString(), mClass), Response.LoadedFrom.DISK));
                }
                return;
            }
            }
        }
        if(offlineOnly) {
            if (responseListener != null) {
                responseListener.onRequestErrorCode(ErrorCode.OFFLINE_ONLY_ERROR);
            }
            return;
        }
        if(!checkForInternetConnection(context)){
            if (responseListener != null) {
                responseListener.onRequestErrorCode(ErrorCode.NO_CONNECTION_ERROR);
            }
            return;
        }
        if(requestHandlers ==null || requestHandlers.size()==0){
            throw new NullPointerException("no request handler set, please set one at least " +
                    "through GlobalBuilder class");
        }
        boolean requestHandled = false;
        for(int i=0;i< requestHandlers.size();i++){
            if(requestHandlers.get(i).canHandleRequest(URL,method)){
                sendJsonRequest(context, requestHandlers.get(i),method, URL, jsonObject,header, responseListener, retryPolicy, reqTAG,memoryPolicy,networkPolicy, cacheTime, mClass);
                requestHandled=true;
                break;
            }
        }
            if(!requestHandled){
                throw new IllegalArgumentException("no request handler found that can handle " +
                        "this type of request, please set one at least request manager that can " +
                        "handle this type of request" +
                        " " + "through GlobalBuilder class");
            }

    }
    private <T> T parseDataToModel(String jsonObject1,
                                    Class<T> mClass) {

        if (converters != null && converters.size() > 0) {
            boolean converted=false;
            for (int i = 0; i < converters.size(); i++) {
                Log.e("converter", "called");
                if (converters.get(i).canConvert(jsonObject1)) {
                    try {
                        return converters.get(i).convert(jsonObject1,
                                mClass);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    converted =true;
                    break;
                }
            }
            if(!converted){
                throw new IllegalArgumentException("no request handler found that can handle " +
                        "this type of request, please set one at least request manager that can " +
                        "handle this type of request" +
                        " " + "through GlobalBuilder class");
            }
        }else{
                throw new NullPointerException("no converter set, please set one at least " +
                        "through GlobalBuilder class");

        }
        return null;
    }


    private <F> void sendJsonRequest(final Context context, RequestHandler requestHandler, int method, String url, JSONObject jsonObject, HashMap<String, String> header, final IParsedResponseListener<JSONObject,F> jsonRequestFinishedListener,  RetryPolicy retryPolicy, final String reqTAG, final int memoryPolicy, final int networkPolicy, final long cacheTime, final Class<F> aClass) {
        if(header==null){
            header = mHeaders;
        }
        if(retryPolicy==null){
            retryPolicy = this.retryPolicy;
        }

        requestHandler.makeJsonRequest(method, url, jsonObject, new
                RequestHandler.IRequest<Response<JSONObject>>() {
            @Override
            public void onRequestSuccess(final Response<JSONObject> response) {
                if (response == null) {
                    jsonRequestFinishedListener.onRequestErrorCode(ErrorCode.RESPONSE_NULL);
                    return;
                }
                ParserTask<F> parserTask = new ParserTask<>(reqTAG, new ParserTask.IParserListener<F>() {
                    @Override
                    public void onParseSuccess(String requestTag, Response<F> parseData) {
                        if(jsonRequestFinishedListener!=null) {
                            jsonRequestFinishedListener.onParseSuccess(parseData);
                        }
                    }

                    @Override
                    public void onParseError(String requestTag, int errorCode) {
                        jsonRequestFinishedListener.onRequestErrorCode(ErrorCode.PARSE_ERROR);
                    }

                    @Override
                    public Response<F> onParse(String requestTag) {
                        if (MemoryPolicy.shouldWriteToMemoryCache(memoryPolicy)) {
                            getMemoryCache().put(reqTAG, new
                                    ICache.CacheEntry(response.toString(), cacheTime, reqTAG, SystemClock.elapsedRealtime()));
                        }
                        if (NetworkPolicy.shouldWriteToDiskCache(networkPolicy)) {
                            BaseCacheRequestManager.getInstance(context).cacheResponse(new
                                    ICache.CacheEntry(response.toString(), cacheTime, reqTAG, SystemClock.elapsedRealtime()));
                        }
                        if (jsonRequestFinishedListener != null) {
                            if (jsonRequestFinishedListener instanceof IResponseListener) {
                                return new Response<>((((IResponseListener<JSONObject,F>) jsonRequestFinishedListener).onRequestSuccess(response.response)), Response.LoadedFrom.NETWORK);
                            } else {
                               return new Response<>
                                        (parseDataToModel(response.response.toString(), aClass), Response
                                                .LoadedFrom.NETWORK);
                            }
                        }
                        return new Response<>(Response.LoadedFrom.NETWORK);
                    }
                });
                if (Utils.hasHoneycomb()) {
                    parserTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    parserTask.execute();
                }
            }

            @Override
            public void onRequestErrorCode(int errorCode) {
                jsonRequestFinishedListener.onRequestErrorCode(errorCode);
            }

        }, header, retryPolicy, reqTAG);
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
    public <F> void makeStringRequest(final Context context, int method, final String URL, String jsonObject, final HashMap<String, String> header, final RetryPolicy retryPolicy, final String reqTAG,  int memoryPolicy,  int networkPolicy,long cacheTime, IParsedResponseListener<String,F> jsonRequestFinishedListener,final Class<F> aClass)
    {
        if(memoryPolicy==0){
            memoryPolicy = this.memoryPolicy;
        }
        if(networkPolicy==0){
            networkPolicy = this.networkPolicy;
        }
        boolean offlineOnly =NetworkPolicy.isOfflineOnly(networkPolicy);
        if(MemoryPolicy.shouldReadFromMemoryCache(memoryPolicy) || offlineOnly){
            ICache.CacheEntry response =   mMemoryCache.get(reqTAG);
            String data = response.getData();
            if (!TextUtils.isEmpty(data)) {
                new Response<>(((IResponseListener<String,F>)jsonRequestFinishedListener)
                        .onRequestSuccess(data), Response.LoadedFrom.MEMORY);
                    return;
            }
        }
        if(NetworkPolicy.shouldReadFromDiskCache(networkPolicy) || offlineOnly){
            String response = BaseCacheRequestManager.getInstance(context).getCacheResponse(reqTAG);
            if (!TextUtils.isEmpty(response)) {
                if(jsonRequestFinishedListener instanceof IResponseListener) {
                    new Response<>(((IResponseListener<String,F>)jsonRequestFinishedListener)
                            .onRequestSuccess(response), Response.LoadedFrom.DISK);
                }
                else {
                    jsonRequestFinishedListener.onParseSuccess(new Response<>(parseDataToModel(response,aClass), Response.LoadedFrom.DISK));
                }
                    return;
            }
        }
        if(offlineOnly){
            if (jsonRequestFinishedListener != null) {
                jsonRequestFinishedListener.onRequestErrorCode(ErrorCode.OFFLINE_ONLY_ERROR);
            }
            return;
        }
        if(!checkForInternetConnection(context)){
            if (jsonRequestFinishedListener != null) {
                jsonRequestFinishedListener.onRequestErrorCode(ErrorCode.NO_CONNECTION_ERROR);
            }
            return;
        }
        if(requestHandlers ==null || requestHandlers.size()==0){
            throw new NullPointerException("no request manager set, please set one atleast " +
                    "through GlobalBuilder class");
        }
        boolean requestHandled = false;
        for(int i=0;i< requestHandlers.size();i++){
            if(requestHandlers.get(i).canHandleRequest(URL,method)){
                sendStringRequest(context, requestHandlers.get(i), method, URL, jsonObject, header, jsonRequestFinishedListener, retryPolicy, reqTAG, memoryPolicy, networkPolicy, cacheTime, aClass);
                requestHandled=true;
                break;
            }
        }
        if(!requestHandled){
            throw new IllegalArgumentException("no request manager found that can handle " +
                    "this type of request, please set one atleast request manager that can " +
                    "handle this type of request" +
                    " " +
                    "through GlobalBuilder class");
        }

    }
    private <T,F> void sendStringRequest(final Context context, RequestHandler requestHandler, int method, String url, String jsonObject, HashMap<String, String> header, final IParsedResponseListener<T,F> jsonRequestFinishedListener, RetryPolicy retryPolicy, final String reqTAG, final int memoryPolicy, final int networkPolicy, final long cacheTime, final Class<F> aClass) {
        if(header==null){
            header = mHeaders;
        }
        if(retryPolicy==null){
            retryPolicy = this.retryPolicy;
        }
        requestHandler.makeStringRequest(method, url, jsonObject, new RequestHandler.IRequest<Response<String>>
                () {
            @Override
            public void onRequestSuccess(final Response<String> response) {
                if (response == null) {
                    if(jsonRequestFinishedListener!=null) {
                        jsonRequestFinishedListener.onRequestErrorCode(ErrorCode.RESPONSE_NULL);
                    }
                    return;
                }
                ParserTask<F> parserTask = new ParserTask<>(reqTAG, new ParserTask.IParserListener<F>() {
                    @Override
                    public void onParseSuccess(String requestTag, Response<F> parseData) {
                        if(jsonRequestFinishedListener!=null) {
                            jsonRequestFinishedListener.onParseSuccess(parseData);
                        }
                    }

                    @Override
                    public void onParseError(String requestTag, int errorCode) {
                        if(jsonRequestFinishedListener!=null) {
                            jsonRequestFinishedListener.onRequestErrorCode(ErrorCode.PARSE_ERROR);
                        }
                    }

                    @Override
                    public Response<F> onParse(String requestTag) {
                        if (MemoryPolicy.shouldWriteToMemoryCache(memoryPolicy)) {
                            getMemoryCache().put(reqTAG, new
                                    ICache.CacheEntry(response.response, cacheTime, reqTAG, SystemClock.elapsedRealtime()));
                        }
                        if (NetworkPolicy.shouldWriteToDiskCache(networkPolicy)) {
                            BaseCacheRequestManager.getInstance(context).cacheResponse(new
                                    ICache.CacheEntry(response.response, cacheTime, reqTAG, SystemClock.elapsedRealtime()));
                        }
                        if (jsonRequestFinishedListener != null) {
                            if (jsonRequestFinishedListener instanceof IResponseListener) {
                                return new Response<>
                                        ((((IResponseListener<String,F>) jsonRequestFinishedListener).onRequestSuccess(response.response)), Response.LoadedFrom.NETWORK);
                            } else {
                                return new Response<>
                                        (parseDataToModel(response.response, aClass), Response
                                                .LoadedFrom.NETWORK);
                            }
                        }
                        return new Response<>(Response.LoadedFrom.NETWORK);
                    }
                });
                if (Utils.hasHoneycomb()) {
                    parserTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    parserTask.execute();
                }
            }
            @Override
            public void onRequestErrorCode(int errorCode) {
                jsonRequestFinishedListener.onRequestErrorCode(errorCode);
            }
        }, header, retryPolicy, reqTAG);

    }

    public void setConverters(ArrayList<Converter> converters) {
        this.converters=converters;
    }
    public void setRequestHandlers(ArrayList<RequestHandler> requestHandler) {
        this.requestHandlers = requestHandler;
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
}
