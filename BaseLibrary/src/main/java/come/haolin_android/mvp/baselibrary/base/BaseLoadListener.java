package come.haolin_android.mvp.baselibrary.base;

public interface BaseLoadListener<T> {
    /**
     * 加载数据成功
     *
     */
    void loadSuccess(Object response);

    /**
     * 加载失败
     *
     * @param message
     */
    void loadFailure(String message);

    /**
     * 开始加载
     */
    void loadStart();

    /**
     * 加载结束
     */
    void loadComplete();
}
