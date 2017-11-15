package com.hank.questionnaire.fragment;

import java.util.List;

import com.hank.questionnaire.base.BaseFragment;

/**
 * Created by facebank on 2017/6/15.
 */

public class MineFragment extends BaseFragment{

    public static MineFragment newInstance(){
        return new MineFragment();
    }

    @Override
    public void onGranted() {

    }

    @Override
    public void onDenied(List<String> deniedPermission) {

    }
}
