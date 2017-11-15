package com.hank.questionnaire.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by facebank on 16-6-17.
 */
public class ToastHelper {

    public static Toast toast;

    public static void showToast(Context context, String msg){
        if(TextUtils.isEmpty(msg)){
            return;
        }
        showToast(context,msg, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, int id){
        if(id == 0){
            return;
        }
        showToast(context,id, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, String msg, int durationn){
        if(TextUtils.isEmpty(msg)){
            return;
        }
        if(toast == null){
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        }else{
            toast.setText(msg);
        }
        toast.show();
    }

    public static void showToast(Context context, int id, int durationn){
        if(id == 0){
            return;
        }
        if(toast == null){
            toast = Toast.makeText(context, id, Toast.LENGTH_SHORT);
        }else{
            toast.setText(id);
        }
        toast.show();
    }

}
