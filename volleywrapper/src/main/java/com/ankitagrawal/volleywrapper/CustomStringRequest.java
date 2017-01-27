package com.ankitagrawal.volleywrapper;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by ankitagrawal on 1/25/17.
 */
public abstract class CustomStringRequest<T> extends Request<T> {
    private final Map<String, String> headers;
    private final Response.Listener<T> listener;
    private final String mRequestBody;
    /** Default charset for JSON request. */
    protected static final String PROTOCOL_CHARSET = "utf-8";
    /** Content type for request. */
    private static final String PROTOCOL_CONTENT_TYPE =
            String.format("application/json; charset=%s", PROTOCOL_CHARSET);
    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param url URL of the request to make
     * @param headers Map of request headers
     */
    public CustomStringRequest(int method, String url, Map<String, String> headers,
                               JSONObject jsonObject,
                               Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mRequestBody = jsonObject!=null?jsonObject.toString():null;
        this.headers = headers;
        this.listener = listener;
    }
    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param url URL of the request to make
     * @param headers Map of request headers
     */
    public CustomStringRequest(int method, String url, Map<String, String> headers,
                               String jsonObject,
                               Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mRequestBody = jsonObject;
        this.headers = headers;
        this.listener = listener;
    }
    /**
     * Constructor which defaults to <code>GET</code> if <code>jsonRequest</code> is
     * <code>null</code>, <code>POST</code> otherwise.
     *
     */
    public CustomStringRequest(String url, Map<String, String> headers, JSONObject jsonObject, Response.Listener<T> listener,
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
    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    @Override
    public byte[] getBody() {
        try {
            return mRequestBody == null ? null : mRequestBody.getBytes(PROTOCOL_CHARSET);
        } catch (UnsupportedEncodingException uee) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                    mRequestBody, PROTOCOL_CHARSET);
            return null;
        }
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
