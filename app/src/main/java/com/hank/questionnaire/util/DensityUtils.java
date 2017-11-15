package com.hank.questionnaire.util;

import android.app.Application;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.hank.questionnaire.QuestionnairesApplication;

/**
 * Created by facebank on 2017/6/16.
 */

public class DensityUtils {

    public static float dip2px(float dip) {
        DisplayMetrics metrics = QuestionnairesApplication.getApplication().getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, metrics);
    }

    public static float px2dip(float px) {
        DisplayMetrics metrics = QuestionnairesApplication.getApplication().getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, metrics);
    }

    public static int getScreenWidth() {
        Application application = QuestionnairesApplication.getApplication();
        int screenWidth = -1;
        if (application != null) {
            screenWidth = application.getResources().getDisplayMetrics().widthPixels;
        }
        return screenWidth;
    }

    public static int getScreenHeight() {
        Application application = QuestionnairesApplication.getApplication();
        int screenHeight = -1;
        if (application != null) {
            screenHeight = application.getResources().getDisplayMetrics().heightPixels;
        }
        return screenHeight;
    }
}
