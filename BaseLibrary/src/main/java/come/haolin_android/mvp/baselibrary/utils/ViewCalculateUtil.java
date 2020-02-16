package come.haolin_android.mvp.baselibrary.utils;


import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ViewCalculateUtil {
    private static final String TAG = ViewCalculateUtil.class.getSimpleName();

    /**
     * 根据屏幕的大小设置view的高度，间距
     *
     * @param view
     * @param width
     * @param height
     * @param topMargin
     * @param bottomMargin
     * @param lefMargin
     * @param rightMargin
     */
    public static void setViewRelativeLayoutParam(View view, int width, int height, int topMargin, int bottomMargin, int lefMargin, int rightMargin,
                                                  boolean asWidth) {
        if (view==null)return;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();

        if (layoutParams != null) {
            if (width != RelativeLayout.LayoutParams.MATCH_PARENT && width != RelativeLayout.LayoutParams.WRAP_CONTENT && width != RelativeLayout.LayoutParams.FILL_PARENT) {
                layoutParams.width = UIUtils.getInstance().getWidth(width);
            } else {
                layoutParams.width = width;
            }
            if (height != RelativeLayout.LayoutParams.MATCH_PARENT && height != RelativeLayout.LayoutParams.WRAP_CONTENT && height != RelativeLayout.LayoutParams.FILL_PARENT) {
                layoutParams.height = asWidth ? UIUtils.getInstance().getWidth(height) : UIUtils.getInstance().getHeight(height);
            } else {
                layoutParams.height = height;
            }

            layoutParams.topMargin = asWidth ? UIUtils.getInstance().getWidth(topMargin) : UIUtils.getInstance().getHeight(topMargin);
            layoutParams.bottomMargin = asWidth ? UIUtils.getInstance().getWidth(bottomMargin) : UIUtils.getInstance().getHeight(bottomMargin);
            layoutParams.leftMargin = UIUtils.getInstance().getWidth(lefMargin);
            layoutParams.rightMargin = UIUtils.getInstance().getWidth(rightMargin);
            view.setLayoutParams(layoutParams);
        } else {
        }
    }

    /**
     * @param view
     * @param width
     * @param height
     * @param topMargin
     * @param bottomMargin
     * @param lefMargin
     * @param rightMargin
     */
    public static void setViewFrameLayoutParam(View view, int width, int height, int topMargin, int bottomMargin, int lefMargin,
                                               int rightMargin, boolean asWidth) {
        if (view==null)return;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
        if (width != RelativeLayout.LayoutParams.MATCH_PARENT && width != RelativeLayout.LayoutParams.WRAP_CONTENT && width != RelativeLayout.LayoutParams.FILL_PARENT) {
            layoutParams.width = UIUtils.getInstance().getWidth(width);
        } else {
            layoutParams.width = width;
        }
        if (height != RelativeLayout.LayoutParams.MATCH_PARENT && height != RelativeLayout.LayoutParams.WRAP_CONTENT && height != RelativeLayout.LayoutParams.FILL_PARENT) {
            layoutParams.height = asWidth ? UIUtils.getInstance().getWidth(height) : UIUtils.getInstance().getHeight(height);
        } else {
            layoutParams.height = height;
        }

        layoutParams.topMargin = asWidth ? UIUtils.getInstance().getWidth(topMargin) : UIUtils.getInstance().getHeight(topMargin);
        layoutParams.bottomMargin = asWidth ? UIUtils.getInstance().getWidth(bottomMargin) : UIUtils.getInstance().getHeight(bottomMargin);
        layoutParams.leftMargin = UIUtils.getInstance().getWidth(lefMargin);
        layoutParams.rightMargin = UIUtils.getInstance().getWidth(rightMargin);
        view.setLayoutParams(layoutParams);
    }

    /**
     * 设置view的内边距
     *
     * @param view
     * @param topPadding
     * @param bottomPadding
     * @param leftpadding
     * @param rightPadding
     */
    public static void setViewPadding(View view, int topPadding, int bottomPadding, int leftpadding, int rightPadding) {
        if (view==null)return;
        view.setPadding(UIUtils.getInstance().getWidth(leftpadding),
                UIUtils.getInstance().getHeight(topPadding),
                UIUtils.getInstance().getWidth(rightPadding),
                UIUtils.getInstance().getHeight(bottomPadding));
    }

    /**
     * 设置字号
     *
     * @param view
     * @param size
     */
    public static void setTextSize(TextView view, int size) {
        if (view==null)return;
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, UIUtils.getInstance().getHeight(size));
    }

    /**
     * 设置LinearLayout中 view的高度宽度
     *
     * @param view
     * @param width
     * @param height
     */
    public static void setViewLinearLayoutParam(View view, int width, int height, boolean asWidth) {
        if (view==null)return;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        if (width != RelativeLayout.LayoutParams.MATCH_PARENT && width != RelativeLayout.LayoutParams.WRAP_CONTENT && width != RelativeLayout.LayoutParams.FILL_PARENT) {
            layoutParams.width = UIUtils.getInstance().getWidth(width);
        } else {
            layoutParams.width = width;
        }
        if (height != RelativeLayout.LayoutParams.MATCH_PARENT && height != RelativeLayout.LayoutParams.WRAP_CONTENT && height != RelativeLayout.LayoutParams.FILL_PARENT) {
            layoutParams.height = asWidth ? UIUtils.getInstance().getWidth(height) : UIUtils.getInstance().getHeight(height);
        } else {
            layoutParams.height = height;
        }

        view.setLayoutParams(layoutParams);
    }

    public static void setViewGroupLayoutParam(View view, int width, int height, boolean asWidth) {
        if (view==null)return;
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (width != RelativeLayout.LayoutParams.MATCH_PARENT && width != RelativeLayout.LayoutParams.WRAP_CONTENT && width != RelativeLayout.LayoutParams.FILL_PARENT) {
            layoutParams.width = UIUtils.getInstance().getWidth(width);
        } else {
            layoutParams.width = width;
        }
        if (height != RelativeLayout.LayoutParams.MATCH_PARENT && height != RelativeLayout.LayoutParams.WRAP_CONTENT && height != RelativeLayout.LayoutParams.FILL_PARENT) {
            layoutParams.height = asWidth ? UIUtils.getInstance().getWidth(height) : UIUtils.getInstance().getHeight(height);
        } else {
            layoutParams.height = height;
        }
        view.setLayoutParams(layoutParams);
    }

    /**
     * 设置LinearLayout中 view的高度宽度
     *
     * @param view
     * @param width
     * @param height
     */
    public static void setViewLinearLayoutParam(View view, int width, int height, int topMargin, int bottomMargin, int lefMargin,
                                                int rightMargin, boolean asWidth) {
        if (view==null)return;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        if (width != RelativeLayout.LayoutParams.WRAP_CONTENT && width != RelativeLayout.LayoutParams.FILL_PARENT) {
            layoutParams.width = UIUtils.getInstance().getWidth(width);
        } else {
            layoutParams.width = width;
        }
        if (height != RelativeLayout.LayoutParams.WRAP_CONTENT && height != RelativeLayout.LayoutParams.FILL_PARENT) {
            layoutParams.height = asWidth ? UIUtils.getInstance().getWidth(height) : UIUtils.getInstance().getHeight(height);
        } else {
            layoutParams.height = height;
        }

        layoutParams.topMargin = asWidth ? UIUtils.getInstance().getWidth(topMargin) : UIUtils.getInstance().getHeight(topMargin);
        layoutParams.bottomMargin = asWidth ? UIUtils.getInstance().getWidth(bottomMargin) : UIUtils.getInstance().getHeight(bottomMargin);
        layoutParams.leftMargin = UIUtils.getInstance().getWidth(lefMargin);
        layoutParams.rightMargin = UIUtils.getInstance().getWidth(rightMargin);
        view.setLayoutParams(layoutParams);
    }
}
