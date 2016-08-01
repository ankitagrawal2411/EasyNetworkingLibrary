package com.ankit.volleywrapper;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by ankitagrawal on 20/7/16.
 */
public abstract class GsonRequest<T> extends JsonRequest<T> {
    private final Map<String, String> headers;
    private final Response.Listener<T> listener;

    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param url URL of the request to make
     * @param headers Map of request headers
     */
    public GsonRequest(int method,String url,Map<String, String> headers,
                       JSONObject jsonObject,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url,(jsonObject == null) ? null : jsonObject.toString(),listener, errorListener);
        this.headers = headers;
        this.listener = listener;
    }
    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param url URL of the request to make
     * @param headers Map of request headers
     */
    public GsonRequest(int method,String url,Map<String, String> headers,
                       String jsonObject,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url,jsonObject,listener, errorListener);
        this.headers = headers;
        this.listener = listener;
    }
    /**
     * Constructor which defaults to <code>GET</code> if <code>jsonRequest</code> is
     * <code>null</code>, <code>POST</code> otherwise.
     *
     */
    public GsonRequest(String url, Map<String, String> headers,JSONObject jsonObject, Response.Listener<T> listener,
                             Response.ErrorListener errorListener) {
        this(jsonObject == null ? Method.GET : Method.POST, url,headers,jsonObject,
                listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }


    /**
     * Subclasses must implement this to parse the raw network response
     * and return an appropriate response type. This method will be
     * called from a worker thread.  The response will not be delivered
     * if you return null.
     *
     * @param response Response from the network
     * @return The parsed response, or null in the case of an error
     */
    @Override
    abstract protected Response<T> parseNetworkResponse(NetworkResponse response);

}
