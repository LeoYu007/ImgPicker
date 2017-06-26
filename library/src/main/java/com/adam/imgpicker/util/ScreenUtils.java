package com.adam.imgpicker.util;

import android.app.Activity;
import android.content.res.Resources;
import android.util.DisplayMetrics;


public final class ScreenUtils {

    public static int width(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int height(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static int dp2px(float dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density + 0.5f);
    }

    public static int px2dp(float px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density + 0.5f);
    }

}
