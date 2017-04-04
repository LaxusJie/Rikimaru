package com.jie.rikimaru.util;

/**
 * 类描述：视频工具类
 * 创建人：haojie
 * 创建时间：2017-02-18
 * 修改人：
 * 修改时间：
 * 修改备注：
 */

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import com.jie.rikimaru.entity.VideoEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取本地视频的工具类
 */
public class VideoUtils {

    /**
     * 获取视频的缩略图
     * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
     * @param videoPath 视频的路径
     * @param width 指定输出视频缩略图的宽度
     * @param height 指定输出视频缩略图的高度度
     * @param kind 参照MediaStore.Images(Video).Thumbnails类中的常量MINI_KIND和MICRO_KIND。
     *            其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     */
    public static Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
        Bitmap bitmap = null;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        if(bitmap!= null){
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }

    /**
     * 获取所有视频
     * @param context
     * @return
     */
    public static List<VideoEntity> getAllVideo(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        String[] projection = new String[]{MediaStore.Video.Media.TITLE, MediaStore.Video.Media.DATA, MediaStore.Video.Media.DURATION};
        Cursor cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection,
                null, null, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
        cursor.moveToFirst();
        int fileNum = cursor.getCount();
        List videoList = new ArrayList<VideoEntity>();
        for(int counter = 0; counter < fileNum; counter++){
            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
            String time = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
            String duration = formatTime(Long.parseLong(time));
            videoList.add(new VideoEntity(title, path, time, duration));
            cursor.moveToNext();
        }
        cursor.close();
        return videoList;
    }

    /**
     * @deprecated
     * 通过MediaMetadataRetriever获取文件信息时间过长
     * 推荐用ContentResolver获取
     * @param videoList
     * @param title
     * @param path
     */
    @NonNull
    private static void getVideoDetail(List videoList, String title, String path) {
        String time = getTime(path);
        Bitmap bitmap = VideoUtils.getVideoThumbnail(path, 300, 225, MediaStore.Images.Thumbnails.MINI_KIND);
        String size = showLongFileSzie(new File(path).length());
        boolean isLandscape = isLandscape(path);
    }

    /**
     * 获取宽高
     * @param path
     * @return
     */
    public static boolean isLandscape(String path) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        try {
            mmr.setDataSource(path);
            if(mmr.getFrameAtTime().getWidth() > mmr.getFrameAtTime().getHeight()) {
                return true;
            } else {
                return false;
            }
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    /**
     * 获取时间长度
     * @param path
     * @return
     */
    public static String getTime(String path) {
        String time = "";
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        try {
            mmr.setDataSource(path);
            time = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION); // 播放时长单位为毫秒
        } catch (IllegalArgumentException e) {
            return "0";
        }
        return time;
    }

    /**
     * 毫秒转化时分秒毫秒
     * @param ms
     * @return
     */
    public static String formatTime(Long ms) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        StringBuffer sb = new StringBuffer();
        if(day > 0) {
            sb.append(day+"天");
        }
        if(hour > 0) {
            sb.append(hour+"小时");
        }
        if(minute > 0) {
            sb.append(minute+"分");
        }
        if(second > 0) {
            sb.append(second+"秒");
        }
//        if(milliSecond > 0) {
//            sb.append(milliSecond+"毫秒");
//        }
        return sb.toString();
    }

    /**
     * 将时分秒转换为毫秒
     * @return
     */
    public static long formatSs(String time) {
        String[] str = time.split(":");
        long h = Long.parseLong(str[0]) * 3600000;
        long m = Long.parseLong(str[1]) * 60000;
        long s = Long.parseLong(str[2]) * 1000;
        return h + m + s;
    }

    /****
     * 计算文件大小
     *
     * @param length
     * @return
     */
    public static String showLongFileSzie(Long length) {
        if (length >= 1048576) {
            return (length / 1048576) + "MB";
        } else if (length >= 1024) {
            return (length / 1024) + "KB";
        } else if (length < 1024) {
            return length + "B";
        } else {
            return "0KB";
        }
    }

}
