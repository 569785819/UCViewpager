package com.zhejunzhu.ucviewpager.utils;

import android.content.Context;
import android.os.Build;
import android.view.WindowManager;

public class AndroidUtils {

    private static final String TAG = "AndroidUtils";
    private static final int VERSION_CODES_LOLLIPOP = 21;


    public static int getScreenOrientation(Context contex) {
        return contex.getResources().getConfiguration().orientation;
    }

    public static boolean isLollipop() {
        return Build.VERSION.SDK_INT >= VERSION_CODES_LOLLIPOP;
    }


    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int getWindowsWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    public static int getWindowsHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }
}
