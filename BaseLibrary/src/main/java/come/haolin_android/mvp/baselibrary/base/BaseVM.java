package come.haolin_android.mvp.baselibrary.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import come.haolin_android.mvp.baselibrary.R;

/**
 * 创建人：lyb
 * 创建时间：2020/1/16 16:53
 * class:
 */
public class BaseVM {


    /**
     * 启动Activity并且不关闭当前页面
     */
    public void startActivityAndDonotFinishThis(Context mContext, Class clazz) {
        Intent intent = new Intent(mContext, clazz);
        mContext.startActivity(intent);
        ((Activity) mContext).overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    /**
     * 启动新页面不关闭当前页面
     *
     * @param intent 意图
     */
    public void startActivityAndNotFinishThis(Context mContext, Intent intent) {
        mContext.startActivity(intent);
        ((Activity) mContext).overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    /**
     * 启动新的Activity并且关闭当前页面
     *
     * @param intent 意图
     */
    public void startActivityAndFinishThis(Context mContext,Intent intent) {
        mContext.startActivity(intent);
        ((Activity) mContext).finish();
        ((Activity) mContext).overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    public void startActivityAndFinishThis(Context mContext,Class clazz) {
        Intent intent = new Intent(mContext, clazz);
        mContext.startActivity(intent);
        ((Activity) mContext).finish();
        ((Activity) mContext).overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    /**
     * 获取EditText中的内容
     *
     * @param et EditText
     * @return EditText内容
     */
    public String getEditTextContent(EditText et) {
        return et.getText().toString().trim();
    }

    /**
     * 获取TextView中内容
     *
     * @param textView TextView
     * @return 内容
     */
    public String getTextViewContent(TextView textView) {
        return textView.getText().toString().trim();
    }

}
