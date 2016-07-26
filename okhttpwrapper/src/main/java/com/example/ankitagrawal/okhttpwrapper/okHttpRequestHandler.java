package com.example.ankitagrawal.okhttpwrapper;

import android.content.Context;
import android.util.Log;

import com.ankit.wrapper.ErrorCode;
import com.ankit.wrapper.IRequest;
import com.ankit.wrapper.RequestManager;
import com.ankit.wrapper.RetryPolicy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
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
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class okHttpRequestHandler extends RequestManager {
    private static final long CONNECT_TIMEOUT_MILLIS = 10000;
    private static final long READ_TIMEOUT_MILLIS = 10000;
    private  OkHttpClient client ;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType STRING = MediaType.parse("text/plain; charset=utf-8");
    public okHttpRequestHandler(Context context) {
        super();
        client = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT_MILLIS,TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Interceptor.Chain chain) throws IOException {
                        Request request = chain.request();

                        // try the request
                        Response response = chain.proceed(request);

                        int tryCount = 0;
                        while (!response.isSuccessful() && tryCount < 3) {

                            Log.d("intercept", "Request is not successful - " + tryCount);

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
    public void makeJsonRequest(int method, String requestUrl, JSONObject jsonObject, final IRequest<com.ankit.wrapper.Response<JSONObject>> onJsonRequestFinishedListener, HashMap<String, String> requestHeader, final RetryPolicy retryPolicy, String reqTAG) {

        Request.Builder builder = new Request.Builder()
                .url(requestUrl)
                .tag(reqTAG);
        if(jsonObject==null){
            jsonObject= new JSONObject();
        }
        if(requestHeader!=null){
            for (Map.Entry<String, String> entry : requestHeader.entrySet()) {
                String key =  entry.getKey();
                if (key == null) {
                    throw new NullPointerException("key == null");
                }
                builder.addHeader(key, entry.getValue());
            }
        }
        switch (method){
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


        Request request =builder.build();
        if(retryPolicy!=null) {
            if (client.interceptors().size() > 0) {
                client.interceptors().clear();
            }
            createInterceptor(client, retryPolicy);
        }
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                onJsonRequestFinishedListener.onRequestErrorCode(ErrorCode.NETWORK_ERROR);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    onJsonRequestFinishedListener.onRequestErrorCode(response.code());
                    throw new IOException("Unexpected code " + response);

                }
                Headers responseHeaders = response.headers();
                HashMap<String,String> headers = new HashMap<>(responseHeaders.size());
                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                    headers.put(responseHeaders.name(i),responseHeaders.value(i));
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }
                String jsonData = response.body().string();
                try {
                    JSONObject jObject = new JSONObject(jsonData);
                    onJsonRequestFinishedListener.onRequestSuccess(new com.ankit.wrapper.Response<>(jObject,headers,response.code(), response.receivedResponseAtMillis()-response
                            .sentRequestAtMillis(), com.ankit.wrapper.Response.LoadedFrom
                            .NETWORK));
                } catch (JSONException e) {
                    e.printStackTrace();
                    onJsonRequestFinishedListener.onRequestErrorCode(ErrorCode.PARSE_ERROR);
                }
                System.out.println(response.body().string());
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

                    Log.d("intercept", "Request is not successful - " + tryCount);
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
    public void makeStringRequest(int method, String requestUrl, String jsonObject, final IRequest<com.ankit.wrapper.Response<String>> onRequestFinishedListener, final HashMap<String, String> requestHeader, RetryPolicy retryPolicy, String reqTAG) {
        Request.Builder builder = new Request.Builder()
                .url(requestUrl)
                .tag(reqTAG);
        if(jsonObject==null){
            jsonObject= "";
        }
        if(requestHeader!=null){
            for (Map.Entry<String, String> entry : requestHeader.entrySet()) {
                String key =  entry.getKey();
                if (key == null) {
                    throw new NullPointerException("key == null");
                }
                builder.addHeader(key, entry.getValue());
            }
        }
        switch (method){
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


        Request request =builder.build();

        if(client.interceptors().size()>0) {
            client.interceptors().clear();
        }
        createInterceptor(client, retryPolicy);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                onRequestFinishedListener.onRequestErrorCode(ErrorCode.NETWORK_ERROR);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    onRequestFinishedListener.onRequestErrorCode(ErrorCode.NO_CONNECTION_ERROR);
                    throw new IOException("Unexpected code " + response);

                }
                Headers responseHeaders = response.headers();
                HashMap<String,String> headers = new HashMap<>(responseHeaders.size());
                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                    headers.put(responseHeaders.name(i),responseHeaders.value(i));
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }
                onRequestFinishedListener.onRequestSuccess(new com.ankit.wrapper.Response<String>(response.body().string(),headers,response.code(), response.receivedResponseAtMillis()-response
                        .sentRequestAtMillis(), com.ankit.wrapper.Response.LoadedFrom
                        .NETWORK));
                System.out.println(response.body().string());
            }
        });
    }
}