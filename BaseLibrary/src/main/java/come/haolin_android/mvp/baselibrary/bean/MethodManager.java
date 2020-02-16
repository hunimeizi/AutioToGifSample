package come.haolin_android.mvp.baselibrary.bean;


import java.lang.reflect.Method;

import come.haolin_android.mvp.baselibrary.networkmonitoring.type.NetType;

public class MethodManager {

    public MethodManager(Class<?> type, NetType netType, Method method) {
        this.type = type;
        this.netType = netType;
        this.method = method;
    }

    //参数类型NetType netType
    private Class<?> type;
    //网络类型
    private NetType netType;
    //执行的方法 netWork(NetType netType)
    private Method method;


    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public NetType getNetType() {
        return netType;
    }

    public void setNetType(NetType netType) {
        this.netType = netType;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
