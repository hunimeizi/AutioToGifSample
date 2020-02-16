
package come.haolin_android.mvp.baselibrary.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import come.haolin_android.mvp.baselibrary.R;
import come.haolin_android.mvp.baselibrary.utils.DownloadKey;

public class ToolAlertDialog {
    private Context ctx;

    public ToolAlertDialog(Context context) {
        ctx = context;
    }

    /**
     * Return a boolean value indicates whether the AlertDialog is showing or
     * not.
     *
     * @return true, if the AlertDialog is showing. <br>
     * false, otherwise.
     */
    public boolean isAlertDialogShowing() {
        return (null != alertDialog && alertDialog.isShowing());
    }

    /**
     * Dismiss this dialog, removing it from the screen.
     */
    public void dismissAlertDialog() {

        if (null == alertDialog) {
            return;
        }

        if (isAlertDialogShowing()) {
            alertDialog.hide();
        }

        alertDialog.dismiss();
        alertDialog = null;
        window = null;
    }

    public void dismissDialog() {
        if (null == dialog) {
            return;
        }

        if (dialog.isShowing()) {
            dialog.hide();
        }

        dialog.dismiss();
        dialog = null;
        window = null;
    }

    public void showAlertDialog(String tipTitle, String messageText, String confirmText, View.OnClickListener confirmClick, String cancelText, View.OnClickListener cancelOnClick, boolean cancelable) {
        dismissAlertDialog();
        try {
            alertDialog = new AlertDialog.Builder(ctx, R.style.ActionSheetDialogStyle).create();
        } catch (Exception e) {
        }
        if (alertDialog == null) {
            return;
        }
        alertDialog.show();
        window = alertDialog.getWindow();
        window.setContentView(R.layout.view_dialog_001_alert_base);
        window.setBackgroundDrawable(null);
        window.setBackgroundDrawableResource(R.color.transparent);
        setWindowWidth((int) (getWindowWidth() * 0.9));
        setWindowHeight();
        window.setGravity(Gravity.BOTTOM);
        setAlertText(R.id.tv_dialog001_tip, tipTitle);
        setAlertText(R.id.base_dialog_message, messageText);
        setButtonInfo(R.id.base_dialog_confirm_btn, confirmText, confirmClick);
        setButtonInfo(R.id.base_dialog_cancel_btn, cancelText, cancelOnClick);
        alertDialog.setCancelable(cancelable);
    }

    private void setAlertText(int resId, String txtTitle) {
        if (0 == resId) {
            return;
        }
        TextView txtDialogTitle = window.findViewById(resId);
        if (TextUtils.isEmpty(txtTitle)) {
            txtDialogTitle.setVisibility(View.GONE);
        } else {
            txtDialogTitle.setText(txtTitle);
        }
    }

    public void showAlertDialog(String tipTitle, String txtMessage, String confirmText, View.OnClickListener confirmClick, boolean cancelable) {
        showAlertDialog(tipTitle, txtMessage, confirmText, confirmClick, null, null, cancelable);
    }

    private AlertDialog alertDialog;
    private Window window;

    private Dialog dialog;

    private int getWindowWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        window.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    private int getWindowHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        window.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    private void setWindowWidth(int width) {

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = width;
        window.setAttributes(lp);
    }

    private void configWindow(Window win, WindowManager.LayoutParams wAttrs) {
        wAttrs.gravity = Gravity.BOTTOM;
        wAttrs.horizontalMargin = 0F;
        // 因为现在的gravity是Gravity.LEFT和Gravity.CENTER，所以在垂直方向上是以中间为margin的参考点
        wAttrs.verticalMargin = 0.59F;
    }

    private void setWindowHeight() {
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.verticalMargin = 0.04f;
        window.setAttributes(lp);
    }

    private void setTextViewInfo(int resID, View.OnClickListener phoneClickListener) {
        if (0 == resID) {
            return;
        }
        TextView btnDialog = (TextView) window.findViewById(resID);

        if (null == phoneClickListener) {
            btnDialog.setOnClickListener(defaultCancelListener);
        } else {
            btnDialog.setOnClickListener(phoneClickListener);
        }
    }

    private void setButtonInfo(int resID, String txtButton, View.OnClickListener clickListener) {
        if (0 == resID) {
            return;
        }
        Button btnDialog = (Button) window.findViewById(resID);
        btnDialog.setText(txtButton);
        if (TextUtils.isEmpty(txtButton)) {
            btnDialog.setVisibility(View.GONE);
        }
        btnDialog.setOnClickListener(clickListener);
    }

    public Window getWindow() {
        return window;
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener dismissListener) {
        if (null == dismissListener) {
            return;
        }
        if (dialog != null) {
            dialog.setOnDismissListener(dismissListener);
        } else if (alertDialog != null) {
            alertDialog.setOnDismissListener(dismissListener);
        }
    }

    public void OnCancelListener(DialogInterface.OnCancelListener cancelListener) {
        if (null == cancelListener) {
            return;
        }
        if (dialog != null) {
            dialog.setOnCancelListener(cancelListener);
        } else if (alertDialog != null) {
            alertDialog.setOnCancelListener(cancelListener);
        }
    }

    /**
     * button传入事件为空时 设置默认
     */
    View.OnClickListener defaultCancelListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            dismissAlertDialog();
            dismissDialog();
        }
    };

    public void showAlertDialogUpApp(String confirmText, View.OnClickListener confirmClick, String cancelText, View.OnClickListener cancelOnClick, boolean cancelable) {
        dismissAlertDialog();
        try {
            alertDialog = new AlertDialog.Builder(ctx).create();
        } catch (Exception e) {
        }
        if (alertDialog == null) {
            return;
        }
        alertDialog.show();
        window = alertDialog.getWindow();
        window.setContentView(R.layout.view_dialog_002_alert_base);
        window.setBackgroundDrawableResource(R.drawable.dialog_upapp_shap);
        setWindowWidth((int) (getWindowWidth() * 0.9));
        setAlertText(R.id.title, "发现新版本：" + DownloadKey.version);
        setFitText();
        setButtonInfo(R.id.base_dialog_confirm_btn, confirmText, confirmClick);
        setButtonInfo(R.id.base_dialog_cancel_btn, cancelText, cancelOnClick);
        alertDialog.setCancelable(cancelable);
    }

    private void setFitText() {
        FitTextView txtDialogTitle = window.findViewById(R.id.updatedialog_text_changelog);
        if (!TextUtils.isEmpty(DownloadKey.changeLog)) {
            String upLog;
            upLog = DownloadKey.changeLog.contains(";") ? DownloadKey.changeLog.replace(";", "\n") : DownloadKey.changeLog;
            upLog = upLog.contains("；") ? upLog.replace("；", "\n") : upLog;
            txtDialogTitle.setText(upLog);
        }
    }

    public ProgressBar progressBar;
    public TextView downloaddialog_count;

    public void showAlertDialogDownApp(String commitText, View.OnClickListener commitOnClick, String cancelText, View.OnClickListener cancelOnClick, boolean cancelable) {
        dismissAlertDialog();
        try {
            alertDialog = new AlertDialog.Builder(ctx).create();
        } catch (Exception e) {
        }
        if (alertDialog == null) {
            return;
        }
        alertDialog.show();
        window = alertDialog.getWindow();
        window.setContentView(R.layout.view_dialog_003_alert_base);
        window.setBackgroundDrawableResource(R.drawable.dialog_shap);
        setWindowWidth((int) (getWindowWidth() * 0.9));
        progressBar = window.findViewById(R.id.downloaddialog_progress);
        downloaddialog_count = window.findViewById(R.id.downloaddialog_count);
        setButtonInfo(R.id.base_dialog_cancel_btn, cancelText, cancelOnClick);
        setButtonInfo(R.id.base_dialog_commit_btn, commitText, commitOnClick);
        alertDialog.setCancelable(cancelable);
    }


    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public TextView getTextView() {
        return downloaddialog_count;
    }

}
