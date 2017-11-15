package com.hank.questionnaire.base;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import com.hank.questionnaire.data.Constants;
import com.hank.questionnaire.util.ProgressDialogUtil;
import com.hank.questionnaire.util.SharedPreferencesHelper;

import butterknife.ButterKnife;

/**
 * Created by facebank on 2017/6/8.
 */

public abstract class BaseFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
    }

    /**
     * 显示等待框
     */
    public void showProgress(){
        showProgress(null);
    }

    /**
     * 显示等待框
     * @param msg 等待框内容
     */
    public void showProgress(String msg){
        ProgressDialogUtil.openProgressDialog(getContext(),msg,false,false);
    }

    /**
     * 隐藏等待框
     */
    public void dismissProgress(){
        ProgressDialogUtil.closeProgressDialog();
    }

    public int getColor(int resourceId){
        return getActivity().getColor(resourceId);
    }

    public Drawable getDrawable(int resourceId){
        return getActivity().getDrawable(resourceId);
    }

    public void gotoActivity(Activity activity){
        Intent intent = new Intent();
        intent.setClass(getActivity(),activity.getClass());
        gotoActivity(intent);
    }

    public void gotoActivity(Intent intent){
        startActivity(intent);
    }

    public boolean hasPermission(String ...permissions){
        if(null == permissions || permissions.length <=0){
            return false;
        }
        for (String permission: permissions){
            if(ContextCompat.checkSelfPermission(getContext(),permission) == PackageManager.PERMISSION_DENIED)
                return false;
        }
        return false;
    }

    public void checkPermission(String ...permissions){
        List<String> permissonList = new ArrayList<String>();
        for(String permission:permissions)
            permissonList.add(permission);
        checkPermission(permissonList);
    }

    public void checkPermission(List<String> permissions) {
        if(permissions == null || permissions.size()<=0){
            onGranted();
            return;
        }
        List<String> deniedPermission = new ArrayList<>();
        for (String permission:permissions) {
            if(ContextCompat.checkSelfPermission(getContext(),permission) ==PackageManager.PERMISSION_DENIED){
                deniedPermission.add(permission);
            }
        }
        if(deniedPermission.size()>0){
            requestPermissions(deniedPermission.toArray(new String[deniedPermission.size()]), Constants.REQUEST_PERMISSION.REQUEST_CODE);
        }else{
            onGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0){
            // 创建一个deniedPermission 集合，用来封装未被授权成功的权限
            List<String> deniedPermission = new ArrayList<>();
            // 遍历授权结果获取每个申请的权限以及每个权限的申请结果值
            for (int i = 0; i < grantResults.length; i++) {
                // 获取每个权限的申请结果值
                int grantResult = grantResults[i];
                // 获取申请的每个权限
                String permission = permissions[i];
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    // 如果没有授权则加入deniedPermission集合
                    deniedPermission.add(permission);
                }
            }
            // deniedPermission集合为空，则说明所有权限均被授权
            if (deniedPermission.isEmpty()) {
                // 全部授权，交由子类实现下一步操作逻辑
                onGranted();
            } else {
                // 某个权限未被授权，交由子类实现下一步操作逻辑
                onDenied(deniedPermission);
            }
        }
    }

       public abstract void onGranted();
        public abstract void onDenied(List<String> deniedPermission);

}
