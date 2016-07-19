package com.example.ankitagrawal.okhttpwrapper;

import android.content.Context;
import android.util.Log;

import com.ankit.volleywrapper.ErrorCode;
import com.ankit.volleywrapper.IRequestListener;
import com.ankit.volleywrapper.NetworkResponse;
import com.ankit.volleywrapper.RequestManager;
import com.ankit.volleywrapper.RetryPolicy;

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
 * Created by ankitagrawal on 19/7/16.
 */
public class okHttpRequestHandler extends RequestManager {
    private static final long CONNECT_TIMEOUT_MILLIS = 10000;
    private static final long READ_TIMEOUT_MILLIS = 10000;
    private  OkHttpClient client ;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType STRING = MediaType.parse("text/plain; charset=utf-8");
    public okHttpRequestHandler(Context context) {
        super(context);
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
    public boolean canHandleRequest(Context context, int method) {
        return method != com.ankit.volleywrapper.Request.Method.OPTIONS && method != com.ankit.volleywrapper.Request.Method.TRACE;
    }

    @Override
    public void init(Context context) {

    }

    @Override
    public void makeJsonRequest(int method, String requestUrl, JSONObject jsonObject, final IRequestListener<JSONObject> onJsonRequestFinishedListener, HashMap<String, String> requestHeader, final RetryPolicy retryPolicy, String reqTAG) {

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
            case com.ankit.volleywrapper.Request.Method.GET:
                builder.get();
                break;
            case com.ankit.volleywrapper.Request.Method.POST:
                builder.post(RequestBody.create(JSON, jsonObject.toString()));

                break;
            case com.ankit.volleywrapper.Request.Method.DELETE:
                builder.delete(RequestBody.create(JSON, jsonObject.toString()));

                break;
            case com.ankit.volleywrapper.Request.Method.HEAD:
                builder.head();
                break;
            case com.ankit.volleywrapper.Request.Method.PATCH:
                builder.patch(RequestBody.create(JSON, jsonObject.toString()));

                break;
            case com.ankit.volleywrapper.Request.Method.PUT:
                builder.put(RequestBody.create(JSON, jsonObject.toString()));
                break;
            case com.ankit.volleywrapper.Request.Method.OPTIONS: {
                throw new IllegalArgumentException("okhttp does not support Options request type," +
                        " Use " +
                        "volley request Manager for this type of request");
            }
            case com.ankit.volleywrapper.Request.Method.TRACE:
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
                onJsonRequestFinishedListener.onRequestErrorCode(ErrorCode.NETWORK_ERROR);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    onJsonRequestFinishedListener.onRequestErrorCode(ErrorCode.NO_CONNECTION_ERROR);
                    throw new IOException("Unexpected code " + response);

                }
                Headers responseHeaders = response.headers();
                HashMap<String,String> headers = new HashMap<>(responseHeaders.size());
                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                    headers.put(responseHeaders.name(i),responseHeaders.value(i));
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }
                onJsonRequestFinishedListener.onNetworkResponse(new NetworkResponse(response.code(),response.body().bytes(),headers,response.code()==304, response.receivedResponseAtMillis()-response
                        .sentRequestAtMillis()));
                String jsonData = response.body().string();
                try {
                    JSONObject Jobject = new JSONObject(jsonData);
                    onJsonRequestFinishedListener.onRequestSuccess(Jobject);
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
    public void makeStringRequest(int method, String requestUrl, String jsonObject, final IRequestListener<String> onRequestFinishedListener, final HashMap<String, String> requestHeader, RetryPolicy retryPolicy, String reqTAG) {
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
            case com.ankit.volleywrapper.Request.Method.GET:
                builder.get();
                break;
            case com.ankit.volleywrapper.Request.Method.POST:
                builder.post(RequestBody.create(STRING, jsonObject));
                break;
            case com.ankit.volleywrapper.Request.Method.DELETE:
                builder.delete(RequestBody.create(STRING, jsonObject));
                break;
            case com.ankit.volleywrapper.Request.Method.HEAD:
                builder.head();
                break;
            case com.ankit.volleywrapper.Request.Method.PATCH:
                builder.patch(RequestBody.create(STRING, jsonObject));
                break;
            case com.ankit.volleywrapper.Request.Method.PUT:
                builder.put(RequestBody.create(STRING, jsonObject));
                break;
            case com.ankit.volleywrapper.Request.Method.OPTIONS: {
                throw new IllegalArgumentException("okhttp does not support Options request type," +
                        " Use " +
                        "volley request Manager for this type of request");
            }
            case com.ankit.volleywrapper.Request.Method.TRACE:
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
                onRequestFinishedListener.onNetworkResponse(new NetworkResponse(response.code(),response.body().bytes(),headers,response.code()==304, response.receivedResponseAtMillis()-response
                        .sentRequestAtMillis()));
                String jsonData = response.body().string();
                onRequestFinishedListener.onRequestSuccess(jsonData);
                System.out.println(response.body().string());
            }
        });
    }
}