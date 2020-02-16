package com.joyrun.gifcreator;

/**
 * author: wneJie
 * date: 2019-10-11 17:15
 * desc:
 */
public class FFmpegExecutor {


    static {
        System.loadLibrary("avutil-55");
        System.loadLibrary("avcodec-57");
        System.loadLibrary("avformat-57");
        System.loadLibrary("avdevice-57");
        System.loadLibrary("swresample-2");
        System.loadLibrary("swscale-4");
        System.loadLibrary("avfilter-6");
        System.loadLibrary("ffmpeg");
    }

    /**
     * 执行commond
     *
     * @param commands commond
     * @return int
     */
    public native int executeFFmpegCommond(String[] commands);
}
