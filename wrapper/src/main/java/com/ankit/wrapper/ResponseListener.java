package com.ankit.wrapper;

/**
 * Created by ankitagrawal on 21/7/16.
 */
public interface ResponseListener<T,F> extends IRequestListener<T,F>{

        F onRequestSuccess(T response);



}
