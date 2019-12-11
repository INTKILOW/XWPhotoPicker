package com.intkilow.photopicker.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.core.graphics.ColorUtils;

public class DisplayUtil {

    private DisplayUtil() {

    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取屏幕物理像素密度
     *
     * @return
     */
    public static float getScreenDensity() {
        return Resources.getSystem().getDisplayMetrics().density;
    }

    /**
     * dp转px
     *
     * @param dp
     * @return
     */
    public static int dpToPx(int dp) {
        return Math.round(Resources.getSystem().getDisplayMetrics().density * dp + 0.5f);
    }

    /**
     * sp转px
     *
     * @param sp
     * @return
     */
    public static int spToPx(int sp) {
        return Math.round(Resources.getSystem().getDisplayMetrics().scaledDensity * sp + 0.5f);
    }

    /**
     * px转dp
     *
     * @param px
     * @return
     */
    public static int pxToDp(int px) {
        return Math.round(px / Resources.getSystem().getDisplayMetrics().density + 0.5f);
    }

    /**
     * px转sp
     *
     * @param px
     * @return
     */
    public static int pxToSp(int px) {
        return Math.round(px / Resources.getSystem().getDisplayMetrics().scaledDensity + 0.5f);
    }

    /**
     * 获取状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {
        Rect rect = new Rect();
        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }

    /**
     * 获取屏幕参数
     *
     * @return
     */
    public static String getScreenParams() {
        return getScreenWidth() + "*" + getScreenHeight();
    }

    public static void setStatusBar(Activity context, @ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // 设置状态栏底色颜色
            context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            context.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            context.getWindow().setStatusBarColor(color);

            // 如果亮色，设置状态栏文字为黑色
            if (isLightColor(color)) {
                context.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                context.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }

    }

    /**
     * 判断颜色是不是亮色
     *
     * @param color
     * @return
     * @from
     */
    private static boolean isLightColor(@ColorInt int color) {
        return ColorUtils.calculateLuminance(color) >= 0.5;
    }

    /**
     * 获取StatusBar颜色，默认白色
     *
     * @return
     */
    protected static @ColorInt
    int getStatusBarColor() {
        return Color.WHITE;
    }
}
