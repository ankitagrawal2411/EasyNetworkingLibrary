package com.ankitagrawal.wrapper;

/**
 * Created by ankitagrawal on 3/8/16.
 */
public  class LogLevel {
    static final int NO_LEVEL = -2;
    public static final int NO_LOGS = -1;
    public static final int INFO = 0;
    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int WARNING = 3;
    public static final int ERROR = 4;
    public static boolean isVerbose(int logLevel,int localLevel) {
        if(localLevel==NO_LEVEL){
            return logLevel >= VERBOSE;
        }
        return localLevel>=VERBOSE;

    }

    public static boolean isInfo(int logLevel,int localLevel) {
        if(localLevel==NO_LEVEL){
            return logLevel >= INFO;
        }
        return localLevel>=INFO;
    }

    public static boolean isDebug(int logLevel,int localLevel) {
        if(localLevel==NO_LEVEL){
            return logLevel >= DEBUG;
        }
        return localLevel>=DEBUG;
    }

    public static boolean isWarning(int logLevel,int localLevel) {
        if(localLevel==NO_LEVEL){
            return logLevel >= WARNING;
        }
        return localLevel>=WARNING;
    }

    public static boolean isError(int logLevel,int localLevel) {
        if(localLevel==NO_LEVEL){
            return logLevel >= ERROR;
        }
        return localLevel>=ERROR;
    }
}