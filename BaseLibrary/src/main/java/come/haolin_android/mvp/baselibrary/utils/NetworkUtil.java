package come.haolin_android.mvp.baselibrary.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import come.haolin_android.mvp.baselibrary.networkmonitoring.manager.NetWorkManager;
import come.haolin_android.mvp.baselibrary.networkmonitoring.type.NetType;


public class NetworkUtil {

    /**
     * 网络是否可用
     */
    @SuppressLint("MissingPermission")
    public static boolean isNetworkAvailable() {
        ConnectivityManager conneMag = (ConnectivityManager) NetWorkManager.getDefault().getApplication()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conneMag == null) return false;
        NetworkInfo[] info = conneMag.getAllNetworkInfo();
        if (info != null) {
            for (NetworkInfo networkInfo : info) {
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 获取当前的网络状态
     */
    @SuppressLint("MissingPermission")
    public static NetType getNetworkType() {
        ConnectivityManager cm = (ConnectivityManager) NetWorkManager.getDefault().getApplication()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = cm.getActiveNetworkInfo();
        if (network == null) {
            return NetType.NONE;
        }
        if (network.getType() == ConnectivityManager.TYPE_MOBILE) {
            return network.getExtraInfo().equalsIgnoreCase("cmnet") ? NetType.CMNET : NetType.CMWAP;
        } else if (network.getType() == ConnectivityManager.TYPE_WIFI) {
            return NetType.WIFI;
        }
        return NetType.NONE;

    }

}
