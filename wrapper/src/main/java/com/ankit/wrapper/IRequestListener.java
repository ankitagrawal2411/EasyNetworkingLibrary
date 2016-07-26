/**
 * 
 */
package com.ankit.wrapper;



/**
 * Created by ankitagrawal on 6/7/16. yay
 */
public interface IRequestListener<T> {

     Object onRequestSuccess(Response<T> response);

     void onParseSuccess(Object response);

     void onRequestErrorCode(int errorCode);

}
