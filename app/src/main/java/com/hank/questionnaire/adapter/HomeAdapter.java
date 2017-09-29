package com.hank.questionnaire.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hank.questionnaire.R;

/**
 * Created by facebank on 2017/6/16.
 */

public class HomeAdapter extends RecyclerView.Adapter {

    public static final String TAG = HomeAdapter.class.getSimpleName();

    private Context mContext;

    private OnHomeFragmentClickListener mClickListener;

    public HomeAdapter(Context context) {
        this.mContext = context;
    }

    public void setOnHomeFragmentClickListener(OnHomeFragmentClickListener listener) {
        this.mClickListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.holder_home_content, parent, false);
        RecyclerView.ViewHolder viewHolder = new ContentViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ContentViewHolder) {
            bindContentView((ContentViewHolder) holder);
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    private void bindContentView(ContentViewHolder contentViewHolder) {

    }

    private class ContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView applyConditions;
        public TextView applyProcess;

        public LinearLayout conditionView;
        public LinearLayout processView;

        public ContentViewHolder(View itemView) {
            super(itemView);
            applyConditions = (TextView) itemView.findViewById(R.id.apply_condition);
            applyProcess = (TextView) itemView.findViewById(R.id.apply_process);
            conditionView = (LinearLayout) itemView.findViewById(R.id.condition_view);
            processView = (LinearLayout) itemView.findViewById(R.id.process_view);
            applyConditions.setOnClickListener(this);
            applyProcess.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.apply_condition:
                    conditionView.setVisibility(conditionView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                    break;
                case R.id.apply_process:
                    processView.setVisibility(processView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                    break;
            }
        }
    }

    public interface OnHomeFragmentClickListener {
        void onViewClickListener(View v);
    }


}
