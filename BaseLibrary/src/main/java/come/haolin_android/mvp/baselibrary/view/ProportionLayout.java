package come.haolin_android.mvp.baselibrary.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import come.haolin_android.mvp.baselibrary.R;
import come.haolin_android.mvp.baselibrary.utils.DebugLog;

public class ProportionLayout extends FrameLayout {

    private boolean isBaseWidth = true;
    private float proportion = 1.0F;

    public ProportionLayout(Context context) {
        super(context);
        this.initView(context, (AttributeSet)null);
    }

    public ProportionLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context, attrs);
    }

    public ProportionLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.initView(context, attrs);
    }

    @SuppressLint("ResourceType")
    private void initView(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ProportionLayout);
            if (a != null) {
                this.isBaseWidth = a.getBoolean(1, this.isBaseWidth);
                String value = a.getString(0);
                a.recycle();
                if (value != null) {
                    if (value.contains(":")) {
                        String[] tmp = value.split(":");
                        if (tmp.length == 2) {
                            tmp[0] = tmp[0].trim();
                            tmp[1] = tmp[1].trim();
                            if (TextUtils.isDigitsOnly(tmp[0]) && TextUtils.isDigitsOnly(tmp[1])) {
                                int x = Integer.parseInt(tmp[0]);
                                int y = Integer.parseInt(tmp[1]);
                                if (x > 0 && y > 0) {
                                    this.proportion = (float)x * 1.0F / (float)y;
                                }
                            }
                        }
                    } else {
                        try {
                            this.proportion = Float.parseFloat(value);
                        } catch (NumberFormatException var8) {
                            DebugLog.EChan("init fail NumberFormatException!");
                        }
                    }
                }
            }
        }

    }

    public void addView(View child, int index, LayoutParams params) {
        if (params != null) {
            params.width = -1;
            params.height = -1;
        }

        super.addView(child, index, params);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (this.isBaseWidth) {
            heightSize = (int)((float)widthSize * this.proportion);
            heightMode = 1073741824;
        } else {
            widthSize = (int)((float)heightSize * this.proportion);
            widthMode = 1073741824;
        }

        super.onMeasure(MeasureSpec.makeMeasureSpec(widthSize, widthMode), MeasureSpec.makeMeasureSpec(heightSize, heightMode));
    }
}

