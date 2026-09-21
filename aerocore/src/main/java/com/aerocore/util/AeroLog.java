package com.aerocore.util;

import android.util.Log;

public final class AeroLog {
    private static final String TAG = "AeroCore";
    private static volatile boolean sVerbose = false;

    private AeroLog() {}

    public static void setVerbose(boolean verbose) {
        sVerbose = verbose;
    }

    public static void d(String msg) {
        if (sVerbose) {
            Log.d(TAG, msg);
        }
    }

    public static void i(String msg) {
        Log.i(TAG, msg);
    }

    public static void w(String msg) {
        Log.w(TAG, msg);
    }

    public static void e(String msg) {
        Log.e(TAG, msg);
    }

    public static void e(String msg, Throwable tr) {
        Log.e(TAG, msg, tr);
    }
}
