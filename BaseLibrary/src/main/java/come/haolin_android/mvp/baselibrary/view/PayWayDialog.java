package come.haolin_android.mvp.baselibrary.view;

import android.app.Activity;
import android.app.Dialog;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import come.haolin_android.mvp.baselibrary.R;
import come.haolin_android.mvp.baselibrary.base.ConstantObj;
import come.haolin_android.mvp.baselibrary.utils.ToastUtils;


/**
 * Author: LYBo(342161360@qq.com)
 * Class: 选择支付方式dialog
 */

public class PayWayDialog {

    public void showPayDialog(final Activity context, final String money) {

        final Dialog dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        View inflate = LayoutInflater.from(context).inflate(R.layout.choos_pay_popupwindow, null);

        final CheckBox checkBox_wchat = inflate.findViewById(R.id.checkBox_wchat);
        final CheckBox alipay_check = inflate.findViewById(R.id.alipay_check);
        final CheckBox yinlian_pay =  inflate.findViewById(R.id.yinlian_pay);
        final TextView tv_pay_money = inflate.findViewById(R.id.tv_pay_money);
        Button pop_btn_jiesuan =  inflate.findViewById(R.id.pop_btn_jiesuan);
        tv_pay_money.setText(money);
        ConstantObj.PAY_WAY = 1;
        pop_btn_jiesuan.setText(context.getString(R.string.common_dialog_ok));
        checkBox_wchat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            tv_pay_money.setText(money);
            if (isChecked) {
                alipay_check.setChecked(false);
                yinlian_pay.setChecked(false);
                ConstantObj.PAY_WAY = 1;
            }else {
                ConstantObj.PAY_WAY = 0;
        }
    });
        alipay_check.setOnCheckedChangeListener((buttonView, isChecked) -> {
            tv_pay_money.setText(money);
            if (isChecked) {
                checkBox_wchat.setChecked(false);
                yinlian_pay.setChecked(false);
                ConstantObj.PAY_WAY = 2;
            } else {
                ConstantObj.PAY_WAY = 0;
            }
        });


        yinlian_pay.setOnCheckedChangeListener((buttonView, isChecked) -> {
            tv_pay_money.setText(money);
            if (isChecked) {
                alipay_check.setChecked(false);
                checkBox_wchat.setChecked(false);
                ConstantObj.PAY_WAY = 3;
            } else {
                ConstantObj.PAY_WAY = 0;
            }
        });

        pop_btn_jiesuan.setOnClickListener(v -> {
            if (!alipay_check.isChecked() && !checkBox_wchat.isChecked() && !yinlian_pay.isChecked()) {
                ToastUtils.showToast(context, context.getString(R.string.pay_me_product_payMoney_pay_style));
            } else {
                dialog.dismiss();
                if (ConstantObj.PAY_WAY != 4 && ConstantObj.PAY_WAY != 5) {
                    if (toPayListener != null) {
                        toPayListener.toPayListenerInfo(ConstantObj.PAY_WAY);
                    }
                }
            }
        });

        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.5); // 改变的是dialog框在屏幕中的位置而不是大小
        p.width = d.getWidth();
        dialogWindow.setAttributes(p);
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialog.show();//显示对话框
    }

    private OnToPayListener toPayListener;

    public interface OnToPayListener {
        void toPayListenerInfo(int payWay);
    }

    public void setToPayListener(OnToPayListener toPayListener) {
        this.toPayListener = toPayListener;
    }

}
