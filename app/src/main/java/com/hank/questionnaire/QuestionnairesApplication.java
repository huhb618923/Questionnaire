package com.hank.questionnaire;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.hank.questionnaire.util.EnvUtil;

/**
 * Created by facebank on 2017/6/8.
 */

public class QuestionnairesApplication extends Application {

    public static QuestionnairesApplication instance;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        instance = this;
    }

    public static QuestionnairesApplication getApplication() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initData();
    }

    private void initData() {
        EnvUtil.getInstance().initEnv(getApplicationContext());
    }

    /**
     * 获取application中指定的meta-data
     */
    public String getAppMetaData(String key) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = instance.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(instance.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return resultData;
    }
}
