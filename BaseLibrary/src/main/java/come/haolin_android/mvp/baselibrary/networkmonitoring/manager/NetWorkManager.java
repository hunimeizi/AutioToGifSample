package come.haolin_android.mvp.baselibrary.networkmonitoring.manager;


import android.annotation.SuppressLint;
import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import come.haolin_android.mvp.baselibrary.networkmonitoring.NetWorkReceiver;


public class NetWorkManager {

    private static volatile NetWorkManager instance;
    private Application application;
    private NetWorkReceiver receiver;

    @SuppressLint("NewApi")
    public NetWorkManager() {
        receiver = new NetWorkReceiver();
    }

    public static NetWorkManager getDefault() {
        if (instance == null) {
            synchronized (NetWorkManager.class) {
                if (instance == null) {
                    instance = new NetWorkManager();
                }
            }
        }
        return instance;
    }

    public void init(Application application) {
        this.application = application;
        //创建意图过滤器
        IntentFilter filter = new IntentFilter();
        //添加动作，监听网络
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        application.registerReceiver(receiver, filter);
    }

    public Application getApplication() {
        if (application == null) {
            throw new RuntimeException();
        }
        return application;
    }

    @SuppressLint("NewApi")
    public void registerObserver(Object register) {
        receiver.registerObserver(register);
    }

    @SuppressLint("NewApi")
    public void unRegisterObserver(Object register) {
        receiver.unRegisterObserver(register);
    }

    @SuppressLint("NewApi")
    public void unRegisterAllObserver() {
        receiver.unRegisterAllObserver();
    }
}
