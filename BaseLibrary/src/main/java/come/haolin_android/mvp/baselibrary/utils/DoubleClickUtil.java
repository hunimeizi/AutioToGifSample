package come.haolin_android.mvp.baselibrary.utils;

import android.content.Context;
import android.os.Handler;

import come.haolin_android.mvp.baselibrary.R;


/*
 * 双击退出辅助
 */
public class DoubleClickUtil {

    public static final String TAG = DoubleClickUtil.class.getSimpleName();

    private final long TIME_DELAYED =3000;

    private Handler handler;

    private int clickIndex = 0;

    private static DoubleClickUtil instance;

    public static DoubleClickUtil getInstance() {
        if (null == instance) {
            instance = new DoubleClickUtil();
        }
        return instance;
    }

    /**
     * 双击退出辅助初始化<br/>
     * 初始化Handler，确保在主线程创建Handler
     *
     * @param handler
     */
    public void initHandler(Handler handler) {
        if ( null == handler) {
            return;
        }
        this.handler = handler;
    }

    /**
     * 双击退出<br/>
     * 2s内点击两次退出按钮
     *
     * @return true 达到退出条件 <br/>false 未达成条件
     */
    public boolean backExit(Context context) {
        if (null == context || null == handler) {
            return true;
        }

        if (clickIndex == 1) {
            clickIndex = 0;
            return true;
        }
        clickIndex++;
        handler.removeCallbacks(clickRunnable);
        handler.postDelayed(clickRunnable, TIME_DELAYED);
        ToastUtils.showToast(context, R.string.main_exit_tips);
        return false;
    }

    private Runnable clickRunnable = () -> clickIndex = 0;
}
