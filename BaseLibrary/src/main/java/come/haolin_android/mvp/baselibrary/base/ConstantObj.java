package come.haolin_android.mvp.baselibrary.base;

import android.os.Environment;

import come.haolin_android.mvp.baselibrary.BuildConfig;

/**
 * 共同参数
 */
public class ConstantObj {

    // 相机
    public final static int CAMERA_CODE = 1001;
    // 相册
    public final static int ALBUM_CODE = 1002;
    // 缓存根目录
    public final static String BASE_CACHE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/";
    // 压缩后缓存路径
    public final static String COMPRESS_CACHE = "compress_cache";

    public static String language = "";
    public static final String ANDROID_NET_CHANGE_ACTION= "android.net.conn.CONNECTIVITY_CHANGE";

    public static String URL = BuildConfig.URL;

    //支付方式
    public static int PAY_WAY = 0; // 1 微信支付 2支付宝支付 3银联
}

