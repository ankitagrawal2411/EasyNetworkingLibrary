/**
 * 
 */
package com.ankitagrawal.wrapper;



/**
 * Created by ankitagrawal on 6/7/16. yay
 */
public interface BaseResponseListener<T,F> {
     void onParseSuccess(Response<F> response);

     void onRequestErrorCode(int errorCode);
}
