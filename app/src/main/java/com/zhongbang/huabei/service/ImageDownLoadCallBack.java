package com.zhongbang.huabei.service;

import android.graphics.Bitmap;

import java.io.File;

/**
 * Created by Administrator on 2017-11-15.
 */

public interface ImageDownLoadCallBack {
    void onDownLoadSuccess(File file);
    void onDownLoadSuccess(Bitmap bitmap);
    void onDownLoadFailed();
}
