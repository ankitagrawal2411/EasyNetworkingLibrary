package com.ankitagrawal.volleywrapper;

import android.content.Context;

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
import com.android.volley.toolbox.Volley;
import com.ankitagrawal.wrapper.Client;
import com.ankitagrawal.wrapper.ErrorCode;
import com.ankitagrawal.wrapper.Logger;
import com.ankitagrawal.wrapper.RetryPolicy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

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
public class VolleyClient extends Client {
    private RequestQueue mRequestQueue;



    private static final String TAG = "Client";

    public VolleyClient(Context context) {
        this(context,null);
    }
    public VolleyClient(Context context, RetryPolicy retryPolicy) {
        super(retryPolicy);
        mRequestQueue = Volley.newRequestQueue(context);
    }
    public VolleyClient(RequestQueue requestQueue, RetryPolicy retryPolicy) {
        super(retryPolicy);
        mRequestQueue = requestQueue;
    }
    @Override
    public boolean canHandleRequest(String url, int method) {
        return true;
    }

    @Override
    public void makeJsonRequest(int method, String requestUrl, final JSONObject jsonObject,
                                final IRequest<com.ankitagrawal.wrapper.Response<JSONObject>> iRequestListener,
                                final HashMap<String, String> requestHeader, RetryPolicy retryPolicy, final String reqTAG) {
        com.android.volley.RetryPolicy volleyRetryPolicy;
        if (retryPolicy == null) {
            volleyRetryPolicy = new com.android.volley.DefaultRetryPolicy(CONNECT_TIMEOUT_MILLIS,
                    MAX_RETRIES, DEFAULT_BACKOFF_MULT);
        } else {
            volleyRetryPolicy = new com.android.volley.DefaultRetryPolicy(retryPolicy.getCurrentTimeout(),
                    retryPolicy.getRetryCount(),
                    retryPolicy.getBackoffMultiplier());
        }
        Logger.getInstance().d(reqTAG, "Tag:" + reqTAG);
        Logger.getInstance().d(reqTAG, reqTAG + " request Url: " + requestUrl);
        Logger.getInstance().d(reqTAG, reqTAG + " request Json Params: " + jsonObject);
        Logger.getInstance().d(reqTAG, reqTAG + " request Header: " + requestHeader);

        CustomJsonRequest jsonObjectRequest = new CustomJsonRequest<com.ankitagrawal.wrapper.Response<JSONObject>>(method,
                requestUrl, requestHeader, jsonObject, new Response.Listener<com.ankitagrawal.wrapper.Response<JSONObject>>() {

            @Override
            public void onResponse(com.ankitagrawal.wrapper.Response<JSONObject> jsonObject) {


                if (jsonObject!=null && jsonObject.response != null) {
                    Logger.getInstance().d(TAG, "onResponse jsonObject: "
                            + jsonObject.response.toString());
                    iRequestListener.onRequestSuccess(jsonObject);
                } else {
                    Logger.getInstance().e(TAG, "onResponse jsonObject: null");
                    iRequestListener.onRequestErrorCode(ErrorCode.RESPONSE_NULL);

                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String error = "";
                if (volleyError != null) {
                    error = volleyError.getMessage();
                }
                Logger.getInstance().e(TAG, reqTAG + " onErrorResponse >> errorCode: " + error);
                iRequestListener.onRequestErrorCode(getErrorCode(volleyError));
            }
        }) {
            @Override
            protected Response<com.ankitagrawal.wrapper.Response<JSONObject>> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                    JSONObject json = new JSONObject(jsonString);

                    return Response.success(new com.ankitagrawal.wrapper.Response<>(json, response.headers, response.statusCode, response.networkTimeMs, com.ankitagrawal.wrapper.Response.LoadedFrom
                            .NETWORK), HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Logger.getInstance().e(TAG, reqTAG + " onErrorResponse >> errorCode: " +
                            ErrorCode.PARSE_ERROR);
                    return Response.error(new ParseError(e));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Logger.getInstance().e(TAG, reqTAG + " onErrorResponse >> errorCode: " + ErrorCode.PARSE_ERROR);
                    return Response.error(new ParseError(e));
                }
            }
        };

        jsonObjectRequest.setRetryPolicy(volleyRetryPolicy);
        jsonObjectRequest.setShouldCache(false);

        addToRequestQueue(jsonObjectRequest, TAG);
    }


    @Override
    public void makeStringRequest(int method, String url, final JSONObject stringParams, final
    IRequest<com.ankitagrawal.wrapper.Response<String>> iRequestListener, final HashMap<String, String> requestHeader, RetryPolicy
                                          retryPolicy, final String reqTAG) {
        com.android.volley.RetryPolicy volleyRetryPolicy;
        if (retryPolicy == null) {
            volleyRetryPolicy = new com.android.volley.DefaultRetryPolicy(CONNECT_TIMEOUT_MILLIS,
                    MAX_RETRIES, DEFAULT_BACKOFF_MULT);
        } else {
            volleyRetryPolicy = new DefaultRetryPolicy(retryPolicy.getCurrentTimeout(),
                    retryPolicy.getRetryCount(),
                    retryPolicy.getBackoffMultiplier());
        }
        Logger.getInstance().d(TAG, "Tag:" + reqTAG);
        Logger.getInstance().d(TAG, reqTAG + " request Url: " + url);
        Logger.getInstance().d(TAG, reqTAG + " request String Params: " + stringParams);
        Logger.getInstance().d(TAG, reqTAG + " request Header: " + requestHeader);

        CustomStringRequest objStringRequest = new CustomStringRequest<com.ankitagrawal.wrapper.Response<String>>
                (method, url, requestHeader, stringParams,
                        new Response.Listener<com.ankitagrawal.wrapper.Response<String>>() {

                            @Override
                            public void onResponse(com.ankitagrawal.wrapper.Response<String> response) {

                                Logger.getInstance().d(TAG, "onResponse String response: " + response);

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
                        if (volleyError != null) {
                            error = volleyError.getMessage();
                        }

                        Logger.getInstance().e(TAG, reqTAG + " onErrorResponse >> errorCode: " +
                                error);
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
            protected Response<com.ankitagrawal.wrapper.Response<String>> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));

                    return Response.success(new com.ankitagrawal.wrapper.Response<>(jsonString, response.headers, response.statusCode, response.networkTimeMs, com.ankitagrawal.wrapper.Response.LoadedFrom
                                    .NETWORK),
                            HttpHeaderParser
                                    .parseCacheHeaders
                                            (response));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return Response.error(new ParseError(e));
                }
            }
        };

        objStringRequest.setRetryPolicy(volleyRetryPolicy);
        objStringRequest.setShouldCache(false);
        addToRequestQueue(objStringRequest, reqTAG);
    }

    @Override
    protected void makeJsonArrayRequest(int method, String requestUrl, JSONObject jsonObject, final IRequest<com.ankitagrawal.wrapper.Response<JSONArray>> iRequestListener, HashMap<String, String> requestHeader, RetryPolicy retryPolicy, final String reqTAG) {
        com.android.volley.RetryPolicy volleyRetryPolicy;
        if (retryPolicy == null) {
            volleyRetryPolicy = new com.android.volley.DefaultRetryPolicy(CONNECT_TIMEOUT_MILLIS,
                    MAX_RETRIES, DEFAULT_BACKOFF_MULT);
        } else {
            volleyRetryPolicy = new com.android.volley.DefaultRetryPolicy(retryPolicy.getCurrentTimeout(),
                    retryPolicy.getRetryCount(),
                    retryPolicy.getBackoffMultiplier());
        }
        Logger.getInstance().d(reqTAG, "Tag:" + reqTAG);
        Logger.getInstance().d(reqTAG, reqTAG + " request Url: " + requestUrl);
        Logger.getInstance().d(reqTAG, reqTAG + " request Json Params: " + jsonObject);
        Logger.getInstance().d(reqTAG, reqTAG + " request Header: " + requestHeader);

        CustomJsonArrayRequest jsonObjectRequest = new CustomJsonArrayRequest<com.ankitagrawal.wrapper.Response<JSONArray>>(method,
                requestUrl, requestHeader, jsonObject, new Response.Listener<com.ankitagrawal.wrapper.Response<JSONArray>>() {

            @Override
            public void onResponse(com.ankitagrawal.wrapper.Response<JSONArray> jsonObject) {


                if (jsonObject!=null && jsonObject.response != null) {
                    Logger.getInstance().d(TAG, "onResponse jsonObject: "
                            + jsonObject.response.toString());
                    iRequestListener.onRequestSuccess(jsonObject);
                } else {
                    Logger.getInstance().e(TAG, "onResponse jsonObject: null");
                    iRequestListener.onRequestErrorCode(ErrorCode.RESPONSE_NULL);

                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String error = "";
                if (volleyError != null) {
                    error = volleyError.getMessage();
                }
                Logger.getInstance().e(TAG, reqTAG + " onErrorResponse >> errorCode: " + error);
                iRequestListener.onRequestErrorCode(getErrorCode(volleyError));
            }
        }) {
            @Override
            protected Response<com.ankitagrawal.wrapper.Response<JSONArray>> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                    JSONArray json = new JSONArray(jsonString);

                    return Response.success(new com.ankitagrawal.wrapper.Response<>(json, response.headers, response.statusCode, response.networkTimeMs, com.ankitagrawal.wrapper.Response.LoadedFrom
                            .NETWORK), HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Logger.getInstance().e(TAG, reqTAG + " onErrorResponse >> errorCode: " +
                            ErrorCode.PARSE_ERROR);
                    return Response.error(new ParseError(e));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Logger.getInstance().e(TAG, reqTAG + " onErrorResponse >> errorCode: " + ErrorCode.PARSE_ERROR);
                    return Response.error(new ParseError(e));
                }
            }
        };

        jsonObjectRequest.setRetryPolicy(volleyRetryPolicy);
        jsonObjectRequest.setShouldCache(false);

        addToRequestQueue(jsonObjectRequest, TAG);
    }


    /**
     * @param volleyError - error encountered while executing request with
     *                    {@link Volley}
     * @return Returns {@link ErrorCode} according to type of
     * {@link VolleyError}
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

        Logger.getInstance().d(TAG, "addToRequestQueue");
        req.setTag(tag);
        mRequestQueue.add(req);
    }

    @Override
    public void cancelPendingRequests(String tag) {
        if (mRequestQueue != null) {
            Logger.getInstance().d(TAG, "Cancelling  request tag ::" + tag);
            mRequestQueue.cancelAll(tag);
        }else{
            Logger.getInstance().w(TAG, "cant clear all requests as RequestQueue is null");
        }
    }
    @Override
    public void cancelAllRequests() {
        if (mRequestQueue != null) {
            Logger.getInstance().d(TAG, "Cancelling all requests:");
            mRequestQueue.cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    return true;
                }
            });
        }else{
            Logger.getInstance().w(TAG, "cant clear all requests as RequestQueue is null");
        }
    }
}
