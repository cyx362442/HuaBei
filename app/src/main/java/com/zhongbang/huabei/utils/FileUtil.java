/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.zhongbang.huabei.utils;

import android.os.Environment;

import java.io.File;

public class FileUtil {
    public static  String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/zhongbang/";
    public static File getSaveFile(String fileName) {
        File file1 = new File(path);
        if(!file1.exists()){
            file1.mkdir();
        }
        File file = new File(path, fileName);
        return file;
    }
}
