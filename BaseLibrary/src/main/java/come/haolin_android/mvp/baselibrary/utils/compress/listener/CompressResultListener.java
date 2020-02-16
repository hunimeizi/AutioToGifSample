package come.haolin_android.mvp.baselibrary.utils.compress.listener;

/**
 * 单张图片压缩时的监听
 */
public interface CompressResultListener {

    // 成功
    void onCompressSuccess(String imgPath);

    // 失败
    void onCompressFailed(String imgPath, String error);
}
