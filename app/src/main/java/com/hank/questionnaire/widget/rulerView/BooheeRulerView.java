package com.hank.questionnaire.widget.rulerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.widget.OverScroller;

import com.hank.questionnaire.R;

/**
 * Created by facebank on 2017/6/8.
 */

public class BooheeRulerView extends View {

    private static final String TAG = BooheeRulerView.class.getSimpleName();

    public static final int DEFAULT_LONG_LINE_HEIGHT = 50;
    public static final int DEFAULT_SELECT_LINE_HEIGHT = 80;
    public static final int DEFAULT_TEXT_SIZE = 48;
    public static final int SELECT_TEXTT_SIZE = 93;
    public static final float DEFAULT_UNIT = 500;
    public static final int DEFAULT_WIDTH_PER_UNIT = 220;
    public static final int DEFAULT_MICRO_UNIT_COUNT = 5;
    public static final float DEFAULT_LONG_LINE_STROKE_WIDTH = 3;
    public static final float DEFAULT_SHORT_LINE_STROKE_WIDTH = 3;

    private int width;
    private int height;
    /**
     * 当前值
     */
    private float currentValue;
    /**
     * 起点值
     */
    private float startValue;
    /**
     * 终点至
     */
    private float endValue;
    /**
     * 两条长线之间的间距
     */
    private float widthPerUnit;
    /**
     * 两条短线之间的距离
     */
    private float widthPerMicroUnit;
    /**
     * 单位，即每两条长线之间的数值
     */
    private float unit;
    /**
     * 短线单位
     */
    private float microUnit;
    /**
     * 每个单位之间的小单位数量，短线数量
     */
    private int microUnitCount;

    /**
     * 能够移动的最大值
     */
    private float maxRightOffset;
    private float maxLeftOffset;

    private float longLineHeight;
    private float shortLineHeight;
    private float selectLineHeight;

    private float textSize;
    private float selectTextSize;
    private int textColor;
    private int unableTextColor;
    private int lineColor;
    private int selectColor;

    private Paint linePaint;
    private Paint textPaint;
    private Paint unableTextPaint;
    private Paint selectPaint;
    private Paint brokenLinePaint;
    private PaintFlagsDrawFilter pfdf;
    private float moveX;
    private VelocityTracker velocityTracker;
    private OverScroller scroller;
    /**
     * 剩余偏移量
     */
    private float offset;
    /**
     * 最小速度
     */
    private int minvelocity;


    private float originValue;

    private OnValueChangeListener listener;

    private Context mContext;

    private float upLimitValue;
    /**
     * 最大额度对应的坐标
     */
    private float maxX;

    public interface OnValueChangeListener {
        void onValueChange(float value);
    }

    public BooheeRulerView(Context context) {
        this(context, null);
    }

    public BooheeRulerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BooheeRulerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scroller = new OverScroller(context);
        mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        longLineHeight = DEFAULT_LONG_LINE_HEIGHT;
        shortLineHeight = (DEFAULT_LONG_LINE_HEIGHT / 2);
        selectLineHeight = DEFAULT_SELECT_LINE_HEIGHT;
        textSize = DEFAULT_TEXT_SIZE;
        selectTextSize = SELECT_TEXTT_SIZE;
        widthPerUnit = DEFAULT_WIDTH_PER_UNIT;

        textColor = mContext.getColor(R.color.ruler_text_black);
        unableTextColor = mContext.getColor(R.color.ruler_text_gray);
        lineColor = mContext.getColor(R.color.ruler_line_gray);
        selectColor = mContext.getColor(R.color.ruler_text_red);

        minvelocity = ViewConfiguration.get(mContext).getScaledMinimumFlingVelocity();
        initPaint();
    }

    /**
     * @param startValue   开始值
     * @param endValue     结束值
     * @param currentValue 当前额度
     * @param upLimitValue 当前用户最高额度
     * @param listener
     */
    public void init(float startValue, float endValue, float currentValue, float upLimitValue, OnValueChangeListener listener) {
        init(startValue, endValue, currentValue, DEFAULT_UNIT, DEFAULT_MICRO_UNIT_COUNT, upLimitValue, listener);
    }

    /**
     * 初始化
     *
     * @param startValue     开始值
     * @param endValue       结束值
     * @param currentValue   当前额度
     * @param unit           单位
     * @param microUnitCount 小单位
     * @param listener
     */
    public void init(float startValue, float endValue, float currentValue, float unit, int microUnitCount, float upLimitValue, OnValueChangeListener listener) {
        this.startValue = startValue;
        this.endValue = endValue;
        this.currentValue = currentValue;
        if (currentValue < startValue) {
            this.currentValue = startValue;
        }
        if (currentValue > endValue) {
            this.currentValue = endValue;
        }
        this.originValue = this.currentValue;
        this.unit = unit;
        this.microUnit = microUnitCount == 0 ? 0 : unit / microUnitCount;
        this.microUnitCount = microUnitCount;
        this.upLimitValue = upLimitValue;
        this.listener = listener;
        this.maxX = (upLimitValue - currentValue) / unit * widthPerUnit;
        caculate();
    }

    private void initPaint() {
        pfdf = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        textPaint = new Paint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);

        unableTextPaint = new Paint();
        unableTextPaint.setColor(unableTextColor);
        unableTextPaint.setTextSize(textSize);

        linePaint = new Paint();
        linePaint.setColor(lineColor);

        selectPaint = new Paint();
        selectPaint.setColor(selectColor);
        selectPaint.setTextSize(selectTextSize);
        selectPaint.setFakeBoldText(true);

        brokenLinePaint = new Paint();
        brokenLinePaint.setStrokeWidth(DEFAULT_SHORT_LINE_STROKE_WIDTH - 1);
        brokenLinePaint.setColor(lineColor);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int widthMeasureSpec) {
        int measureMode = MeasureSpec.getMode(widthMeasureSpec);
        int measureSize = MeasureSpec.getSize(widthMeasureSpec);
        int result = getSuggestedMinimumWidth();
        switch (measureMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = measureSize;
                break;
            default:
                break;
        }
        width = result;
        return result;
    }

    private int measureHeight(int heightMeasure) {
        int measureMode = MeasureSpec.getMode(heightMeasure);
        int measureSize = MeasureSpec.getSize(heightMeasure);
        int result = (int) (longLineHeight * 4) + (int) getTextHeight(String.valueOf((int) currentValue), selectPaint);
        switch (measureMode) {
            case MeasureSpec.EXACTLY:
                result = Math.max(result, measureSize);
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(result, measureSize);
                break;
            default:
                break;
        }
        height = result;
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(pfdf);

        drawRuler(canvas);
        drawLine(canvas);
        drawBrokenLine(canvas, currentValue);
        drawSelectValue(canvas, currentValue);
        drawSelectLine(canvas);
    }

    private void drawBrokenLine(Canvas canvas, float selectValue) {
        String text = String.valueOf((int) selectValue);
        float textWidth = selectPaint.measureText(text);
        float textStartX = this.width / 2 - textWidth / 2;
        float textStartY = getHeight() - selectLineHeight - shortLineHeight - getTextHeight(text, textPaint);
        float distance = 10;
        float offset = 12;
        Path path = new Path();
        path.moveTo(textStartX, textStartY);
        path.lineTo(textStartX + textWidth + distance, textStartY);
        brokenLinePaint.setStyle(Paint.Style.STROKE);
        PathEffect pathEffect = new DashPathEffect(new float[]{distance, distance, distance, distance}, offset);
        brokenLinePaint.setPathEffect(pathEffect);
        canvas.drawPath(path, brokenLinePaint);
    }

    private void drawSelectValue(Canvas canvas, float selectValue) {
        String text = String.valueOf((int) caculateValue(selectValue));
        Rect rect = new Rect();
        selectPaint.getTextBounds(text, 0, text.length(), rect);
        int width = rect.width();
        float textStartX = this.width / 2 - width / 2 - DEFAULT_LONG_LINE_STROKE_WIDTH;
        float textStartY = getHeight() - selectLineHeight - shortLineHeight - getTextHeight(text, textPaint) - 17;
        canvas.drawText(text, textStartX, textStartY, selectPaint);
    }

    private void drawLine(Canvas canvas) {
        Path path = new Path();
        canvas.drawLine(0, getHeight(), getWidth(), getHeight(), linePaint);
    }

    private void drawRuler(Canvas canvas) {
        if (moveX < maxRightOffset) {
            moveX = maxRightOffset;
        }
        if (moveX > maxLeftOffset) {
            moveX = maxLeftOffset;
        }
        int halfCount = (int) (width / 2 / getBaseUnitWidth());
        float moveValue = (int) (moveX / getBaseUnitWidth()) * getBaseUnit();
        currentValue = originValue - moveValue;
        //剩余偏移量
        offset = moveX - (int) (moveX / getBaseUnitWidth()) * getBaseUnitWidth();

        for (int i = -halfCount - 1; i <= halfCount + 1; i++) {
            float value = ArithmeticUtil.addWithScale(currentValue, ArithmeticUtil.mulWithScale(i, getBaseUnit(), 2), 2);
            //只绘出范围内的图形
            if (value >= startValue && value <= endValue) {
                //画长的刻度
                float startx = width / 2 + offset + i * getBaseUnitWidth();
                if (startx > 0 && startx < width) {
                    if (microUnitCount != 0) {
                        if (ArithmeticUtil.remainder(value, unit) == 0) {
                            if (!(0 <= (startx - width / 2) && (startx - width / 2) < getBaseUnitWidth())) {
                                drawLongLine(canvas, i, value);
                            }
                        } else {
                            //画短线
                            drawShortLine(canvas, i, value);
                        }
                    } else {
                        //画长线
                        if (!(0 <= (startx - width / 2) && (startx - width / 2) < getBaseUnitWidth())) {
                            drawLongLine(canvas, i, value);
                        }
                    }
                }
            }
        }
        notifyValueChange();
    }

    private void notifyValueChange() {
       /* if (listener != null) {
            currentValue = ArithmeticUtil.round(currentValue, 2);
            if(currentValue % unit == 0){
                listener.onValueChange(currentValue);
            }
        }*/
    }

    private void drawShortLine(Canvas canvas, int i, float value) {
        linePaint.setStrokeWidth(DEFAULT_SHORT_LINE_STROKE_WIDTH);
        canvas.drawLine(width / 2 + offset + i * getBaseUnitWidth(), height, width / 2 + offset + i * getBaseUnitWidth(), height - shortLineHeight, linePaint);
    }

    private void drawLongLine(Canvas canvas, int i, float value) {
        String text = String.valueOf((int) value);
        linePaint.setStrokeWidth(DEFAULT_LONG_LINE_STROKE_WIDTH);
        //画长线
        canvas.drawLine(width / 2 + offset + i * getBaseUnitWidth(), height, width / 2 + offset + i * getBaseUnitWidth(), height - longLineHeight, linePaint);
        float textStartX = width / 2 + offset + i * getBaseUnitWidth() - textPaint.measureText((int) value + "") / 2;
        float textStartY = getHeight() - longLineHeight - shortLineHeight;
        //画刻度值
        if (!(Math.abs(offset + i * getBaseUnitWidth()) <= 2.5 * getBaseUnitWidth())) {
            drawText(canvas, value > upLimitValue ? unableTextPaint : textPaint, textStartX, textStartY, value);
        }
    }

    private void drawText(Canvas canvas, Paint paint, float startX, float startY, float value) {
        canvas.drawText(String.valueOf((int) value), startX, startY, paint);
    }

    private void drawSelectLine(Canvas canvas) {
        selectPaint.setStrokeWidth(DEFAULT_LONG_LINE_STROKE_WIDTH);
        canvas.drawLine(width / 2, height, width / 2, height - selectLineHeight, selectPaint);
    }

    private float caculateValue(float value) {
        if(value % unit == 0){
            if(listener != null){
                listener.onValueChange(value);
            }
        }
        int remainder = (int) (value % unit);
        if (remainder != 0) {
            if (remainder <= microUnit * 2) {
                return value - remainder;
            } else {
                return value - remainder + unit;
            }
        }
        return value;
    }

    private float getBaseUnitWidth() {
        if (microUnitCount != 0) {
            return widthPerMicroUnit;
        }
        return widthPerUnit;
    }

    private float getBaseUnit() {
        if (microUnitCount != 0) {
            return microUnit;
        }
        return unit;
    }

    private void caculate() {
        startValue = format(startValue);
        endValue = format(endValue);
        currentValue = format(currentValue);
        originValue = currentValue;
        if (unit == 0) {
            unit = DEFAULT_UNIT;
        }
        if (microUnitCount != 0) {
            widthPerMicroUnit = ArithmeticUtil.div(widthPerUnit, microUnitCount, 2);
        }
        maxRightOffset = -1 * ((endValue - originValue) * getBaseUnitWidth() / getBaseUnit());
        maxLeftOffset = ((originValue - startValue) * getBaseUnitWidth() / getBaseUnit());
        invalidate();
    }

    private float format(float vallue) {
        float result = vallue;
        if (getBaseUnit() < 0.1) {
            //0.01
            if (ArithmeticUtil.remainder(result, getBaseUnit()) != 0) {
                result += 0.01;
                result = format(result);
            }
        } else if (getBaseUnit() < 1) {
            //0.1
            if (ArithmeticUtil.remainder(result, getBaseUnit()) != 0) {
                result += 0.1;
                result = format(result);
            }
        } else if (getBaseUnit() < 10) {
            //1
            if (ArithmeticUtil.remainder(result, getBaseUnit()) != 0) {
                result += 1;
                result = format(result);
            }
        }
        return result;
    }


    private boolean isActionUp = false;
    private float mLastX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float xPosition = event.getX();

        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                isActionUp = false;
                scroller.forceFinished(true);
                if (null != animator) {
                    animator.cancel();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                isActionUp = false;
                float off = xPosition - mLastX;
                if ((moveX <= maxRightOffset) && off < 0 || (moveX >= maxLeftOffset) && off > 0) {
                } else {
                    moveX += off;
                    postInvalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isActionUp = true;
                f = true;

                countVelocityTracker(event);
                postInvalidate();
                return false;
            default:
                break;
        }

        mLastX = xPosition;
        return true;
    }

    private ValueAnimator animator;

    private boolean isCancel = false;

    private void startAnim() {
        isCancel = false;
        //添加滚动最大值
        float neededMoveX = ArithmeticUtil.mul(ArithmeticUtil.div(moveX, widthPerUnit, 0), widthPerUnit);
        if (isActionUp && f) {
            if (neededMoveX < 0 && Math.abs(neededMoveX) > maxX) {
                neededMoveX = -maxX;
            }
        }
        animator = new ValueAnimator().ofFloat(moveX, neededMoveX);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (!isCancel) {
                    moveX = (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                isCancel = true;
            }
        });
        animator.start();
    }

    private boolean f = true;

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            float off = scroller.getFinalX() - scroller.getCurrX();
            off = off * functionSpeed();
            if ((moveX <= maxRightOffset) && off < 0) {
                moveX = maxRightOffset;
                startAnim();
            } else if ((moveX >= maxLeftOffset) && off > 0) {
                moveX = maxLeftOffset;
            } else {
                moveX += off;
                if (scroller.isFinished()) {
                    startAnim();
                } else {
                    postInvalidate();
                    mLastX = scroller.getFinalX();
                }
            }
        } else {
            if (isActionUp && f) {
                startAnim();
                f = false;

            }
        }
    }

    /**
     * 控制滑动速度
     *
     * @return
     */
    private float functionSpeed() {
        return 0.2f;
    }

    private void countVelocityTracker(MotionEvent event) {
        velocityTracker.computeCurrentVelocity(1000, 3000);
        float xVelocity = velocityTracker.getXVelocity();
        if (Math.abs(xVelocity) > minvelocity) {
            scroller.fling(0, 0, (int) xVelocity, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
        }
    }

    private float getTextHeight(String str, Paint paint) {
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        return rect.height();
    }
}
