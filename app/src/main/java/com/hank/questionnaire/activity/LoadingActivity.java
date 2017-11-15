package com.hank.questionnaire.activity;

import android.os.Bundle;

import com.hank.questionnaire.R;
import com.hank.questionnaire.base.BaseActivity;
import com.hank.questionnaire.fragment.LoadingFragment;
import com.hank.questionnaire.presenter.LoadingPresenter;
import com.hank.questionnaire.util.ActivityUtils;
import com.hank.questionnaire.util.schedulers.SchedulerProvider;

/**
 * Created by Hank on 2017/6/8.
 */

public class LoadingActivity extends BaseActivity {

    public static final String TAG = LoadingActivity.class.getSimpleName();

    private static final int DELAY_SECONDS = 3;

    private LoadingFragment mLoadingFragment;
    private LoadingPresenter mLoadingPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_view);
        findView();
        initData();
    }

    private void findView(){
        mLoadingFragment = (LoadingFragment)getSupportFragmentManager().findFragmentById(R.id.contentFrame);
    }

    private void initData(){
        if(mLoadingFragment == null){
            mLoadingFragment = LoadingFragment.newInstance();
        }
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mLoadingFragment, R.id.contentFrame);
        mLoadingPresenter = new LoadingPresenter(DELAY_SECONDS, mLoadingFragment, SchedulerProvider.getInstance());
    }

    @Override
    public boolean hasToolbar() {
        return false;
    }
}
