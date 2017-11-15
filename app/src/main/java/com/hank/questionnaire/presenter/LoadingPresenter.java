package com.hank.questionnaire.presenter;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import com.hank.questionnaire.contract.LoadingContract;
import com.hank.questionnaire.util.schedulers.BaseSchedulerProvider;

/**
 * Created by facebank on 2017/6/8.
 */

public class LoadingPresenter implements LoadingContract.Presenter {

    private final LoadingContract.View mLoadingView;

    private final BaseSchedulerProvider mSchedulerProvider;

    private CompositeDisposable mSubscriptions;

    private int delaySeconds;

    public LoadingPresenter(int delaySeconds, LoadingContract.View mLoadingView, BaseSchedulerProvider schedulerProvider) {
        this.delaySeconds = delaySeconds;
        this.mLoadingView = mLoadingView;
        mSchedulerProvider = schedulerProvider;

        mSubscriptions = new CompositeDisposable();
        mLoadingView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        delayPage();
    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void delayPage() {
           mSubscriptions.add(Observable.interval(1, TimeUnit.SECONDS)
                .take(delaySeconds)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long integer) {
                        return delaySeconds - integer;
                    }
                })
                .subscribeOn(mSchedulerProvider.io())
                .observeOn(mSchedulerProvider.ui())
                .subscribeWith(new DisposableObserver<Long>() {
                    @Override
                    public void onNext(@NonNull Long aLong) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        mLoadingView.showHomePage();
                    }
                }));
    }
}
