package come.haolin_android.mvp.baselibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 软键盘工具类<br/>
 * 控制显示或是隐藏软键盘
 */
public class SoftKeyboardUtil {

    /**
     * 显示软键盘
     *
     * @param view 当前获取焦点的View
     */
    public static void showKeyboard(View view) {
        if (null == view) {
            return;
        }
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    /**
     * 显示软键盘
     *
     * @param activity 当前页面对象
     */
    public static void showKeyboard(Activity activity) {
        if (null == activity) {
            return;
        }
        InputMethodManager im = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = activity.getCurrentFocus();
        if (v != null) {
            im.showSoftInput(v, InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
    }

    /**
     * 隐藏软键盘
     *
     * @param activity 当前页面对象
     */
    public static void hideKeyboard(Activity activity) {
        if (null == activity) {
            return;
        }
        InputMethodManager im = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = activity.getCurrentFocus();
        if (v != null) {
            im.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 隐藏软键盘
     *
     * @param view 当前页面对象
     */
    public static void hideKeyboard(View view) {
        if (null == view) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
