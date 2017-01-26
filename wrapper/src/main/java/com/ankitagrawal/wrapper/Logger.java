package com.ankitagrawal.wrapper;

import android.util.Log;

/**
 * Created by ankitagrawal on 2/8/16.
 */
 public class Logger {
    private int level=LogLevel.NO_LOGS;
    private static Logger mInstance;
    private boolean disabledLogs=false;
    private int localLevel=LogLevel.NO_LEVEL;

    private Logger() {

    }

    public static Logger getInstance(){
        if(mInstance==null){
            mInstance = new Logger();
        }
        return mInstance;
    }
    public void setLevel(int level){
        if(level!=LogLevel.NO_LEVEL) {
            this.level = level;
        }
    }
    public void setLocalLevel(int level){
        if(level!=LogLevel.NO_LEVEL) {
            this.localLevel = level;
        }
    }


    public  void e(String tag, String message) {
        if (LogLevel.isError(level,localLevel)&& !disabledLogs) {
            Log.e(tag, message);
        }
    }

    public void v(String tag, String message) {
        if (LogLevel.isVerbose(level,localLevel)&& !disabledLogs) {
            Log.v(tag, message);
        }
    }
     @Deprecated
    public  void wtf(String tag, String message) {
         if (!disabledLogs) {
             Log.wtf(tag, message);
         }
    }

    public void d(String tag, String message) {
        if (LogLevel.isDebug(level,localLevel) && !disabledLogs) {
            Log.d(tag, message);
        }
    }

    public void i(String tag, String message) {
        if (LogLevel.isInfo(level,localLevel) && !disabledLogs) {
            Log.i(tag, message);
        }
    }

    public void w(String tag, String message) {
        if (LogLevel.isWarning(level,localLevel) && !disabledLogs) {
            Log.w(tag, message);
        }
    }


    public  void printStackTrace(Exception e) {
        if (!disabledLogs) {
            e.printStackTrace();
        }
    }

    public void setDisabledLogs(boolean disabledLogs) {
        this.disabledLogs = disabledLogs;
    }
}
