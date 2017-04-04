package com.jie.rikimaru.entity;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 类描述：视频实体
 * 创建人：haojie
 * 创建时间：2017-02-18
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class VideoEntity implements Parcelable {
    private String title;
    private String filePath;
    private Bitmap bitmap;
    private String time;
    private String duration;
    private String size;
    private boolean isLandcape;
    public VideoEntity() {
    }

    public VideoEntity(String title, String filePath, String time, String duration) {
        this.title = title;
        this.filePath = filePath;
        this.time = time;
        this.duration = duration;
    }

    protected VideoEntity(Parcel in) {
        title = in.readString();
        filePath = in.readString();
        time = in.readString();
        duration = in.readString();
        size = in.readString();
        bitmap = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<VideoEntity> CREATOR = new Creator<VideoEntity>() {
        @Override
        public VideoEntity createFromParcel(Parcel in) {
            return new VideoEntity(in);
        }

        @Override
        public VideoEntity[] newArray(int size) {
            return new VideoEntity[size];
        }
    };



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public boolean isLandcape() {
        return isLandcape;
    }

    public void setLandcape(boolean landcape) {
        isLandcape = landcape;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(filePath);
        dest.writeString(duration);
        dest.writeString(time);
        dest.writeString(size);
        dest.writeParcelable(bitmap, flags);
    }
}
