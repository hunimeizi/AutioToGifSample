package come.haolin_android.mvp.baselibrary.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.View;

import come.haolin_android.mvp.baselibrary.R;
import come.haolin_android.mvp.baselibrary.utils.dtoast.DToast;


/**
 * Toast工具类，下一个Toast提示前会取消掉上一个Toast
 */
@SuppressLint("InflateParams")
public class ToastUtils {

    public static void showToast(Context mContext, String msg) {
        if (mContext == null || msg == null) return;
        DToast.make(mContext)
                .setView(View.inflate(mContext, R.layout.common_mytoast_layout, null))
                .setText(R.id.text, msg)
                .setGravity(Gravity.TOP, 0, (int) (0.80 * PhoneUitls.getScreenHeight(mContext)))
                .showLong();
    }

    public static void showToast(Context mContext, int msg) {
        if (mContext == null || msg == 0) return;
        DToast.make(mContext)
                .setView(View.inflate(mContext, R.layout.common_mytoast_layout, null))
                .setText(R.id.text, mContext.getString(msg))
                .setGravity(Gravity.TOP, 0, (int) (0.80 * PhoneUitls.getScreenHeight(mContext)))
                .showLong();
    }

    //退出APP时调用
    public static void cancelAll() {
        DToast.cancel();
    }
}