package come.haolin_android.mvp.baselibrary.base;

import android.app.Application;

import come.haolin_android.mvp.baselibrary.networkmonitoring.manager.NetWorkManager;


/**
 * 作者：haoLin_Lee on 2019/10/16 23:12
 * 邮箱：Lhaolin0304@sina.com
 * class:
 */
public class BaseApplication extends Application {

    private static BaseApplication INSTANCE;

    public static BaseApplication getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        NetWorkManager.getDefault().init(this);
    }

}
