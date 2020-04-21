package com.ojk.retrofitmvp.utils;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * @author azheng
 * @date 2018/5/24.
 * GitHub：https://github.com/RookieExaminer
 * Email：wei.azheng@foxmail.com
 * Description：圆形进度条Dialog
 */
public class ProgressTonggDialog {

    private static volatile ProgressTonggDialog instance;

    private ProgressTonggDialog() {
    }

    public static ProgressTonggDialog getInstance() {
        if (instance == null) {
            synchronized (ProgressTonggDialog.class) {
                if (instance == null) {
                    instance = new ProgressTonggDialog();
                }
            }
        }
        return instance;
    }

    private MaterialDialog materialDialog;

    public void show(Context mContext) {
        if (materialDialog!=null&&materialDialog.isShowing()){
            return;
        }
        materialDialog = new MaterialDialog.Builder(mContext)
//                .title(R.string.progress_dialog_title)
                .content("loading...")
                .progress(true, 0)
                .progressIndeterminateStyle(false)
                .show();

    }

    public void dismiss() {
        if (materialDialog!=null&&materialDialog.isShowing()){
            materialDialog.dismiss();
        }
    }
}
