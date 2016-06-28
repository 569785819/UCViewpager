package com.zhejunzhu.ucviewpager.utils;

import android.util.Log;

public class LLog {
    private static final String TAG = "zhuzhejun";
    public static boolean sIsDebug = true;
    private LLog() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void i(String msg) {
        if (sIsDebug)
            Log.i(TAG, msg);
    }

    public static void d(String msg) {
        if (sIsDebug)
            Log.d(TAG, msg);
    }

    public static void e(String msg) {
        if (sIsDebug)
            Log.e(TAG, msg);
    }

    public static void v(String msg) {
        if (sIsDebug)
            Log.v(TAG, msg);
    }

    public static void i(String tag, String msg) {
        if (sIsDebug)
            Log.i(TAG + "  " + tag, msg);
    }

    public static void d(String tag, String msg) {
        if (sIsDebug)
            Log.d(TAG + "  " + tag, msg);
    }

    public static void e(String tag, String msg) {
        if (sIsDebug)
            Log.e(TAG + "  " + tag, msg);
    }

    public static void v(String tag, String msg) {
        if (sIsDebug)
            Log.v(TAG + "  " + tag, msg);
    }
}
