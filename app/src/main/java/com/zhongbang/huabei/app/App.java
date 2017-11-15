package com.zhongbang.huabei.app;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

import com.baidu.mapapi.SDKInitializer;
import com.zhongbang.huabei.http.MyVolley;
import com.zhongbang.huabei.service.LocationService;

/**
 * Created by Administrator on 2017-08-19.
 */

public class App extends Application{
    private static Context context;
    public LocationService locationService;
    public Vibrator mVibrator;
    @Override
    public void onCreate() {
        MyVolley.init(this);
        context=getApplicationContext();

        /***
         * 初始化定位sdk
         */
        locationService = new LocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
//        SDKInitializer.initialize(getApplicationContext());
        super.onCreate();
    }
    public static Context getContext(){
        return context;
    }
}
