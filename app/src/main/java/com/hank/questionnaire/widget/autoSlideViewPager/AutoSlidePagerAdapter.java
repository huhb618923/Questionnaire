package com.hank.questionnaire.widget.autoSlideViewPager;

import android.support.v4.view.PagerAdapter;

/**
 * Created by facebank on 2017/6/16.
 */

public abstract class AutoSlidePagerAdapter extends PagerAdapter {

    public abstract int getRealCount();

    public abstract int getItemIndexForPosition(int position);
}
