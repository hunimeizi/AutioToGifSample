package come.haolin_android.mvp.baselibrary.utils.compress.listener;


import java.util.ArrayList;

import come.haolin_android.mvp.baselibrary.bean.PhotoBean;

/**
 * 图片集合的压缩返回监听
 */
public interface CompressImage {

    // 开始压缩
    void compress();

    // 图片集合的压缩结果返回
    interface CompressListener {

        // 成功
        void onCompressSuccess(ArrayList<PhotoBean> images);

        // 失败
        void onCompressFailed(ArrayList<PhotoBean> images, String error);
    }
}
