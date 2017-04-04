package com.jie.rikimaru.video;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jie.rikimaru.R;
import com.jie.rikimaru.entity.VideoEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 类描述：视频列表适配器
 * 创建人：haojie
 * 创建时间：2017-02-18
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoHolder> {
    private Context context;
    private OnClickListener onClickListener;
    protected List<VideoEntity> mItemList = new ArrayList<>();

    public VideoListAdapter(Context context, List<VideoEntity> mItemList) {
        this.context = context;
        this.mItemList = mItemList;
    }

    /**
     * setting up a new instance to data;
     *
     * @param data
     */
    public void setNewData(List<VideoEntity> data) {
        if(data == null) return;
        if(data != mItemList) {
            mItemList.clear();
            mItemList.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }


    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        VideoHolder holder = new VideoHolder(LayoutInflater.from(context).inflate(R.layout.activity_videolist_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(VideoHolder holder, int position) {
        VideoEntity videoEntity = mItemList.get(position);
        holder.tvVideolistItemDes.setText(videoEntity.getTitle());
        holder.tvVideolistItemDuration.setText(videoEntity.getDuration());
        Glide.with(context).load(videoEntity.getFilePath()).placeholder(R.mipmap.ic_launcher).into(holder.ivVideolistItem);
        if(onClickListener != null) {
            final int pos = position;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onclick(v, pos);
                }
            });
        }
    }

    class VideoHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_videolist_item)
        ImageView ivVideolistItem;
        @BindView(R.id.tv_videolist_item_size)
        TextView tvVideolistItemSize;
        @BindView(R.id.tv_videolist_item_duration)
        TextView tvVideolistItemDuration;
        @BindView(R.id.tv_videolist_item_des)
        TextView tvVideolistItemDes;
        public VideoHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    interface OnClickListener {
        void onclick(View v, int pos);
    }
}
