package com.hank.questionnaire.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hank.questionnaire.R;
import com.hank.questionnaire.activity.HomePageActivity;
import com.hank.questionnaire.base.BaseFragment;
import com.hank.questionnaire.contract.LoadingContract;

import java.util.List;


/**
 * Created by facebank on 2017/6/8.
 */

public class LoadingFragment extends BaseFragment implements LoadingContract.View {

    private LoadingContract.Presenter mPresenter;

    public static LoadingFragment newInstance() {
        return new LoadingFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_loading, container, false);
        return root;
    }

    @Override
    public void onGranted() {

    }

    @Override
    public void onDenied(List<String> deniedPermission) {

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
    public void setPresenter(LoadingContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showHomePage() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), HomePageActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
