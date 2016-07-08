package com.ankit.volleywrapper;

/**
 * Created by darshan.zite on 7/24/2015.
 */
public interface IParserListener {

         void onParseSuccess(String requestTag, Object object);
         void onParseError(String requestTag, int errorCode);
         Object onParse(String requestTag);
}
