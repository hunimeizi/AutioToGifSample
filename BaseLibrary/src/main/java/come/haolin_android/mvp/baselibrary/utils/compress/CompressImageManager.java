package come.haolin_android.mvp.baselibrary.utils.compress;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;

import come.haolin_android.mvp.baselibrary.bean.PhotoBean;
import come.haolin_android.mvp.baselibrary.utils.compress.config.CompressConfig;
import come.haolin_android.mvp.baselibrary.utils.compress.core.CompressImageUtil;
import come.haolin_android.mvp.baselibrary.utils.compress.listener.CompressImage;
import come.haolin_android.mvp.baselibrary.utils.compress.listener.CompressResultListener;

/**
 * 框架：思路、起稿。千万不要过度封装
 * 压缩图片管理类
 * 1、单例？
 * 2、能否重复压缩？
 */
public class CompressImageManager implements CompressImage {

    private CompressImageUtil compressImageUtil; // 压缩工具类
    private ArrayList<PhotoBean> images; // 要压缩的图片集合
    private CompressImage.CompressListener listener; // 压缩监听，告知MainActivity
    private CompressConfig config; // 压缩配置

    /**
     * 私有实现
     *
     * @param context 上下文
     * @param config 配置
     * @param images 图片集合
     * @param listener 监听
     * @return
     */
    private CompressImageManager(Context context, CompressConfig config,
                                 ArrayList<PhotoBean> images, CompressListener listener) {
        compressImageUtil = new CompressImageUtil(context, config);
        this.config = config;
        this.images = images;
        this.listener = listener;
    }

    /**
     * 静态方法，new实现
     *
     * @param context 上下文
     * @param config 配置
     * @param images 图片集合
     * @param listener 监听
     * @return
     */
    public static CompressImage build(Context context, CompressConfig config,
                                      ArrayList<PhotoBean> images, CompressImage.CompressListener listener) {
        return new CompressImageManager(context, config, images, listener);
    }

    @Override
    public void compress() {
        if (images == null || images.isEmpty()) {
            listener.onCompressFailed(images, "集合为空");
            return;
        }

        for (PhotoBean image : images) {
            if (image == null) {
                listener.onCompressFailed(images, "某图片为空");
                return;
            }
        }

        // 开始递归压缩，从第一张开始
        compress(images.get(0));
    }

    // 从第一张开始，index = 0
    private void compress(final PhotoBean image) {
        // 路径为空
        if (TextUtils.isEmpty(image.getOriginalPath())) {
            // 继续
            continueCompress(image, false);
            return;
        }

        // 文件不存在
        File file = new File(image.getOriginalPath());
        if (!file.exists() || !file.isFile()) {
            continueCompress(image, false);
            return;
        }

        // < 200KB
        if (file.length() < config.getMaxSize()) {
            continueCompress(image, true);
            return;
        }

        // 单张压缩
        compressImageUtil.compress(image.getOriginalPath(), new CompressResultListener() {
            @Override
            public void onCompressSuccess(String imgPath) {
                image.setCompressPath(imgPath);
                continueCompress(image, true);
            }

            @Override
            public void onCompressFailed(String imgPath, String error) {
                continueCompress(image, false, error);
            }
        });
    }

    private void continueCompress(PhotoBean image, boolean bool, String... error) {
        image.setCompressed(bool);
        // 当前图片的索引
        int index = images.indexOf(image);
        if (index == images.size() - 1) { // 最后一张
            handlerCallback(error);
        } else {
            compress(images.get(index + 1));
        }
    }

    private void handlerCallback(String... error) {
        if (error.length > 0) {
            listener.onCompressFailed(images, error[0]);
            return;
        }

        for (PhotoBean image : images) {
            // 如果存在没有压缩的图片，或者压缩失败的
            if (!image.isCompressed()) {
                listener.onCompressFailed(images, image.getOriginalPath() + "压缩失败");
                return;
            }
        }

        listener.onCompressSuccess(images);
    }
}
