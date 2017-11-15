package com.hank.questionnaire.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.hank.questionnaire.data.beans.AppInfoBean;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeviceUtils {

    private static String IMEI = "", MacAddress = "", ip = "", versionName = "";

    /**
     * 获取设备Model，可以用来判断设备型号
     *
     * @return
     */
    public static String getBuildModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取CPU的核数
     *
     * @return
     */
    public static int getProcessorNumber() {
        Runtime rt = Runtime.getRuntime();
        return rt.availableProcessors();
    }

    /**
     * 获取系统型号，eg：android_OS_4.1
     *
     * @return
     */
    public static String getVersionRelease() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取SDK版本号
     *
     * @return
     */
    public static int getSdkInt() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 获取imei号，如果可以获取到，直接返回，如果无法获取，或是获取到的不是imei，则返回""
     * <ul>
     * <strong>Attentions:</strong>
     * <li>You should add <strong>android.permission.READ_PHONE_STATE</strong>
     * in manifest</li>
     * </ul>
     *
     * @param context
     * @return
     */
    public static String getImei(Context context) {
        if (!TextUtils.isEmpty(IMEI)) {
            return IMEI;
        }
        if(context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED){
            return  "";
        }
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        IMEI = tm.getDeviceId();

        // 如果不是imei号，则返回空
        if (!isImei(IMEI)) {
            IMEI = "";
            return "";
        }
        return IMEI;
    }

    /**
     * 判断是否是imei号
     *
     * @param imei
     * @return
     */
    private static boolean isImei(String imei) {
        if (TextUtils.isEmpty(imei)) {
            return false;
        }

        // 检查是否都是数字且在15个及以上
        // 使用正则表达式, 更直观
        Pattern pattern = Pattern.compile("[\\d]{15,}");
        Matcher matcher = pattern.matcher(imei);
        if (matcher.matches()) {
            return true;
        }

        return false;
    }

    /**
     * 获取mac地址
     * <ul>
     * <strong>Attentions:</strong>
     * <li>You should add <strong>android.permission.ACCESS_WIFI_STATE</strong>
     * in manifest</li>
     * </ul>
     *
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {
        if (!TextUtils.isEmpty(MacAddress)) {
            return MacAddress;
        }
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        MacAddress = info.getMacAddress();
        return MacAddress;
    }

    /**
     * 获取手机制造商信息
     *
     * @return
     */
    public static String getManufacturer() {
        return android.os.Build.MANUFACTURER;
    }

    public static String getHostIp() {
        if(!TextUtils.isEmpty(ip)){
            return ip;
        }
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> ipAddr = intf.getInetAddresses(); ipAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = ipAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        ip = inetAddress.getHostAddress().toString();
                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 获取APP当前版本号
     *
     * @return
     */
    public static String getVersionName(Context context) {
        if(!TextUtils.isEmpty(versionName)){
            return  versionName;
        }
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
        }
        return versionName;
    }

    public static List<AppInfoBean> getApplist(Context context) {
        ArrayList<AppInfoBean> appList = new ArrayList<AppInfoBean>(); //用来存储获取的应用信息数据
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);

        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            AppInfoBean tmpInfo = new AppInfoBean();
            tmpInfo.appName = packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();
            tmpInfo.packageName = packageInfo.packageName;
            tmpInfo.versionName = packageInfo.versionName;
            tmpInfo.lastUpdateTime = packageInfo.lastUpdateTime;
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                appList.add(tmpInfo);
            }
        }
        return appList;
    }

}