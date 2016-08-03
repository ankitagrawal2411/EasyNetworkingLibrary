package com.example.ankitagrawal.okhttpwrapper;

import android.content.Context;

import com.ankit.wrapper.DefaultRetryPolicy;
import com.ankit.wrapper.ErrorCode;
import com.ankit.wrapper.Logger;
import com.ankit.wrapper.RequestHandler;
import com.ankit.wrapper.RetryPolicy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Copyright (C) 2016 ankitagrawal on 19/7/16
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class okHttpRequestHandler extends RequestHandler {

    private OkHttpClient client;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType STRING = MediaType.parse("text/plain; charset=utf-8");
    public okHttpRequestHandler(final RetryPolicy retryPolicy) {
        super(retryPolicy);
        createClient();
    }
    public okHttpRequestHandler() {
        super();
        createClient();
    }

    private void createClient() {
        client = new OkHttpClient.Builder()
                .connectTimeout(mRetryPolicy.getCurrentTimeout(), TimeUnit.MILLISECONDS)
                .readTimeout(mRetryPolicy.getCurrentTimeout(), TimeUnit.MILLISECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Interceptor.Chain chain) throws IOException {
                        Request request = chain.request();
                        // try the request
                        Response response = chain.proceed(request);
                        int tryCount = 0;
                        while (!response.isSuccessful() && tryCount < mRetryPolicy.getRetryCount()) {
                            Logger.getInstance().w("intercept", "Request is not successful - " +
                                    tryCount);
                            tryCount++;

                            // retry the request
                            response = chain.proceed(request);
                        }

                        // otherwise just pass the original response on
                        return response;
                    }
                }).build();
    }



    @Override
    public boolean canHandleRequest(String url, int method) {
        return method != com.ankit.wrapper.Request.Method.OPTIONS && method != com.ankit.wrapper.Request.Method.TRACE;
    }

    @Override
    public void makeJsonRequest(int method, String requestUrl, JSONObject jsonObject, final IRequest<com.ankit.wrapper.Response<JSONObject>> onJsonRequestFinishedListener, HashMap<String, String> requestHeader, final RetryPolicy retryPolicy, final String reqTAG) {
        Logger.getInstance().d(TAG, "Tag:" + reqTAG);
        Logger.getInstance().d(TAG, reqTAG + " request Url: " + requestUrl);
        Logger.getInstance().d(TAG, reqTAG + " request Json Params: " + jsonObject);
        Logger.getInstance().d(TAG, reqTAG + " request Header: " + requestHeader);
        Request.Builder builder = new Request.Builder()
                .url(requestUrl)
                .tag(reqTAG);
        if (jsonObject == null) {
            jsonObject = new JSONObject();
        }
        if (requestHeader != null) {
            for (Map.Entry<String, String> entry : requestHeader.entrySet()) {
                String key = entry.getKey();
                if (key == null) {
                    throw new NullPointerException("key == null");
                }
                builder.addHeader(key, entry.getValue());
            }
        }
        switch (method) {
            case com.ankit.wrapper.Request.Method.GET:
                builder.get();
                break;
            case com.ankit.wrapper.Request.Method.POST:
                builder.post(RequestBody.create(JSON, jsonObject.toString()));

                break;
            case com.ankit.wrapper.Request.Method.DELETE:
                builder.delete(RequestBody.create(JSON, jsonObject.toString()));

                break;
            case com.ankit.wrapper.Request.Method.HEAD:
                builder.head();
                break;
            case com.ankit.wrapper.Request.Method.PATCH:
                builder.patch(RequestBody.create(JSON, jsonObject.toString()));

                break;
            case com.ankit.wrapper.Request.Method.PUT:
                builder.put(RequestBody.create(JSON, jsonObject.toString()));
                break;
            case com.ankit.wrapper.Request.Method.OPTIONS: {
                throw new IllegalArgumentException("okhttp does not support Options request type," +
                        " Use " +
                        "volley request Manager for this type of request");
            }
            case com.ankit.wrapper.Request.Method.TRACE:
                throw new IllegalArgumentException("okhttp does not support trace request type, " +
                        "Use " +
                        "volley request Manager for this type of request");
        }


        Request request = builder.build();
        if (retryPolicy != null) {
            if (client.interceptors().size() > 0) {
                client.interceptors().clear();
            }
            createInterceptor(client, retryPolicy);
        }else{
            createInterceptor(client, mRetryPolicy);
        }
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Logger.getInstance().e(TAG, reqTAG + " onErrorResponse >> errorCode: " + ErrorCode.UNKNOWN_ERROR);
                onJsonRequestFinishedListener.onRequestErrorCode(ErrorCode.UNKNOWN_ERROR);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Logger.getInstance().e(TAG, reqTAG + " onErrorResponse >> errorCode: " + response.code());
                    onJsonRequestFinishedListener.onRequestErrorCode(response.code());
                    return;
                }
                if (response.body() == null) {
                    Logger.getInstance().e(TAG, "onResponse jsonObject: null");
                    onJsonRequestFinishedListener.onRequestErrorCode(ErrorCode.RESPONSE_NULL);
                    return;
                }
                Logger.getInstance().d(TAG, "onResponse jsonObject: " + response.body().toString());
                Headers responseHeaders = response.headers();
                HashMap<String, String> headers;
                if (responseHeaders != null) {
                    headers = new HashMap<>(responseHeaders.size());
                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                        headers.put(responseHeaders.name(i), responseHeaders.value(i));
                    }
                } else {
                    headers = new HashMap<>(0);
                }
                String jsonData = response.body().string();
                try {
                    JSONObject jObject = new JSONObject(jsonData);
                    onJsonRequestFinishedListener.onRequestSuccess(new com.ankit.wrapper.Response<>(jObject, headers, response.code(), response.receivedResponseAtMillis() - response
                            .sentRequestAtMillis(), com.ankit.wrapper.Response.LoadedFrom
                            .NETWORK));
                } catch (JSONException e) {
                    Logger.getInstance().e(TAG, reqTAG + " onErrorResponse >> errorCode: " + ErrorCode.PARSE_ERROR);
                    e.printStackTrace();
                    onJsonRequestFinishedListener.onRequestErrorCode(ErrorCode.PARSE_ERROR);
                }

            }
        });

    }


    private void createInterceptor(OkHttpClient client, final RetryPolicy retryPolicy) {
        client.interceptors().add(getInterceptor(retryPolicy));
    }

    private Interceptor getInterceptor(final RetryPolicy retryPolicy) {
        return new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request request = chain.request();

                // try the request
                Response response = chain.proceed(request);

                int tryCount = 0;
                while (!response.isSuccessful() && tryCount < retryPolicy.getRetryCount()) {

                    Logger.getInstance().w("intercept", "Request is not successful - " + tryCount);
                    retryPolicy.setCurrentTimeout((int) (retryPolicy.getCurrentTimeout() * retryPolicy
                            .getBackoffMultiplier()));
                    tryCount++;

                    // retry the request
                    response = chain.proceed(request);
                }

                // otherwise just pass the original response on
                return response;
            }
        };
    }

    @Override
    public void makeStringRequest(int method, String requestUrl, String jsonObject, final IRequest<com.ankit.wrapper.Response<String>> onRequestFinishedListener, final HashMap<String, String> requestHeader, RetryPolicy retryPolicy, final String reqTAG) {
        Logger.getInstance().d(TAG, "Tag:" + reqTAG);
        Logger.getInstance().d(TAG, reqTAG + " request Url: " + requestUrl);
        Logger.getInstance().d(TAG, reqTAG + " request Json Params: " + jsonObject);
        Logger.getInstance().d(TAG, reqTAG + " request Header: " + requestHeader);
        Request.Builder builder = new Request.Builder()
                .url(requestUrl)
                .tag(reqTAG);
        if (jsonObject == null) {
            jsonObject = "";
        }
        if (requestHeader != null) {
            for (Map.Entry<String, String> entry : requestHeader.entrySet()) {
                String key = entry.getKey();
                if (key == null) {
                    throw new NullPointerException("key == null");
                }
                builder.addHeader(key, entry.getValue());
            }
        }
        switch (method) {
            case com.ankit.wrapper.Request.Method.GET:
                builder.get();
                break;
            case com.ankit.wrapper.Request.Method.POST:
                builder.post(RequestBody.create(STRING, jsonObject));
                break;
            case com.ankit.wrapper.Request.Method.DELETE:
                builder.delete(RequestBody.create(STRING, jsonObject));
                break;
            case com.ankit.wrapper.Request.Method.HEAD:
                builder.head();
                break;
            case com.ankit.wrapper.Request.Method.PATCH:
                builder.patch(RequestBody.create(STRING, jsonObject));
                break;
            case com.ankit.wrapper.Request.Method.PUT:
                builder.put(RequestBody.create(STRING, jsonObject));
                break;
            case com.ankit.wrapper.Request.Method.OPTIONS: {
                throw new IllegalArgumentException("okhttp does not support Options request type," +
                        " Use " +
                        "volley request Manager for this type of request");
            }
            case com.ankit.wrapper.Request.Method.TRACE:
                throw new IllegalArgumentException("okhttp does not support trace request type, " +
                        "Use " +
                        "volley request Manager for this type of request");
        }


        Request request = builder.build();

        if (client.interceptors().size() > 0) {
            client.interceptors().clear();
        }
        createInterceptor(client, retryPolicy);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Logger.getInstance().e(TAG, reqTAG + " onErrorResponse >> errorCode: " + ErrorCode.UNKNOWN_ERROR);
                onRequestFinishedListener.onRequestErrorCode(ErrorCode.UNKNOWN_ERROR);


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Logger.getInstance().e(TAG, reqTAG + " onErrorResponse >> errorCode: " +
                            response.code());
                    onRequestFinishedListener.onRequestErrorCode(response.code());
                }
                if (response.body() == null) {
                    Logger.getInstance().e(TAG, "onResponse jsonObject: null");
                    onRequestFinishedListener.onRequestErrorCode(ErrorCode.RESPONSE_NULL);
                }
                Logger.getInstance().d(TAG, "onResponse jsonObject: " + response.body().toString());
                Headers responseHeaders = response.headers();
                HashMap<String, String> headers;
                if (responseHeaders != null) {
                    headers = new HashMap<>(responseHeaders.size());
                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                        headers.put(responseHeaders.name(i), responseHeaders.value(i));
                    }
                } else {
                    headers = new HashMap<>(0);
                }
                onRequestFinishedListener.onRequestSuccess(new com.ankit.wrapper.Response<>(response.body().string(), headers, response.code(), response.receivedResponseAtMillis() - response
                        .sentRequestAtMillis(), com.ankit.wrapper.Response.LoadedFrom
                        .NETWORK));
                System.out.println(response.body().string());
            }
        });
    }

    @Override
    protected void cancelPendingRequests(String tag) {
        List<Call> calls = client.dispatcher().queuedCalls();
        for (Call call : calls) {
            if (call.request().tag().equals(tag) && !call.isCanceled()) {
                call.cancel();
            }
        }
    }

    @Override
    protected void cancelAllRequests() {
        client.dispatcher().cancelAll();

    }
}