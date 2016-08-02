package com.ankit.wrapper;


import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ankitagrawal on 14/7/16.
 */
public abstract class RequestHandler {


    public RequestHandler() {

    }

    /**
     * Created by ankitagrawal on 6/7/16. yay
     */
    protected interface IRequest<T> {

        void onRequestSuccess(T response);

        void onRequestErrorCode(int errorCode);


    }

    public abstract boolean canHandleRequest(String url, int method);

    protected abstract void makeJsonRequest(int method, String requestUrl, JSONObject jsonObject,
                                            IRequest<Response<JSONObject>> onJsonRequestFinishedListener,
                                            HashMap<String, String> requestHeader, RetryPolicy retryPolicy, String reqTAG);


    protected abstract void makeStringRequest(int method, String url, String stringParams,
                                              IRequest<Response<String>> onStringRequestFinishedListener,
                                              HashMap<String, String> requestHeader, RetryPolicy retryPolicy, String reqTAG);
/*    protected abstract <T> void makeRequest(int method, String url, String stringParams,
                                              IRequest<Response<T>> onStringRequestFinishedListener,
                                              HashMap<String, String> requestHeader, RetryPolicy retryPolicy, String reqTAG);*/
}
