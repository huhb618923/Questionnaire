package com.hank.questionnaire.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.hank.questionnaire.R;


/**
 * Created by huhb on 16-5-16.
 */
public class ClearEditText extends AppCompatEditText {

    public static final int MINLENGTH = 16;
    public static final int CARD_MAXLENGTH = 19;
    public static final int SEPARATENUM = 4;

    public static final int PHONE_MAXLENGTH = 11;
    public static final int ID_MAXLENGTH = 18;

    public static final String INPUT_LIMIT = "1234567890";
    public static final String ID_INPUT_LIMIT = "0123456789x";


    public static final int PHONE_MODE =0;
    public static final int ID_MODE = 1;
    public static final int CARD_MODE = 3;

    private Drawable clearDrawable;

    private int mode;

    private CardEditTextChangerListener listener;

    private TextWatcher mTextWatcher = new TextWatcher() {
        String oldtext;
        int selectedIndex;
        int addOrDel;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            oldtext = s.toString();
            selectedIndex = start;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            setClearIconVisible(s.length()>0);
            if (s.toString().length() > oldtext.length()) {// add
                addOrDel = 1;
            } else {// dele
                addOrDel = 0;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            removeTextChangedListener(mTextWatcher);
            String formateText = mode == CARD_MODE ? formatCardno(s.toString()) : formatePhoneNo(s.toString());
            setText(formateText);
            if (formateText.length() != oldtext.length()) {
                selectedIndex += addOrDel;
            }
            if (formateText.length() != 0) {
                if(mode == CARD_MODE){
                    if (selectedIndex % 5 == 0 && addOrDel == 1) {
                        selectedIndex += 1;
                    } else if (selectedIndex % 5 == 0 && addOrDel == 0) {
                        selectedIndex -= 1;
                    }
                }
                setSelection(selectedIndex >= 0 ? selectedIndex : 0);
            }
            addTextChangedListener(mTextWatcher);
            if(listener != null){
                listener.onTextChange(s.toString());
            }
        }
    };

    public ClearEditText(Context context) {
        this(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle); // Attention here !
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mode = 0;//默认是手机号
        init(mode);
    }

    private void init(int mode) {
        InputFilter.LengthFilter lengthFilter = null;
        String inputLimit = null;
        switch (mode){
            case PHONE_MODE:
               lengthFilter = new InputFilter.LengthFilter(PHONE_MAXLENGTH);
                inputLimit = INPUT_LIMIT;
                break;
            case ID_MODE:
                lengthFilter =new InputFilter.LengthFilter(ID_MAXLENGTH);
                inputLimit = ID_INPUT_LIMIT;
                break;
            case CARD_MODE:
                lengthFilter =new InputFilter.LengthFilter(CARD_MAXLENGTH + CARD_MAXLENGTH / SEPARATENUM);
                inputLimit = INPUT_LIMIT;
                break;
        }
        this.setFilters(new InputFilter[]{lengthFilter});
        this.setKeyListener(DigitsKeyListener.getInstance(inputLimit));
        this.setInputType(InputType.TYPE_CLASS_PHONE);
        this.setSingleLine(true);

        this.addTextChangedListener(mTextWatcher);
        clearDrawable = getCompoundDrawablesRelative()[2];
        if(clearDrawable == null){
            clearDrawable = getContext().getDrawable(R.drawable.ic_clear);
        }
    }

    /**
     * 设置控件模式
     * @param mode 0 手机号；1 身份证；2 银行卡;
     */
    public void setMode(int mode){
        this.mode = mode;
        init(mode);
    }

    public void setValueChangeListener(CardEditTextChangerListener listener){
        this.listener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (clearDrawable != null) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                boolean touchable = event.getX() > (getWidth() - getPaddingRight() - clearDrawable.getIntrinsicWidth()) && (event.getX() < ((getWidth() - getPaddingRight())));
                if (touchable) {
                    this.setText("");
                    event.setAction(MotionEvent.ACTION_CANCEL);
                }
            }
        }
        return super.onTouchEvent(event);
    }

    protected void setClearIconVisible(boolean visible) {
        Drawable right = visible ? clearDrawable : null;
        setCompoundDrawablesRelativeWithIntrinsicBounds(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    public String getInput() {
        String cardNum = this.getText().toString();
        if (TextUtils.isEmpty(cardNum)) {
            return null;
        }
        return cardNum.replace(" ", "");
    }

    private String formatCardno(String cardsrc) {
        if (TextUtils.isEmpty(cardsrc)) {
            return cardsrc;
        }
        String src = cardsrc.replace(" ", "");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < src.length(); i++) {
            if (i % 4 == 0 && i != 0) {
                sb.append(" ");
            }
            sb.append(src.charAt(i));
        }
        return sb.toString();
    }

    private String formatePhoneNo(String nums){
        if (TextUtils.isEmpty(nums)) {
            return nums;
        }
        return  nums.replace(" ", "");
    }

    public static interface  CardEditTextChangerListener{
         void onTextChange(String s);
    }

}

