package com.joyrun.gifcreator;

import android.os.AsyncTask;

import java.io.File;


/**
 * author: wneJie
 * date: 2019-10-14 15:15
 * desc:
 */
public class FFmpegHandler {

    /**
     * @param command                commond[]
     * @param fFmpegExecutorCallback callback
     * @see FFmpegUtils
     */
    public void execute(String[] command, FFmpegExecutorCallback fFmpegExecutorCallback) {
        FFmpegExecutorAsyncTask fFmpegExecutorAsyncTask = new FFmpegExecutorAsyncTask(fFmpegExecutorCallback);
        fFmpegExecutorAsyncTask.execute(command);
    }


    private static class FFmpegExecutorAsyncTask extends AsyncTask<String[], Integer, String> {


        private FFmpegExecutorCallback fFmpegExecutorCallback;

        FFmpegExecutorAsyncTask(FFmpegExecutorCallback fFmpegExecutorCallback) {
            this.fFmpegExecutorCallback = fFmpegExecutorCallback;
        }


        @Override
        protected String doInBackground(String[]... strings) {
            FFmpegExecutor fFmpegExecutor = new FFmpegExecutor();
            String[] command = strings[0];
            String filePath = command[command.length - 1];
            fFmpegExecutor.executeFFmpegCommond(strings[0]);
            return filePath;
        }

        @Override
        protected void onPostExecute(String filePath) {
            super.onPostExecute(filePath);
            if (fFmpegExecutorCallback != null) {
                File gif = new File(filePath);
                if (gif.isFile() && gif.getName().endsWith(".gif")) {
                    fFmpegExecutorCallback.onSuccess(filePath);
                } else {
                    fFmpegExecutorCallback.onFailure();
                }
            }
        }
    }
}
