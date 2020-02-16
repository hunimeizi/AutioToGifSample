package come.haolin_android.mvp.baselibrary.networkmonitoring;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import come.haolin_android.mvp.baselibrary.base.ConstantObj;
import come.haolin_android.mvp.baselibrary.bean.MethodManager;
import come.haolin_android.mvp.baselibrary.networkmonitoring.annotation.NetWork;
import come.haolin_android.mvp.baselibrary.networkmonitoring.manager.NetWorkManager;
import come.haolin_android.mvp.baselibrary.networkmonitoring.type.NetType;
import come.haolin_android.mvp.baselibrary.utils.DebugLog;
import come.haolin_android.mvp.baselibrary.utils.NetworkUtil;


@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class NetWorkReceiver extends BroadcastReceiver {

    private NetType netType;
    private Map<Object, List<MethodManager>> netWorkList;

    public NetWorkReceiver() {
        this.netType = NetType.NONE;
        netWorkList = new HashMap<>();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) return;
        if (intent.getAction().equalsIgnoreCase(ConstantObj.ANDROID_NET_CHANGE_ACTION)) {
            DebugLog.EChan("网络发生变化了");
            netType = NetworkUtil.getNetworkType();
            post(netType);
        }
    }

    // 消息分发
    private void post(NetType netType) {
        //拿到所有的activity
        Set<Object> set = netWorkList.keySet();
        //比如mainActivity
        for (Object object : set) {
            List<MethodManager> methodManagerList = netWorkList.get(object);
            if (methodManagerList != null) {
                //循环每个方法 发送网络变化消息
                for (MethodManager method : methodManagerList) {
                    if (method.getType().isAssignableFrom(netType.getClass())) {
                        switch (method.getNetType()) {
                            case AUTO:
                                invoke(method, object, netType);
                                break;
                            case CMWAP:
                            case CMNET:
                                if (netType == NetType.CMWAP || netType == NetType.CMNET || netType == NetType.NONE) {
                                    invoke(method, object, netType);
                                }
                                break;
                            case WIFI:
                                if (netType == NetType.WIFI || netType == NetType.NONE) {
                                    invoke(method, object, netType);
                                }
                                break;
                        }
                    }
                }
            }
        }
    }

    private void invoke(MethodManager method, Object object, NetType netType) {
        try {
            method.getMethod().invoke(object, netType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerObserver(Object register) {
        //将mainActivity所有网络注解的方法加入集合
        List<MethodManager> methodList = netWorkList.get(register);
        if (methodList == null) {
            methodList = findAnnotationMethod(register);
            netWorkList.put(register, methodList);
        }
    }

    private List<MethodManager> findAnnotationMethod(Object register) {
        List<MethodManager> methodList = new ArrayList<>();
        Class<?> clazz = register.getClass();
        Method[] methods = clazz.getMethods();//获取父类的公共方法
        for (Method method : methods) {
            //获取方法注解
            NetWork netWork = method.getAnnotation(NetWork.class);
            if (netWork == null) continue;
            //注解方法的校验
            Type returnType = method.getGenericReturnType();
            if (!"void".equalsIgnoreCase(returnType.toString())) {
                throw new RuntimeException(method.getName() + "这个方法返回不是void");
            }
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != 1) {
                throw new RuntimeException(method.getName() + "方法有且只有一个参数");
            }
            for (Class<?> parameterType : parameterTypes) {
                if (!parameterType.isAssignableFrom(netType.getClass())) {
                    throw new RuntimeException(method.getName() + "参数类型不匹配");
                }
            }
            //过滤了所有的方法 找到了要添加的集合
            MethodManager manager = new MethodManager(parameterTypes[0], netWork.netType(), method);
            methodList.add(manager);
        }

        return methodList;
    }

    public void unRegisterObserver(Object register) {
        if (netWorkList != null) {
            netWorkList.remove(register);
        }
        DebugLog.EChan("注销成功了");
    }

    public void unRegisterAllObserver() {
        if (netWorkList != null) {
            netWorkList.clear();
        }
        //注销广播
        NetWorkManager.getDefault().getApplication().unregisterReceiver(this);
        DebugLog.EChan("注销所有网络监听广播成功");
    }
}
