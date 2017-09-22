# Rikimaru视频压缩demo

> 最近项目中用到了视频压缩，想到就头皮发麻，查阅了很多相关ffmpeg的资料，底层用的是[ffmpeg-android-java]{https://github.com/WritingMinds/ffmpeg-android-java} 的开源库，主要封装了对参数的控制，压缩进度的可视化显示。

## 展示

![这里写图片描述](http://img.blog.csdn.net/20170508110208630?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvYW50aG9ueV8z/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
![这里写图片描述](http://img.blog.csdn.net/20170508110215130?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvYW50aG9ueV8z/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

## 目录说明
* media文件夹没有用，有几个工具类作为参考的
* util文件夹内CompressUtil是压缩工具类
* util文件夹内VideoUtil是处理视频工具类
##使用压缩
压缩前需要配置压缩参数

```
    /**
     * 压缩配置
     * @param entity
     */
    private void setConfiguration(VideoEntity entity) {
        configuration = new Configuration();
        //输入路径
        configuration.inputPath = entity.getFilePath();
        //输出路径
        configuration.outputPath = FileUtil.createVideoFile();
        //是否横屏
        configuration.isLandscape = VideoUtils.isLandscape(entity.getFilePath());
        //设置分辨率
        configuration.resolution = resolution[select];
    }
```

压缩核心代码在CompressUtil类，compressExe方法启动压缩：

```
    //1.获取视频时长
    //2.设置横竖比
    //3.根据参数配置设置参数
    //4.启动ffmpeg压缩
    //5.根据信息获取压缩进度
```

cancel方法取消压缩


## 视频压缩主要用到参数
* -strict strictness 跟标准的严格性
* -vcodec codec 强制使用codec编解码方式。如果用copy表示原始编解码数据必须被拷贝。
* -acodec codec 使用codec编解码
* -ar freq 设置音频采样率
* -ac channels 设置通道 缺省为1
* -b bitrate 设置比特率，缺省200kb/s
* -s size 设置帧大小 格式为WXH 缺省160X128.下面的简写也可以直接使用：
* -aspect aspect 设置横纵比 4:3 16:9 或 1.3333 1.7777


