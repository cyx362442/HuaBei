/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.zhongbang.huabei.utils;

import android.os.Environment;

import java.io.File;

public class FileUtil {
    public static File getSaveFile(String fileName) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/zhongbang/";
        File file = new File(path, fileName);
        return file;
    }
}
