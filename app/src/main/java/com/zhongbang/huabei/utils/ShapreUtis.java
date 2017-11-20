package com.zhongbang.huabei.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.zhongbang.huabei.R;
import com.zhongbang.huabei.app.App;

/**
 * Created by Administrator on 2017-08-18.
 */

public class ShapreUtis {
    private static SharedPreferences.Editor editor;
    private static SharedPreferences sp;

    private ShapreUtis(){}
    private static ShapreUtis utils=null;
    public static ShapreUtis getInstance(Context context){
        if(utils==null){
            utils=new ShapreUtis();
        }
        sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        editor = sp.edit();
        return utils;
    }
    public void isLand(boolean land){
        editor.putBoolean("island",land);
        editor.commit();
    }
    public boolean getLand(){
        return sp.getBoolean("island", false);
    }

    public void setAccount(String account){
        editor.putString(App.getContext().getString(R.string.account),account);
        editor.commit();
    }
    public String getAccount(){
        return sp.getString(App.getContext().getString(R.string.account),"");
    }

    public void setName(String name){
        editor.putString(App.getContext().getString(R.string.name),name);
        editor.commit();
    }
    public String getName(){
        return sp.getString(App.getContext().getString(R.string.name),"");
    }
}
