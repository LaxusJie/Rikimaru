package com.jie.rikimaru.util;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.jie.rikimaru.entity.Configuration;

/**
 * Desction：压缩工具类
 * Author：haojie
 * date：2017-03-27
 */
public class CompressUtil {
    public static final String TAG = "CompressUtil";
    public static void compressInit(Activity activity) {
        FFmpeg ffmpeg = FFmpeg.getInstance(activity);
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {

                @Override
                public void onStart() {}

                @Override
                public void onFailure() {
                    Log.d(TAG, "压缩库初始化失败");
                }

                @Override
                public void onSuccess() {
                    Log.d(TAG, "压缩库初始化完成");
                }

                @Override
                public void onFinish() {}
            });
        } catch (FFmpegNotSupportedException e) {
            // Handle if FFmpeg is not supported by device
        }
    }

    /**
     * 执行压缩程序 （360x270标清 480×360高清）
     * @param activity
     * @param configuration
     * @param listener
     */
    public static void compressExe(Activity activity, Configuration configuration, final OnCompressListener listener) {
        final double duration = Double.parseDouble(VideoUtils.getTime(configuration.inputPath));
        String ratio;
        if(configuration.isLandscape) {
            ratio = "16:9 ";
        } else {
            ratio = "9:16 ";
        }
        String[] command = getCmd(configuration, ratio);
        FFmpeg ffmpeg = FFmpeg.getInstance(activity);
        try {
            // to execute "ffmpeg -version" command you just need to pass "-version"
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {

                @Override
                public void onStart() {}

                @Override
                public void onProgress(String message) {
                    Log.d(TAG, "onProgress：" + message);
                    if(message.contains("frame") && message.contains("fps")) {
                        String str[] = message.split(" ");
                        for (int i = 0; i < str.length; i++) {
                            if(str[i].contains("time")) {
                                String time = str[i].substring(5,13);
                                double currentTime = VideoUtils.formatSs(time);
                                long progress = Math.round(currentTime/duration * 100);
                                listener.onProgress((int) progress);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(String message) {
                    Log.d(TAG, "onFailure：" + message);
                    listener.onFail();
                }

                @Override
                public void onSuccess(String message) {
                    Log.d(TAG, "onSuccess：" + message);
                    listener.onSuccess();
                }

                @Override
                public void onFinish() {}
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // Handle if FFmpeg is already running
        }
    }

    /**
     * 获取指令集
     * @param configuration
     * @param ratio
     * @return
     */
    @NonNull
    private static String[] getCmd(Configuration configuration, String ratio) {
        String cmd = "-y -i " + configuration.inputPath +
                " -strict -2 " +
                "-vcodec libx264 " +
                "-preset ultrafast " +
                "-crf 24 " +
                "-acodec aac " +
                "-ar 44100 " +
                "-ac 2 " +
                "-b:v 150k " +
                "-s " + configuration.resolution +
                " -aspect " + ratio + configuration.outputPath;
        return cmd.split(" ");
    }

    public static void cancel(Activity activity) {
        FFmpeg.getInstance(activity).killRunningProcesses();
    }

    public interface OnCompressListener {
        void onFail();
        void onProgress(int progress);
        void onSuccess();
    }



}