package com.ankit.volleywrapper;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

/**
 * Created by ankitagrawal on 6/7/16. yay
 */
public class ErrorCode {



    /**
     * Error code when volley error is not predicted.
     */
    public static final int UNKNOWN_ERROR = 110;

    /**
     * Error code when volley returns network error.
     */
    public static final int NETWORK_ERROR = 111;

    /**
     * Error code when volley returns no connection error.
     */
    public static final int NO_CONNECTION_ERROR = 112;



    /**
     * Error code when volley returns server error.
     */
    public static final int SERVER_ERROR = 114;

    /**
     * Error code when volley returns auth failure error.
     */
    public static final int AUTH_FAILURE_ERROR = 115;

    /**
     * Error code when volley returns parse error.
     */
    public static final int PARSE_ERROR = 116;
    /**
     * Error code when response is null.
     */
    public static final int RESPONSE_NULL = 117;

    /**
     * Error code when volley returns request timeout error.
     */
    public static final int TIMEOUT_ERROR = 118;


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
}
