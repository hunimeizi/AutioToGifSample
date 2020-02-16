package com.joyrun.gifcreator;

import java.util.Locale;

/**
 * author: wneJie
 * date: 2019-10-11 18:15
 * desc:
 */
public class FFmpegUtils {



    /**
     * 抽取视频的帧并生成gif
     * @param inputVideoFile 输入的视频文件
     * @param width 缩放宽度
     * @param height 缩放高度
     * @param startTime 开始时间 秒
     * @param duration 时长 秒
     * @param frameRate 帧率 每秒多少帧
     * @param targetFile 输出gif文件
     * @return 生成gif命令
     * "-ss 00:00:00 -t 1 -i %s -vf crop=iw:ih -s 720x1080 -r 5 " + theDir + File.separator + System.currentTimeMillis() + ".gif";
     */
    public static String[] extractVideoFramesToGIF(String inputVideoFile, int startTime, int duration, int width, int height, int frameRate, String targetFile){
        /*
         * -ss  开始时间
         * -t 截取的视频长度为1s
         * -i 输入文件路径
         * -vf crop=iw/2:ih/2 表示截取视频的部分区域，其中宽为视频宽度一半，高为原视频的一半
         * -s 缩放大小为720*1080
         * -r 帧率
         * gif输出文件
         */
        String command = "ffmpeg -i %s -ss %s -t %s -vf crop=iw:ih -s %sx%s -r %s -y %s";
        command = String.format(Locale.CHINESE, command, inputVideoFile, startTime, duration, width,height,frameRate, targetFile);
        return command.split(" ");
    }


    /**
     * 抽取视频的帧并生成gif 不缩放取视频原分辨率大小
     * @param inputVideoFile 输入的视频文件
     * @param startTime 开始时间 秒
     * @param duration 时长 秒
     * @param frameRate 帧率 每秒多少帧
     * @param targetFile 输出gif文件
     * @return 生成gif命令
     */
    public static String[] extractVideoFramesToGIF(String inputVideoFile, int startTime, int duration, int frameRate, String targetFile){
        /*
         * -ss  开始时间
         * -t 截取的视频长度为1s
         * -i 输入文件路径
         * -vf crop=iw/2:ih/2 表示截取视频的部分区域，其中宽为视频宽度一半，高为原视频的一半
         * -s 缩放大小为720*1080
         * -r 帧率
         * gif输出文件
         */
        String command = "ffmpeg -i %s -ss %s -t %s -vf crop=iw:ih -r %s -y %s";
        command = String.format(Locale.CHINESE, command, inputVideoFile, startTime, duration,frameRate, targetFile);
        return command.split(" ");
    }


    /**
     * 视频抽帧转成图片
     * @param inputVideoFile 输入文件
     * @param startTime 开始时间 秒
     * @param duration 持续时间 秒
     * @param frameRate 帧率
     * @param targetFile 输出图片到指定文件夹路径
     *
     * @return 视频抽帧的命令行
     * "-i xx.mp4 -ss 7.5 -to 8.5 -s 640x320 -r 15 out.gif";
     * "-i %s -ss 0 -t 3 -r 10 -f image2 ";
     * "-ss 25 -t 10 -i **.mp4 -r 1 -s 320x240 -f image2 frame_%d.jpeg";
     */
    public static  String[] extractVideoFramesToImages(String inputVideoFile, int startTime, int duration, int frameRate, String targetFile){
        /*
         * -i 输入文件
         * -ss 开始时间
         * -t 截取的视频长度
         * -r 10每秒10帧  指定多少取每秒就去多少  不指定 每秒就去当前视频的帧率
         * -f image2指定文件格式 image2
         * targetFile 最后接输出到那个文件夹
         *
         */
        String command = "ffmpeg -i %s -ss %s -t %s -r %s -f image2 %s";
        command = String.format(Locale.CHINESE, command, inputVideoFile, startTime, duration, frameRate, targetFile);
        //每帧保存的文件的名字
        command = command + "/frame_%d.jpg";
        return command.split(" ");
    }



}
