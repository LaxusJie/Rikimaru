package com.jie.rikimaru.video;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.jie.rikimaru.R;
import com.jie.rikimaru.entity.VideoEntity;
import com.jie.rikimaru.util.DividerGridItemDecoration;
import com.jie.rikimaru.util.VideoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 类描述：视频列表页面
 * 创建人：haojie
 * 创建时间：2016-12-08
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class VideoListActivity extends AppCompatActivity {

    @BindView(R.id.rv_videolist)
    RecyclerView rvVideolist;
    private VideoListAdapter videoListAdapter;
    private List<VideoEntity> videoList = new ArrayList<>();
    private static final int FINISH = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videolist);
        ButterKnife.bind(this);
        initRecycleView();
        runThread();
//        MediaUtils.getMediaWithVideoList(this, String.valueOf(Integer.MIN_VALUE), 1, 23);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //更新adapter
            videoListAdapter.setNewData(videoList);
        }
    };

    private void initRecycleView() {
        //设置布局管理器
        rvVideolist.setLayoutManager(new GridLayoutManager(this, 3));
        //设置Item增加、移除动画
        rvVideolist.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        rvVideolist.addItemDecoration(new DividerGridItemDecoration(this));
        videoListAdapter = new VideoListAdapter(this, videoList);
        videoListAdapter.setOnClickListener(new VideoListAdapter.OnClickListener() {
            @Override
            public void onclick(View v, int pos) {
                if (verify(pos)) return;
                VideoEntity entity = videoList.get(pos);
                Bitmap bitmap = VideoUtils.getVideoThumbnail(entity.getFilePath(), 300, 225, MediaStore.Images.Thumbnails.MINI_KIND);
                entity.setBitmap(bitmap);
                Intent intent = getIntent();
                intent.putExtra("video", videoList.get(pos));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        rvVideolist.setAdapter(videoListAdapter);
    }

    /**
     * 验证视频大小
     *
     * @param position
     * @return
     */
    private boolean verify(int position) {
        long time = Long.parseLong(VideoUtils.getTime(videoList.get(position).getFilePath()));
        if(time > 120000) {
            Toast.makeText(VideoListActivity.this, "暂不支持大于2分钟的视频", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void runThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                videoList.addAll(VideoUtils.getAllVideo(VideoListActivity.this));
                handler.sendEmptyMessage(FINISH);
            }
        }).start();
    }
}
