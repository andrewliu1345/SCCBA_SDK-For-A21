/*    */ package com.joesmate.sdk.util;
/*    */ 
/*    */ import android.app.ProgressDialog;
/*    */ import android.content.Context;
/*    */ import android.content.DialogInterface.OnDismissListener;
/*    */ import android.os.Handler;
/*    */ 
/*    */ 

public class SamplesUtils {
    public SamplesUtils() {
    }

    public static void indeterminate(Context context, Handler handler, String message, Runnable runnable, OnDismissListener dismissListener) {
        try {
            indeterminateInternal(context, handler, message, runnable, dismissListener, true);
        } catch (Exception var6) {
            ;
        }

    }

    public static void indeterminate(Context context, Handler handler, String message, Runnable runnable, OnDismissListener dismissListener, boolean cancelable) {
        try {
            indeterminateInternal(context, handler, message, runnable, dismissListener, cancelable);
        } catch (Exception var7) {
            ;
        }

    }

    private static ProgressDialog createProgressDialog(Context context, String message) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setIndeterminate(false);
        dialog.setMessage(message);
        return dialog;
    }

    private static void indeterminateInternal(Context context, final Handler handler, String message, final Runnable runnable, OnDismissListener dismissListener, boolean cancelable) {
        final ProgressDialog dialog = createProgressDialog(context, message);
        dialog.setCancelable(cancelable);
        if(dismissListener != null) {
            dialog.setOnDismissListener(dismissListener);
        }

        dialog.show();
        (new Thread() {
            public void run() {
                runnable.run();
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            dialog.dismiss();
                        } catch (Exception var2) {
                            ;
                        }

                    }
                });
            }
        }).start();
    }
}