package come.haolin_android.mvp.baselibrary.utils;

import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.TextView;


public class ImageHelper {


    @BindingAdapter({"relativeLayoutParamWidth", "relativeLayoutParamHeight", "relativeLayoutParamTopMargin",
            "relativeLayoutParamBottomMargin", "relativeLayoutParamLefMargin", "relativeLayoutParamRightMargin", "relativeLayoutParamAsWidth"})
    public static void ViewRelativeLayoutParam(View view, int width, int height, int topMargin, int bottomMargin, int lefMargin, int rightMargin,
                                               boolean asWidth) {
        ViewCalculateUtil.setViewRelativeLayoutParam(view, width, height, topMargin, bottomMargin, lefMargin, rightMargin, asWidth);
    }

    @BindingAdapter({"frameLayoutParamWidth", "frameLayoutParamHeight", "frameLayoutParamTopMargin",
            "frameLayoutParamBottomMargin", "frameLayoutParamLefMargin", "frameLayoutParamRightMargin", "frameLayoutParamAsWidth"})
    public static void ViewFrameLayoutParam(View view, int width, int height, int topMargin, int bottomMargin, int lefMargin,
                                            int rightMargin, boolean asWidth) {
        ViewCalculateUtil.setViewFrameLayoutParam(view, width, height, topMargin, bottomMargin, lefMargin, rightMargin, asWidth);
    }

    @BindingAdapter({"linearLayoutParamWidth", "linearLayoutParamHeight", "linearLayoutParamTopMargin",
            "linearLayoutParamBottomMargin", "linearLayoutParamLefMargin", "linearLayoutParamRightMargin", "linearLayoutParamAsWidth"})
    public static void ViewLinearLayoutParam(View view, int width, int height, int topMargin, int bottomMargin, int lefMargin,
                                             int rightMargin, boolean asWidth) {
        ViewCalculateUtil.setViewLinearLayoutParam(view, width, height, topMargin, bottomMargin, lefMargin, rightMargin, asWidth);
    }

    @BindingAdapter({"groupLayoutParamWidth", "groupLayoutParamHeight", "groupLayoutParamAsWidth"})
    public static void ViewGroupLayoutParam(View view, int width, int height, boolean asWidth) {
        ViewCalculateUtil.setViewGroupLayoutParam(view, width, height, asWidth);
    }

    @BindingAdapter({"TextViewSize"})
    public static void TextSize(TextView view, int size) {
        ViewCalculateUtil.setTextSize(view, size);
    }
}
