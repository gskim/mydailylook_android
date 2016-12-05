package net.args.mydailylook.utils;

import android.content.Context;
import android.util.Log;

import net.args.mydailylook.common.Const;

/**
 * Created by Administrator on 2016-07-10.
 */
public class DevLog {

    public static void v(Context context, String tag, String msg) {
        if (Const.DEBUG)
            Log.v(tag, "[" + context.getClass().getSimpleName() + "] " + msg);
    }

    public static void d(Context context, String tag, String msg) {
        if (Const.DEBUG)
            Log.d(tag, "[" + context.getClass().getSimpleName() + "] " + msg);
    }

    public static void i(Context context, String tag, String msg) {
        if (Const.DEBUG)
            Log.i(tag, "[" + context.getClass().getSimpleName() + "] " + msg);
    }

    public static void w(Context context, String tag, String msg) {
        if (Const.DEBUG)
            Log.w(tag, "[" + context.getClass().getSimpleName() + "] " + msg);
    }

    public static void e(Context context, String tag, String msg) {
        if (Const.DEBUG)
            Log.e(tag, "[" + context.getClass().getSimpleName() + "] " + msg);
    }

    public static void v(Context context, String msg) {
        if (Const.DEBUG)
            Log.v(Const.TAG, "[" + context.getClass().getSimpleName() + "] " + msg);
    }

    public static void d(Context context, String msg) {
        if (Const.DEBUG)
            Log.d(Const.TAG, "[" + context.getClass().getSimpleName() + "] " + msg);
    }

    public static void i(Context context, String msg) {
        if (Const.DEBUG)
            Log.i(Const.TAG, "[" + context.getClass().getSimpleName() + "] " + msg);
    }

    public static void w(Context context, String msg) {
        if (Const.DEBUG)
            Log.w(Const.TAG, "[" + context.getClass().getSimpleName() + "] " + msg);
    }

    public static void e(Context context, String msg) {
        if (Const.DEBUG)
            Log.e(Const.TAG, "[" + context.getClass().getSimpleName() + "] " + msg);
    }

    public static void v(String tag, String msg) {
        if (Const.DEBUG)
            Log.v(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (Const.DEBUG)
            Log.d(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (Const.DEBUG)
            Log.i(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (Const.DEBUG)
            Log.w(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (Const.DEBUG)
            Log.e(tag, msg);
    }

}
