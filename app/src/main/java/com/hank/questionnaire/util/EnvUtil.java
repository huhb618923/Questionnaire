package com.hank.questionnaire.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import static android.content.Context.CONTEXT_IGNORE_SECURITY;

/**
 * Created by facebank on 2017/6/26.
 */

public class EnvUtil {

    private static EnvUtil instance;

    private boolean isOudingTest;


    /**
     * 线程安全的单例模式
     *
     * @return EnvUtil
     */
    public static EnvUtil getInstance() {
        if (null == instance) {
            synchronized (EnvUtil.class) {
                if (instance == null) {
                    instance = new EnvUtil();
                }
            }
        }
        return instance;
    }

    public boolean isOudingTest() {
        return isOudingTest;
    }

    public void setOudingTest(boolean isOudingTest){
        this.isOudingTest = isOudingTest;
    }

    public void initEnv(Context context){
        Context targetAPPContext = null;
        try {
            targetAPPContext = context.createPackageContext("los.com.envsetting", CONTEXT_IGNORE_SECURITY);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (targetAPPContext != null) {
            SharedPreferences mPreferences = targetAPPContext.getSharedPreferences("ouding_dld", Context.MODE_PRIVATE);
            if (mPreferences != null) {
                 isOudingTest = mPreferences.getBoolean("dld", false);
                LogHelper.i("当前环境 "+ isOudingTest);
            }
        }
    }

    public String getDevelopUrl(Context context){
        Context targetAPPContext = null;
        try {
            targetAPPContext = context.createPackageContext("los.com.envsetting", CONTEXT_IGNORE_SECURITY);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (targetAPPContext != null) {
            SharedPreferences mPreferences = targetAPPContext.getSharedPreferences("ouding_dld", Context.MODE_PRIVATE);
            if (mPreferences != null) {
                return mPreferences.getString("develop_url", null);
            }
        }
        return null;
    }
}
