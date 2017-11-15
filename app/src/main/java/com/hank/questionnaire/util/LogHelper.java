package com.hank.questionnaire.util;


import android.text.TextUtils;
import android.util.Log;


public class LogHelper {

    private static boolean isShow = true;
    public static void i(String msg) {
        i(null, msg);
    }

    public static void i(String tag, String msg) {
        if (isShow) {
            try {
                Log.i(TextUtils.isEmpty(tag) ? "Ouding" : tag, msg);
             /*   LogWriter writer = LogWriter.open();
                writer.print(msg);
                writer.close();*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
