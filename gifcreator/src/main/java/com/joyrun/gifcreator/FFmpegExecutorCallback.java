package com.joyrun.gifcreator;


/**
 * author: wneJie
 * date: 2019-10-14 15:04
 * desc:
 */
public interface FFmpegExecutorCallback {


    void onFailure();


    void onSuccess(String gifFile);
}
