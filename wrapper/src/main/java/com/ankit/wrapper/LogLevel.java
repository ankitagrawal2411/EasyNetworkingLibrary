package com.ankit.wrapper;

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
    public static boolean isVerbose(int logLevel) {
        return logLevel >= VERBOSE;
    }

    public static boolean isInfo(int logLevel) {
        return logLevel>=INFO;
    }

    public static boolean isDebug(int logLevel) {
        return logLevel>=DEBUG;
    }

    public static boolean isWarning(int logLevel) {
        return logLevel>=WARNING;
    }

    public static boolean isError(int logLevel) {
        return logLevel>=ERROR;
    }
}