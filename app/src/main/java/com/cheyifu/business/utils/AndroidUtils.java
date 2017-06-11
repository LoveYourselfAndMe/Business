package com.cheyifu.business.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Administrator on 2016/10/8 0008.
 */
public class AndroidUtils {
    /**
     * 隐藏输入法（根据activity当前焦点所在控件的WindowToken）
     */
    public static void hideSoftInput(Activity activity, View editText) {
        View view;
        if (editText == null) {
            view = activity.getCurrentFocus();
        } else {
            view = editText;
        }

        if (view != null) {
            InputMethodManager inputMethod = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethod.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    /**
     * 隐藏键盘
     */
    public static boolean hideKeyboardboolean(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        return imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    /**
     * 显示键盘
     *
     * @param view
     */
    public static void showKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        imm.showSoftInput(view, 0);
    }
    /**
     * 获取版本号
     */
    public static int getVerCode(Context context) {
        int verCode = -1;
        try {
            verCode = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verCode;
    }

    /**
     * 获取版本名称
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return verName;
    }
    public static int dp2px(Context context, int dpValue) {
        // 获取密度比
        float density = context.getResources().getDisplayMetrics().density;
        int px = (int) (density * dpValue + 0.5f);
        return px;
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }
    /**
     * 去掉特殊符号
     *
     * @param s
     * @return
     */
    public static String format(String s) {
        String str = s
                .replaceAll(
                        "[`~!@#$%^&*()+=|{}':;',\\[\\]<>/?~！@#￥%……&;*（）——+|{}【】‘；：”“’。，、？|-]",
                        "");
        return str;
    }
}
