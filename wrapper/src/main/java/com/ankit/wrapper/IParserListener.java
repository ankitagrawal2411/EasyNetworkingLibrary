package com.ankit.wrapper;

/**
 * Created by ankitagrawal on 6/7/16. yay
 */
public interface IParserListener<T> {

         void onParseSuccess(String requestTag, Response<T> object);
         void onParseError(String requestTag, int errorCode);
         Response<T> onParse(String requestTag);
}
