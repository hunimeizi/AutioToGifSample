package come.haolin_android.mvp.baselibrary.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import come.haolin_android.mvp.baselibrary.R;
import come.haolin_android.mvp.baselibrary.inject.InjectPresenter;
import come.haolin_android.mvp.baselibrary.utils.DebugLog;
import come.haolin_android.mvp.baselibrary.utils.SharedPreferencesManager;
import come.haolin_android.mvp.baselibrary.utils.SoftKeyboardUtil;
import come.haolin_android.mvp.baselibrary.utils.UIUtils;
import come.haolin_android.mvp.baselibrary.utils.immersion.ImmersionBar;
import come.haolin_android.mvp.baselibrary.view.DialogLoad;

public abstract class BaseActivity<T extends ViewDataBinding> extends AppCompatActivity implements BaseView {

    protected abstract int getLayoutId();
    protected T binding = null;

    protected Context mContext;
    protected ImmersionBar mImmersionBar;
    private DialogLoad pd;
    protected SharedPreferencesManager spManager;
    public static Map<String, Activity> foregroundActivityMap = new HashMap<>();

    private List<BasePresenter> mPresenters;

    protected TextView titleBar_back_tv;
    protected TextView titleBar_title_tv;
    protected TextView titleBar_more_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        spManager = SharedPreferencesManager.getInstance(this);
        foregroundActivityMap.put(this.getClass().getName(), this);
        DebugLog.EChan(this.getClass().getSimpleName());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        UIUtils.getInstance(mContext);
        mPresenters = new ArrayList<>();

        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            InjectPresenter injectPresenter = field.getAnnotation(InjectPresenter.class);
            if (injectPresenter != null) {
                //创建注入
                Class<? extends BasePresenter> presenterClazz = null;
                try {
                    //获取这个类
                    presenterClazz = (Class<? extends BasePresenter>) field.getType();
                } catch (Exception e) {
                    // 乱七八糟一些注入
                    throw new RuntimeException("not support inject presenter" + field.getType());
                }
                try {
                    //创建BasePresenter对象
                    BasePresenter basePresenter = presenterClazz.newInstance();
                    //attach
                    basePresenter.attach(this);
                    mPresenters.add(basePresenter);
                    field.setAccessible(true);
                    field.set(this, basePresenter);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        initBase();
    }

    /**
     * 初始化资源layout，初始化布局加载器，初始化imgLoader，Activity入栈，查找控件，设置监听
     */
    private void initBase() {
        // 启动activity时不自动弹出软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //初始化沉浸式
        initImmersionBar();
        initTitleView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setAndroidNativeLightStatusBar(true);
        }
    }

    private void initTitleView() {
        binding = DataBindingUtil.setContentView(this, getLayoutId());
        initViews();
    }

    protected void initImmersionBar() {
        //在BaseActivity里初始化
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void setAndroidNativeLightStatusBar(boolean dark) {
        View decor = getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    /**
     * 数据处理
     */
    protected abstract void initViews();

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        SoftKeyboardUtil.hideKeyboard(this);
    }


    /**
     * 启动Activity并且不关闭当前页面
     */
    public void startActivityAndDonotFinishThis(Class clazz) {
        Intent intent = new Intent(getApplicationContext(), clazz);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    /**
     * 启动新页面不关闭当前页面
     *
     * @param intent 意图
     */
    public void startActivityAndNotFinishThis(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    /**
     * 启动新的Activity并且关闭当前页面
     *
     * @param intent 意图
     */
    public void startActivityAndFinishThis(Intent intent) {
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    public void startActivityAndFinishThis(Class clazz) {
        Intent intent = new Intent(getApplicationContext(), clazz);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = res.getConfiguration();
        config.fontScale = 1.1f;
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null) {
            mImmersionBar.destroy();  //在BaseActivity里销毁
        }
        foregroundActivityMap.remove(this.getClass().getName());
        SoftKeyboardUtil.hideKeyboard(this);
        //解绑Presenter
        for (BasePresenter presenter : mPresenters) {
            presenter.detach();
        }
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

    public void showLoadingDialog() {
        try {
            if (null == pd) {
                pd = new DialogLoad(this);
            }
            if (!pd.isShowing()) {
                pd.showDialog();
                pd.setCancelable(false);
            }
        } catch (Exception e) {
            DebugLog.e("showLoadingDialog error.");
        }
    }

    public void dismissLoadingDialog() {
        try {
            if (pd != null && pd.isShowing()) {
                pd.cancelDialog();
            }
        } catch (Exception e) {
            DebugLog.e("dismissLoadingDialog error.");
        }
    }

    protected void finishAllActivity() {
        try {
            for (String key : foregroundActivityMap.keySet()) {
                if (!key.equals(this.getClass().getName())) {
                    DebugLog.EChan("finish " + foregroundActivityMap.get(key));
                    foregroundActivityMap.get(key).finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();
    }


    public void setTitleBarView(){
        Toolbar toolbar = findViewById(R.id.toolbar_title);
        ImmersionBar.setTitleBar(this, toolbar);
        titleBar_back_tv = findViewById(R.id.titleBar_back_tv);
        titleBar_back_tv.setOnClickListener(v -> finish());
        titleBar_title_tv = findViewById(R.id.titleBar_title_tv);
        titleBar_more_tv = findViewById(R.id.titleBar_more_tv);

    }
    public void setTitleBarMoreLeftIcon(int icon) {
        titleBar_more_tv.setVisibility(View.VISIBLE);
        Drawable leftDrawable = getResources().getDrawable(icon);
        leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
        titleBar_more_tv.setCompoundDrawables(leftDrawable, null, null, null);
    }

}
