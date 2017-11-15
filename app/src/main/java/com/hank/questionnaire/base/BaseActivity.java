package com.hank.questionnaire.base;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hank.questionnaire.R;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;

import butterknife.ButterKnife;

/**
 * Created by Hank on 2017/6/8.
 */

public class BaseActivity extends AppCompatActivity implements BaseRefreshListener {

    private ViewGroup mRootView;
    private Toolbar mToolbar;
    private View mCustomView;

    private String rightTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        mCustomView = LayoutInflater.from(this).inflate(layoutResID, null);
        setContentView(mCustomView);
    }

    @Override
    public void setContentView(View view) {
        setContentView(view, null);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (!hasToolbar()) {
            super.setContentView(view);
        } else {
            if (isRefresh()) {
                super.setContentView(R.layout.base_activity_refresh_layout);
                mRootView = (PullToRefreshLayout) findViewById(R.id.base_root);
                ((PullToRefreshLayout) mRootView).setCanRefresh(isShowHeader());
                ((PullToRefreshLayout) mRootView).setCanLoadMore(isShowFooter());
                ((PullToRefreshLayout) mRootView).setRefreshListener(this);
            } else {
                super.setContentView(R.layout.base_activity_layout);
                mRootView = (LinearLayout) findViewById(R.id.base_root);
            }
            ButterKnife.bind(this);
            mToolbar = findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
            mCustomView = view;
            addCustomView(mRootView, mCustomView, params);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void addCustomView(ViewGroup parent, View customView, ViewGroup.LayoutParams params) {
        if (parent == null || customView == null) {
            return;
        }
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        parent.addView(customView, params);
    }

    public boolean isRefresh() {
        return false;
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    /**
     * 是否支持刷新页面
     */
    public boolean isShowHeader() {
        return true;
    }

    /**
     * 是否支持加载更多
     */
    public boolean isShowFooter() {
        return true;
    }

    /**
     * 页面下拉刷新
     */
    public void onRefresh() {
        ((PullToRefreshLayout) mRootView).finishRefresh();
    }

    /**
     * 页面上拉加载
     */
    public void onLoadMore() {
        ((PullToRefreshLayout) mRootView).finishLoadMore();
    }


    public boolean hasToolbar() {
        return true;
    }

    public void setTitle(int resourceId) {
        setTitle(this.getString(resourceId));
    }

    public void setTitle(String title) {
        TextView titleView = new TextView(this);
        titleView.setText(title);
        titleView.setTextColor(this.getColor(R.color.white));
        titleView.setTextSize(18);
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        titleView.setLayoutParams(layoutParams);
        layoutParams.gravity = Gravity.CENTER | Gravity.CENTER_VERTICAL;
        mToolbar.addView(titleView, layoutParams);
    }

    public void setRightView(int resoutId) {
        this.rightTitle = this.getString(resoutId);
    }

    public void setLeftView(int resourceId) {
        TextView leftView = new TextView(this);
        leftView.setText(resourceId);
        leftView.setTextColor(this.getColor(R.color.white));

        leftView.setTextSize(16);
        leftView.setCompoundDrawablesRelativeWithIntrinsicBounds(this.getDrawable(R.drawable.ic_back), null, null, null);
        leftView.setCompoundDrawablePadding(0);
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        leftView.setLayoutParams(layoutParams);
        layoutParams.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        mToolbar.addView(leftView, layoutParams);
    }

    public void showLeftView() {
        mToolbar.setNavigationIcon(this.getDrawable(R.drawable.ic_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_menu, menu);
        return true;

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_right).setTitle(rightTitle);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void refresh() {
        onRefresh();
    }

    @Override
    public void loadMore() {
        onLoadMore();
    }
}
