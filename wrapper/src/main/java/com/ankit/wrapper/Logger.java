package com.ankit.wrapper;

import android.util.Log;

/**
 * Created by ankitagrawal on 2/8/16.
 */
public class Logger {
    private int level=100;
    private Logger mInstance;

    private Logger(int i) {
        level = i;
    }

    private Logger() {

    }

    public Logger getInstance(){
        if(mInstance==null){
            mInstance = new Logger();
        }
        return mInstance;
    }
    public void setLevel(int level){
        this.level = level;
    }

    public static class LogLevel {

        public static final int VERBOSE = 0;
        public static final int INFO = 1;
        public static final int DEBUG = 2;
        public static final int WARNING = 3;
        public static final int ERROR = 4;
        public static boolean isVerbose(int logLevel) {
            return logLevel <= VERBOSE;
        }

        public static boolean isInfo(int logLevel) {
            return logLevel<=INFO;
        }

        public static boolean isDebug(int logLevel) {
            return logLevel<=DEBUG;
        }

        public static boolean isWarning(int logLevel) {
            return logLevel<=WARNING;
        }

        public static boolean isError(int logLevel) {
            return logLevel<=ERROR;
        }
    }

    public  void e(String tag, String message) {
        if (LogLevel.isError(level)) {
            Log.e(tag, message);
        }
    }

    public void v(String tag, String message) {
        if (LogLevel.isVerbose(level)) {
            Log.v(tag, message);
        }
    }
     @Deprecated
    public  void wtf(String tag, String message) {
            Log.wtf(tag, message);
    }

    public void d(String tag, String message) {
        if (LogLevel.isDebug(level)) {
            Log.d(tag, message);
        }
    }

    public void i(String tag, String message) {
        if (LogLevel.isInfo(level)) {
            Log.i(tag, message);
        }
    }

    public void w(String tag, String message) {
        if (LogLevel.isWarning(level)) {
            Log.w(tag, message);
        }
    }


    public static void printStackTrace(Exception e) {
            e.printStackTrace();
    }
}
