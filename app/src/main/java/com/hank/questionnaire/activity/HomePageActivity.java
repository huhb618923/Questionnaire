package com.hank.questionnaire.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hank.questionnaire.R;
import com.hank.questionnaire.base.BaseActivity;
import com.hank.questionnaire.fragment.HomeFragment;
import com.hank.questionnaire.fragment.MineFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hank on 2017/6/8.
 */

public class HomePageActivity extends BaseActivity {

    public static final String TAG = HomePageActivity.class.getSimpleName();

    public static final String TAG_HOME = "home";
    public static final String TAG_MINE = "mine";

    private Context mContext;
    FragmentTabHost mTabHost;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        mContext = this;
        initData();
    }

    private void initData(){
        initTabHost();
    }

    private void initTabHost(){
        mTabHost = findViewById(android.R.id.tabhost);
        mTabHost.setup(mContext, getSupportFragmentManager(), R.id.realtabcontent);
        mTabHost.getTabWidget().setDividerDrawable(null);
        mTabHost.addTab(mTabHost.newTabSpec(TAG_HOME).setIndicator(getTabItemView(R.drawable.tab_home_bg, R.string.tab_home_title)), HomeFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(TAG_MINE).setIndicator(getTabItemView(R.drawable.tab_mine_bg, R.string.tab_mine_title)), MineFragment.class, null);
        mTabHost.setCurrentTabByTag(TAG_HOME);
    }

    private View getTabItemView(int drwId, int titleId) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_tab_view, null);
        ImageView imageView = view.findViewById(R.id.img_tab_icon);
        imageView.setImageResource(drwId);
        TextView textView = view.findViewById(R.id.tv_tab_title);
        textView.setText(getResources().getText(titleId));
        return view;
    }

    @Override
    public boolean hasToolbar() {
        return false;
    }

}
