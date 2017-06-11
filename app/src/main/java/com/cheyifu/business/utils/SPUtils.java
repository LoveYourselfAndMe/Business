package com.cheyifu.business.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.List;

/**
 * Created by Administrator on 2016/9/14 0014.
 */
public class SPUtils {

    public static final String CHEYIFU_FILE_NAME = "cheyifu"; // 缓存文件名
    public static final String USER_GUIDE_FILE_NAME = "guide";   //引导界面文件名
    public static final String KEY_USER_INFO = "userinfo";		//存储用户信息
    public static final String KEY_USER_CODE = "usercode";		//存储验证码

    ///////////////////////////////通用的/////////////////////////////////////////////
    /**
     * 取布尔值
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static boolean getBoolean(Context context, String key,boolean defValue) {
        SharedPreferences prefe = context.getSharedPreferences(CHEYIFU_FILE_NAME, 0);
        return prefe.getBoolean(key, defValue);

    }

    /**
     * 存布尔值
     * @param context
     * @param key
     * @param value
     */
    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences prefe = context.getSharedPreferences(CHEYIFU_FILE_NAME, 0);
        prefe.edit().putBoolean(key, value).commit();
    }
    /**
     * 存String方法
     * @param context
     * @param key
     * @param value
     */
    public static void putString(Context context, String key, String value){
        SharedPreferences prefe = context.getSharedPreferences(CHEYIFU_FILE_NAME, 0);
        prefe.edit().putString(key, value).commit();
    }
    /**
     * 取String方法
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static String getString(Context context, String key, String defValue){
        SharedPreferences prefe = context.getSharedPreferences(CHEYIFU_FILE_NAME, 0);
        return prefe.getString(key, defValue);
    }

    /**
     * 存Int方法
     * @param context
     * @param key
     * @param value
     */
    public static void putInt(Context context, String key, int value){
        SharedPreferences prefe = context.getSharedPreferences(CHEYIFU_FILE_NAME, 0);
        prefe.edit().putInt(key, value).commit();
    }


    /**
     * 取Int方法
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static int getInt(Context context, String key, int defValue){
        SharedPreferences prefe = context.getSharedPreferences(CHEYIFU_FILE_NAME, 0);
        int getInt = prefe.getInt(key, defValue);
        return getInt;
    }

    /**
     * 清除某个key对应的缓存
     * @param key
     * @param context
     */
    public static void clearByKey(String key, Context context) {
        SharedPreferences prefe = context.getSharedPreferences(CHEYIFU_FILE_NAME, 0);
        SharedPreferences.Editor editor = prefe.edit();
        editor.putString(key, "");
        editor.commit();
    }

    /**清除所有的缓存数据
     * @param context
     */
    public static void clearAll(Context context) {
        SharedPreferences prefe = context.getSharedPreferences(CHEYIFU_FILE_NAME, 0);
        SharedPreferences.Editor editor = prefe.edit();
        editor.clear();
        editor.commit();
    }
    ///////////////////////////////主要本项目用到的//////////////////////////////////////////////////
    /**
     * 保存用户信息
     * @param context
     * @param user
     */
	/*public static void saveUserInfo(Context context, UserInfoModel user){
		saveObj(context, user, KEY_USER_INFO);
	}*/

    /**
     * 取出用户信息
     * @param context
     * @return
     */
	/*public static UserInfoModel getUserInfo(Context context){
		return (UserInfoModel) readObj(context, KEY_USER_INFO);
	}*/

    /**
     * 判断是否进入引导界面
     * @param context
     * @return
     */
    public static boolean getGuided(Context context) {
        SharedPreferences prefe = context.getSharedPreferences(USER_GUIDE_FILE_NAME, 0);
        boolean b = prefe.getBoolean("isguide", false);
        return b;
    }

    /**
     * 设置进入过引导界面
     *
     * @param context
     */
    public static void setGuided(Context context,Boolean isguided) {
        SharedPreferences prefe = context.getSharedPreferences(USER_GUIDE_FILE_NAME, 0);
        SharedPreferences.Editor editor = prefe.edit();
        editor.putBoolean("isguide", isguided);
        editor.commit();
    }



}

