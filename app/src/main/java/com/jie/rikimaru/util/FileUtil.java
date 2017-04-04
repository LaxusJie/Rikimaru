package com.jie.rikimaru.util;

import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 捷 on 2017/4/4.
 */
public class FileUtil {
    /**
     * 创建文件
     * @return
     */
    @NonNull
    public static String createVideoFile() {
        // 获取 SD 卡根目录
        String saveDir = Environment.getExternalStorageDirectory() + "/rikimaru_photos";
        // 新建目录
        File dir = new File(saveDir);
        if (! dir.exists()) dir.mkdir();
        // 生成文件名
        SimpleDateFormat t = new SimpleDateFormat("yyyyMMddssSSS");
        return saveDir + "/MT" + (t.format(new Date())) + ".mp4";
    }
}
