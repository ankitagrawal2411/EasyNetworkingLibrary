package com.ankit.volleywrapper;

/**
 * Created by ankitagrawal on 6/7/16. yay
 */
public interface IParserListener {

         void onParseSuccess(String requestTag, Object object);
         void onParseError(String requestTag, int errorCode);
         Object onParse(String requestTag);
}
