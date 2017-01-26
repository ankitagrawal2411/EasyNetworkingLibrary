package com.ankitagrawal.wrapper;

/**
 * Created by ankitagrawal on 21/7/16.
 */
public interface ResponseListener<T,F> extends BaseResponseListener<T,F> {

        F onRequestSuccess(T response);

}
