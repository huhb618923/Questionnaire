package com.hank.questionnaire.util;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogUtil {

    private static ProgressDialog progressDialog;

    /**
     * 不带标题进度框显示
     *
     * @param context 上下文
     * @param message 显示内容
     */
    public static void openProgressDialog(Context context, String message) {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                return;
            }
            progressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
            // 设置进度框內容
            progressDialog.setMessage(message);
            if (progressDialog != null && !progressDialog.isShowing()) {
                progressDialog.show();
            }
        } catch (Exception e) {
            LogHelper.i(e.getMessage());
        }
    }

    /**
     * 不带标题进度框显示
     *
     * @param context      上下文
     * @param message      显示内容
     * @param touchOutside 点击外部区域对话框不消失
     */
    public static void openProgressDialog(Context context, String message, boolean touchOutside) {
        try {
            progressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
            // 设置进度框內容
            progressDialog.setMessage(message);
            // 设置触摸取消对话框属性
            progressDialog.setCancelable(touchOutside);
            progressDialog.setCanceledOnTouchOutside(touchOutside);
            if (progressDialog != null && !progressDialog.isShowing()) {
                progressDialog.show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 不带标题进度框显示
     *
     * @param context      上下文
     * @param message      显示内容
     * @param touchOutside ，即进度框不会消失
     * @param canclable    返回键是否隐藏对话框
     */
    public static void openProgressDialog(Context context, String message, boolean touchOutside, boolean canclable) {
        try {
            progressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
            // 设置进度框內容
            progressDialog.setMessage(message);
            // 设置触摸取消对话框属性
            progressDialog.setCancelable(canclable);
            progressDialog.setCanceledOnTouchOutside(touchOutside);
            if (progressDialog != null && !progressDialog.isShowing()) {
                progressDialog.show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 带标题进度框显示
     *
     * @param context 上下文
     * @param title   标题
     * @param message 显示内容
     */
    public static void openProgressDialog(Context context, String title, String message) {
        try {
            progressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
            // 设置进度框标题
            progressDialog.setTitle(title);
            // 设置进度框內容
            progressDialog.setMessage(message);
            if (progressDialog != null && !progressDialog.isShowing()) {
                progressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 带标题进度框显示
     *
     * @param context      上下文
     * @param title        标题
     * @param message      显示内容
     * @param touchOutside ，即进度框不会消失
     */
    public static void openProgressDialog(Context context, String title, String message, boolean touchOutside) {
        try {
            progressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
            // 设置进度框标题
            progressDialog.setTitle(title);
            // 设置进度框內容
            progressDialog.setMessage(message);
            // 设置触摸取消对话框属性
            progressDialog.setCancelable(touchOutside);
            progressDialog.setCanceledOnTouchOutside(touchOutside);
            if (progressDialog != null && !progressDialog.isShowing()) {
                progressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭进度框
     */
    public static void closeProgressDialog() {
        try {
            if (progressDialog != null) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
