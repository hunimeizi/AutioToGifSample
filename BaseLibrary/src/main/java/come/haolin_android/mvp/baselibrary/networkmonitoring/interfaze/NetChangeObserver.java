package come.haolin_android.mvp.baselibrary.networkmonitoring.interfaze;

import come.haolin_android.mvp.baselibrary.networkmonitoring.type.NetType;

public interface NetChangeObserver {

    //网络连接
    void onConnect(NetType netType);

    //网络断开
    void onDisConnect();

}
