package com.hank.questionnaire.presenter;

import com.hank.questionnaire.contract.HomeContract;
import com.hank.questionnaire.util.schedulers.BaseSchedulerProvider;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by facebank on 2017/6/14.
 */

public class HomePresenter implements HomeContract.Presenter {
    public static final String TAG = HomePresenter.class.getSimpleName();

    private HomeContract.View mHomeView;

    private BaseSchedulerProvider mShedulerProvider;

    private CompositeDisposable mCompositeDisposable;

    public HomePresenter(HomeContract.View view, BaseSchedulerProvider schedulerProvider) {
        this.mHomeView = view;
        this.mShedulerProvider = schedulerProvider;

        mCompositeDisposable = new CompositeDisposable();
        mHomeView.setPresenter(this);
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }

}
