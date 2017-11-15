package com.hank.questionnaire.contract;

import com.hank.questionnaire.base.BasePresenter;
import com.hank.questionnaire.base.BaseView;

/**
 * Created by facebank on 2017/6/14.
 */

public interface HomeContract {

    interface View extends BaseView<Presenter> {
    }

    interface Presenter extends BasePresenter {
    }
}
