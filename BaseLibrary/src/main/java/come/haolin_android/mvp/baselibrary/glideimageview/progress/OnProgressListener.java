package come.haolin_android.mvp.baselibrary.glideimageview.progress;

/**
 * @author by sunfusheng on 2017/6/14.
 */
public interface OnProgressListener {
    void onProgress(boolean isComplete, int percentage, long bytesRead, long totalBytes);
}
