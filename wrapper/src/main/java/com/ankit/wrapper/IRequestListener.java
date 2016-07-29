/**
 * 
 */
package com.ankit.wrapper;



/**
 * Created by ankitagrawal on 6/7/16. yay
 */
public interface IRequestListener<T,F> {


     void onParseSuccess(Response<F> response);

     void onRequestErrorCode(int errorCode);

}
