package com.hank.questionnaire.data.beans;

/**
 * Created by facebank on 2017/7/5.
 */

public class AppInfoBean extends BaseBean {

    public String appName;
    public String packageName;
    public String versionName;
    public long lastUpdateTime;

    public String getAppName() {
        return appName;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

}
