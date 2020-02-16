package come.haolin_android.mvp.baselibrary.base;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import come.haolin_android.mvp.baselibrary.R;
import come.haolin_android.mvp.baselibrary.inject.InjectPresenter;
import come.haolin_android.mvp.baselibrary.utils.DebugLog;
import come.haolin_android.mvp.baselibrary.utils.SharedPreferencesManager;
import come.haolin_android.mvp.baselibrary.utils.UIUtils;
import come.haolin_android.mvp.baselibrary.utils.immersion.ImmersionBar;
import come.haolin_android.mvp.baselibrary.view.DialogLoad;

/**
 * 所有Fragment的基类<br/>
 */
public abstract class BaseFragment<T extends ViewDataBinding> extends Fragment implements BaseView {


    /**
     * 上下文参数
     */
    protected Context mContext;


    protected ImmersionBar mImmersionBar;

    protected SharedPreferencesManager spManager;

    protected T binding;

    private DialogLoad pd;

    private List<BasePresenter> mPresenters;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setAndroidNativeLightStatusBar(true);
        }
        spManager = SharedPreferencesManager.getInstance(getActivity());
        UIUtils.getInstance(getActivity());
        mContext = getActivity();
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
                } catch (java.lang.InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        initData();
    }

    /**
     * 获取layout Id
     *
     * @return layout Id
     */
    protected abstract int getLayoutId();

    protected int getStatusBarTintColor() {
        return R.color.transparent;
    }

    /**
     * 初始化赋值操作
     */
    protected abstract void initData();


    /**
     * onActivityResult 回调方法
     *
     * @param requestCode int
     * @param resultCode  int
     * @param data        Intent
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    /**
     * 启动新的Activity并且关闭当前页面
     *
     * @param clazz 类名
     */
    @SuppressWarnings("rawtypes")
    public void startActivityAndFinishThis(Class clazz) {
        Intent intent = new Intent(mContext, clazz);
        startActivity(intent);
        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    /**
     * 启动Activity并且不关闭当前页面
     */
    public void startActivityAndDonotFinishThis(Class clazz) {
        Intent intent = new Intent(mContext, clazz);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    /**
     * 启动新的Activity并且关闭当前页面
     *
     * @param intent 意图
     */
    public void startActivityAndFinishThis(Intent intent) {
        startActivity(intent);
        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    protected void restartApplication() {
        BaseActivity BaseActivity = (BaseActivity) getActivity();
        final Intent intent = BaseActivity.getPackageManager().getLaunchIntentForPackage(BaseActivity.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();

        //解绑Presenter
        for (BasePresenter presenter : mPresenters) {
            presenter.detach();
        }
    }

    protected void initImmersionBar() {
        //在BaseActivity里初始化
        mImmersionBar = ImmersionBar.with(getActivity());
        mImmersionBar.init();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void setAndroidNativeLightStatusBar(boolean dark) {
        View decor = getActivity().getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }


    public void showLoadingDialog() {
        try {
            if (null == pd) {
                pd = new DialogLoad(getActivity());
            }
            if (!pd.isShowing()) {
                pd.showDialog();
                pd.setCancelable(false);
            }
        } catch (Exception e) {
        }
    }

    public void dismissLoadingDialog() {
        try {
            if (pd != null && pd.isShowing()) {
                pd.cancelDialog();
            }
        } catch (Exception e) {
        }
    }

}
