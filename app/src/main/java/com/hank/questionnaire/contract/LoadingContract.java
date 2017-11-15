package com.hank.questionnaire.contract;

import com.hank.questionnaire.base.BasePresenter;
import com.hank.questionnaire.base.BaseView;

/**
 * Created by facebank on 2017/6/8.
 */

public interface LoadingContract {

    interface View extends BaseView<Presenter>{
        /**
         * 跳转到首页
         */
        void showHomePage();
    }

    interface Presenter extends BasePresenter{
        /**
         * Loading 延时处理
         */
        void delayPage();
    }
}
