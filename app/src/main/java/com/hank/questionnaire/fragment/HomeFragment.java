package com.hank.questionnaire.fragment;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hank.questionnaire.R;
import com.hank.questionnaire.adapter.HomeAdapter;
import com.hank.questionnaire.base.BaseFragment;
import com.hank.questionnaire.contract.HomeContract;
import com.hank.questionnaire.presenter.HomePresenter;
import com.hank.questionnaire.util.LogHelper;
import com.hank.questionnaire.util.schedulers.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class HomeFragment extends BaseFragment implements HomeContract.View, HomeAdapter.OnHomeFragmentClickListener {

    public static final String TAG = HomeFragment.class.getSimpleName();

    private HomeContract.Presenter mPresenter;

    private Activity mContext;

    private View rootView;
    @BindView(R.id.recycle_view) RecyclerView mRecycleView;
    private LinearLayoutManager mLayoutManager;
    private HomeAdapter homeAdapter;

    private void initPresenter() {
        new HomePresenter(this, SchedulerProvider.getInstance());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();
        initPresenter();
        initRecycleView();
        initData();
    }

    private void initRecycleView() {
        mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecycleView.setLayoutManager(mLayoutManager);
        homeAdapter = new HomeAdapter(mContext);
        homeAdapter.setOnHomeFragmentClickListener(this);
        mRecycleView.setAdapter(homeAdapter);
    }

    private void initData() {
        checkPermission(Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onViewClickListener(View v) {

    }

    @Override
    public void onGranted() {
        LogHelper.i("onGranted");
    }

    @Override
    public void onDenied(List<String> deniedPermission) {
        LogHelper.i("onDenied");
        List<String> permissons = new ArrayList<String>();
        if (deniedPermission != null && deniedPermission.size() > 0) {
            for (String permission : deniedPermission) {
                if (shouldShowRequestPermissionRationale(permission)) {
                    permissons.add(permission);
                }
            }
            if (permissons.size() > 0) {
                checkPermission(deniedPermission);
            }
        }
    }

}
