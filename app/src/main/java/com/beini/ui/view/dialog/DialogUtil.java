package com.beini.ui.view.dialog;

import android.content.Context;

import com.beini.app.GlobalApplication;

/**
 * Created by beini on 2016/12/27.
 */

public class DialogUtil {
    private static UIDialog uiDialog;

    private volatile static DialogUtil instance; //声明成 volatile

    private DialogUtil() {
    }

    public static DialogUtil getSingleton() {
        if (instance == null) {
            synchronized (DialogUtil.class) {
                if (instance == null) {
                    instance = new DialogUtil();
                }
            }
        }
        return instance;
    }


    public static UIDialog showDailog(Context context, String msg) {

        uiDialog = new UIDialog(context).builder()
                .setCanceledOnTouchOutside(true)
                .setMsg(msg);

        return uiDialog;
    }

    public static UIDialog showDailog(Context context) {

        uiDialog = new UIDialog(context).builder()
                .setCanceledOnTouchOutside(true);

        return uiDialog;
    }

    public static UIDialog uploadDailog(String msg) {
        final UIDialog uploadDialog = new UIDialog(GlobalApplication.getInstance()).builder();
        uploadDialog.setLoading(msg).setCanceledOnTouchOutside(false).setCancelable(false);
        return uploadDialog;
    }
}
