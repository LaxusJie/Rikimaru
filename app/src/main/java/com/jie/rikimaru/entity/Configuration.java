package com.jie.rikimaru.entity;

import android.support.annotation.NonNull;

/**
 * Desction 压缩配置实体
 * Author：haojie
 * date：2017-03-27
 */
public class Configuration {
    @NonNull
    public String inputPath;//输入路径
    @NonNull
    public String outputPath;//输出路径
    @NonNull
    public String resolution;//分辨率
    public boolean isLandscape;//横竖屏判断
}
