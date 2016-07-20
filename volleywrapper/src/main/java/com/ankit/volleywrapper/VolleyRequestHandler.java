package com.ankit.volleywrapper;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

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
public class VolleyRequestHandler extends RequestManager {
    private RequestQueue mRequestQueue;

    public static final int timeout = 20000;
    public static final int RETRY = 1;

    private static final String TAG = "RequestHandler";

    public VolleyRequestHandler(Context context) {
        super(context);
        mRequestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public  void init(Context context) {

    }
    @Override
    public boolean canHandleRequest(Context context, int method) {
        return true;
    }
    @Override
    public void makeJsonRequest(int method, String requestUrl, JSONObject jsonObject, final IRequestListener<JSONObject> iRequestListener, final HashMap<String, String> requestHeader, RetryPolicy retryPolicy, final String reqTAG) {
        com.android.volley.RetryPolicy volleyRetryPolicy;
        if(retryPolicy==null) {
            volleyRetryPolicy= new com.android.volley.DefaultRetryPolicy(timeout,
                    RETRY,
                    com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        }else{
            volleyRetryPolicy =  new com.android.volley.DefaultRetryPolicy(retryPolicy.getCurrentTimeout(),
                    retryPolicy.getRetryCount(),
                    retryPolicy.getBackoffMultiplier());
        }

        Log.d(TAG, reqTAG + " request Url: " + requestUrl);
        Log.d(TAG, reqTAG + " request Json Params: " + jsonObject);
        Log.d(TAG, reqTAG + " request Header: " + requestHeader);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method,
                requestUrl, jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.d(TAG, "onResponse jsonObject: "
                        + jsonObject);

                if (jsonObject != null) {
                    iRequestListener.onRequestSuccess(jsonObject);
                } else {
                    iRequestListener.onRequestErrorCode(ErrorCode.RESPONSE_NULL);

                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String error = "";
                if(volleyError!=null){
                    error = volleyError.getMessage();
                }
                Log.v(TAG, reqTAG + " onErrorResponse >> errorCode: " + error);
                iRequestListener.onRequestErrorCode(getErrorCode(volleyError));
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                if (requestHeader != null) {
                    return requestHeader;
                } else {
                    return super.getHeaders();
                }

            }
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {

                if(response!=null) {
                    JSONObject json =iRequestListener.onNetworkResponse(createNetworkResponse(response));
                    return Response.success(json, HttpHeaderParser.parseCacheHeaders(response));
                }
                else{
                    return Response.error(new ParseError());
                }

            }
        };

        jsonObjectRequest.setRetryPolicy(volleyRetryPolicy);
        jsonObjectRequest.setShouldCache(false);

        if (reqTAG!=null && reqTAG.trim().length()>0) {
            Log.d("RequestHandler", "Tag is:" + reqTAG);
            addToRequestQueue(jsonObjectRequest, reqTAG);

        } else {
            Log.d("RequestHandler", "Tag is:" + TAG);

            addToRequestQueue(jsonObjectRequest, TAG);
        }
    }

/*

    public <T> void makeGsonRequest(int method, String requestUrl,  final Class<T> clazz, JSONObject jsonObject, final IRequestListener<Class<T>> iRequestListener, final HashMap<String, String> requestHeader, RetryPolicy retryPolicy, final String reqTAG) {
        com.android.volley.RetryPolicy volleyRetryPolicy;
        if(retryPolicy==null) {
            volleyRetryPolicy= new com.android.volley.DefaultRetryPolicy(timeout,
                    RETRY,
                    com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        }else{
            volleyRetryPolicy =  new com.android.volley.DefaultRetryPolicy(retryPolicy.getCurrentTimeout(),
                    retryPolicy.getRetryCount(),
                    retryPolicy.getBackoffMultiplier());
        }
     *//*   Object foo =  Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class<?>[]{clazz},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        return null;
                    }
                });*//*
        Log.d(TAG, reqTAG + " request Url: " + requestUrl);
        Log.d(TAG, reqTAG + " request Json Params: " + jsonObject);
        Log.d(TAG, reqTAG + " request Header: " + requestHeader);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method,
                requestUrl,jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.d(TAG, "onResponse jsonObject: "
                        + jsonObject);

                if (jsonObject != null) {
                    iRequestListener.onRequestSuccess(null);
                } else {
                    iRequestListener.onRequestErrorCode(ErrorCode.RESPONSE_NULL);

                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String error = "";
                if(volleyError!=null){
                    error = volleyError.getMessage();
                }
                Log.v(TAG, reqTAG + " onErrorResponse >> errorCode: " + error);
                iRequestListener.onRequestErrorCode(getErrorCode(volleyError));
            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                if (requestHeader != null) {
                    return requestHeader;
                } else {
                    return super.getHeaders();
                }

            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String json = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(new Gson().fromJson(json, clazz),
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JsonSyntaxException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };

        jsonObjectRequest.setRetryPolicy(volleyRetryPolicy);
        jsonObjectRequest.setShouldCache(false);

        if (reqTAG!=null && reqTAG.trim().length()>0) {
            Log.d("RequestHandler", "Tag is:" + reqTAG);
            addToRequestQueue(jsonObjectRequest, reqTAG);

        } else {
            Log.d("RequestHandler", "Tag is:" + TAG);

            addToRequestQueue(jsonObjectRequest, TAG);
        }
    }*/
    @Override
    public void makeStringRequest(int method, String url, final String stringParams, final IRequestListener<String> iRequestListener, final HashMap<String, String> requestHeader, RetryPolicy retryPolicy, final String reqTAG) {
        com.android.volley.RetryPolicy volleyRetryPolicy;
        if(retryPolicy==null) {
            volleyRetryPolicy= new com.android.volley.DefaultRetryPolicy(timeout,
                    RETRY,
                    com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        }else{
            volleyRetryPolicy =  new DefaultRetryPolicy(retryPolicy.getCurrentTimeout(),
                    retryPolicy.getRetryCount(),
                    retryPolicy.getBackoffMultiplier());
        }

        Log.d(TAG, reqTAG + " request Url: " + url);
        Log.d(TAG, reqTAG + " request String Params: " + stringParams);
        Log.d(TAG, reqTAG + " request Header: " + requestHeader);

        StringRequest objStringRequest = new StringRequest(method, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Log.d(TAG, "onResponse String response: " + response);

                        if (response != null) {

                            iRequestListener.onRequestSuccess(response);
                        } else {
                            iRequestListener.onRequestErrorCode(ErrorCode.RESPONSE_NULL);
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                iRequestListener.onRequestErrorCode(getErrorCode(volleyError));
                String error = "";
                if(volleyError!=null){
                    error = volleyError.getMessage();
                }

                Log.v(TAG, reqTAG + " onErrorResponse >> errorCode: " + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                if (stringParams != null) {
                    HashMap<String, String> mParams = new HashMap<>();
                    mParams.put("key", stringParams);
                    return mParams;
                } else {
                    return super.getParams();
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                if (requestHeader != null) {
                    return requestHeader;
                } else {
                    return super.getHeaders();
                }
            }
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {

                if(response!=null) {
                    String json =iRequestListener.onNetworkResponse(createNetworkResponse(response));
                    return Response.success(
                            json, HttpHeaderParser.parseCacheHeaders(response));
                }
                else{
                    return Response.error(new ParseError());
                }

            }
        };

        objStringRequest.setRetryPolicy(volleyRetryPolicy);
        objStringRequest.setShouldCache(false);


        if (reqTAG!=null && reqTAG.trim().length()>0) {
            Log.d(TAG, "Tag is:" + reqTAG);
            addToRequestQueue(objStringRequest, reqTAG);

        } else {
            Log.d(TAG, "Tag is:" + TAG);

            addToRequestQueue(objStringRequest, TAG);
        }
    }
    private com.ankit.volleywrapper.NetworkResponse createNetworkResponse(NetworkResponse response) {
        return new com.ankit.volleywrapper.NetworkResponse(response.statusCode,response.data,
                response.headers,response.notModified,response.networkTimeMs);
    }

    /**
     * @param volleyError
     *            - error encountered while executing request with
     *            {@link Volley}
     * @return Returns {@link ErrorCode} according to type of
     *         {@link VolleyError}
     */
    public static int getErrorCode(VolleyError volleyError) {

        int errorCode = ErrorCode.UNKNOWN_ERROR;

        if (volleyError != null) {
            if (volleyError instanceof AuthFailureError) {
                errorCode = ErrorCode.AUTH_FAILURE_ERROR;
            } else if (volleyError instanceof ServerError) {
                errorCode = ErrorCode.NETWORK_ERROR;
            } else if (volleyError instanceof NetworkError) {
                errorCode = ErrorCode.NETWORK_ERROR;
            } else if (volleyError instanceof ParseError) {
                errorCode = ErrorCode.PARSE_ERROR;
            } else if (volleyError instanceof TimeoutError) {
                errorCode = ErrorCode.TIMEOUT_ERROR;
            } else {
                errorCode = ErrorCode.UNKNOWN_ERROR;
            }
        }

        return errorCode;
    }
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        // req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        Log.d(TAG, "addToRequestQueue");
        req.setTag(tag);
        mRequestQueue.add(req);
    }


    public void cancelPendingRequests(String tag) {
        if (mRequestQueue != null) {
            Log.d(TAG, "Cancelling  request tag ::" + tag);
            Log.d(TAG, "Rquest Queue : " + mRequestQueue);
            mRequestQueue.cancelAll(tag);
            Log.d(TAG, "Rquest Queue : " + mRequestQueue);
        }
    }
}
