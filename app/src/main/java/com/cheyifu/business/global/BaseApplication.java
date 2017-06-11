package com.cheyifu.business.global;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.zhy.autolayout.config.AutoLayoutConifg;

/**
 * Created by wsl  on 2017/6/618:01
 */
public class BaseApplication extends Application {
    private static Context mContext;
    public  static BaseApplication mApplication;
    public static RequestQueue queues;

    public BaseApplication(){


    }
    public static BaseApplication getmApplication(){
        if (mApplication == null) {
            mApplication=new BaseApplication();
        }
        return mApplication;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this;

        AutoLayoutConifg.getInstance().useDeviceSize().init(this);//屏幕适配
        queues= Volley.newRequestQueue(getApplicationContext());
    }
    public static  Context getmContext(){
        return mContext;
    }
    /**
     * 封装volley
     */
    public static RequestQueue getHttpQueue(){

        return queues;

    }
}
