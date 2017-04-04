package com.jie.rikimaru;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.jie.rikimaru.entity.Configuration;
import com.jie.rikimaru.util.CompressUtil;
import com.jie.rikimaru.util.PermissionManager;
import com.jie.rikimaru.util.FileUtil;
import com.jie.rikimaru.entity.VideoEntity;
import com.jie.rikimaru.util.VideoUtils;
import com.jie.rikimaru.video.VideoListActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Desction：主界面
 * Author：haojie
 * date：2017-03-27
 */
public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_VIDEO = 0;
    @BindView(R.id.iv_bitmap)
    ImageView ivBitmap;
    //视频实体
    private VideoEntity videoEntity;
    //压缩进度
    private MaterialDialog progressDialog;
    //压缩配置
    private Configuration configuration;
    //分辨率
    private String[] resolution = {"360x270","480×360","640x480", "1280x720"};
    //用户选项
    private int select;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        PermissionManager.getInstance().getStartPermission(this);
        CompressUtil.compressInit(this);
    }


    @OnClick({R.id.btn_select, R.id.btn_compress})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_select:
                Intent intent = new Intent(this, VideoListActivity.class);
                startActivityForResult(intent, REQUEST_VIDEO);
                break;
            case R.id.btn_compress:
                if(videoEntity == null) {
                    Toast.makeText(MainActivity.this, "请先选择视频", Toast.LENGTH_SHORT).show();
                } else {
                    showCompressDialog();
                }
                break;
        }
    }

    /**
     * 显示压缩弹框
     */
    private void showCompressDialog() {
        new MaterialDialog.Builder(this)
                .title("请选择压缩后分辨率")
                .items(resolution)
                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        select = which;
                        return true;
                    }
                })
                .alwaysCallSingleChoiceCallback()
                .positiveText("确定")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        progressDialog = new MaterialDialog.Builder(MainActivity.this)
                                .title("视频压缩中")
                                .progress(false, 100)
                                .dismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        CompressUtil.cancel(MainActivity.this);
                                    }
                                })
                                .show();
                        gotoCompress(videoEntity);
                    }
                })
                .show();
    }

    /**
     * 启动压缩
     * @param entity
     */
    private void gotoCompress(VideoEntity entity) {
        setConfiguration(entity);
        CompressUtil.compressExe(this, configuration, new CompressUtil.OnCompressListener() {
            @Override
            public void onFail() {
                progressDialog.dismiss();
            }

            @Override
            public void onProgress(int progress) {
                progressDialog.setProgress(progress);
            }

            @Override
            public void onSuccess() {
                progressDialog.dismiss();
                new MaterialDialog.Builder(MainActivity.this)
                        .title("压缩完成")
                        .positiveText("打开视频")
                        .negativeText("取消")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.parse(configuration.outputPath), "video/mp4");
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
    }

    /**
     * 压缩配置
     * @param entity
     */
    private void setConfiguration(VideoEntity entity) {
        configuration = new Configuration();
        configuration.inputPath = entity.getFilePath();
        configuration.outputPath = FileUtil.createVideoFile();
        configuration.isLandscape = VideoUtils.isLandscape(entity.getFilePath());
        configuration.resolution = resolution[select];
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) return;
        if (requestCode == REQUEST_VIDEO) {
            videoEntity = data.getParcelableExtra("video");
            ivBitmap.setImageBitmap(videoEntity.getBitmap());
        }
    }


}
