package com.hank.questionnaire.widget;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;

import com.hank.questionnaire.R;

/**
 * Created by facebank on 2017/6/29.
 */

public class VerifyCodeView extends AppCompatTextView implements View.OnClickListener {

    public static final String TAG = VerifyCodeView.class.getSimpleName();

    public VerifyCodeView instance;
    public Context mContext;

    public static long millisInFuture = 60000;//倒计时时长60s
    public static long countDownInterval = 1000;//倒计时间隔1s

    private TimeCount timeCount;//计时器
    private OnVerifyCodeViewClickListener listener;

    public VerifyCodeView(Context context) {
        this(context,null);
    }

    public VerifyCodeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public VerifyCodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setClickListener(OnVerifyCodeViewClickListener listener) {
        this.listener = listener;
    }

    public void startCountDown() {
        if (timeCount != null) {
            timeCount.start();
        }
    }

    public void stopCountDown() {
        if (timeCount != null) {
            timeCount.onFinish();
            timeCount.cancel();
        }
    }

    private void init() {
        instance = this;
        mContext = getContext();
        timeCount = new TimeCount(millisInFuture, countDownInterval);
        instance.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
        /*if (!timeCount.isCounting){
           startCountDown();
        }*/
    }

    private class TimeCount extends CountDownTimer {

        public boolean isCounting;

        /**
         * @param millisInFuture    总时长
         * @param countDownInterval 计时时间间隔
         */
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }


        // 计时完毕时触发
        @Override
        public void onFinish() {
            isCounting = false;
            instance.setText(R.string.send_code);
            instance.setEnabled(true);
        }

        // 计时，过程显示
        @Override
        public void onTick(long millisUntilFinished) {
            isCounting = true;
            instance.setEnabled(false);
            instance.setText(mContext.getString(R.string.resend_code) + " " + millisUntilFinished / 1000);
        }
    }

    public interface OnVerifyCodeViewClickListener {
        void onClick(View v);
    }


}
