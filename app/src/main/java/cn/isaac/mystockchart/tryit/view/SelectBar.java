package cn.isaac.mystockchart.tryit.view;

import android.content.Context;
import android.drm.DrmRights;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.Toast;

import cn.isaac.mystockchart.chartlib.impl.IKChartView;

/**
 * Created by RaoWei on 2017/6/26 14:12.
 */

public class SelectBar extends RelativeLayout {
    private int mWidth;
    private int mHeigh;
    private String[] mTextArray;
    private int mRightPadding;
    private int mLeftPadding;
    private float mItemWidth;
    private TextPaint mTextPaint;
    private int mBlack;
    private int mRed;
    private Paint mPaint;
    private float mLineHalfWidth;
    private float mHalfTextWidth;
    private Float[] mTextX;
    private Float[] mLineXStart;
    private Float[] mLineXEnd;
    private RectF[] mTextRect;
    private int downIn;
    private int choiceIn;
    private float mHalfTextWidth1;
    private float mLineHalfWidth1;
    private OnSelectedListener mListener;

    public SelectBar(Context context) {
        super(context);
        init();
    }

    public SelectBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SelectBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        mLeftPadding = px(0);
        mRightPadding = px(0);
        mTextArray = new String[]{"分时", "日K", "周K", "月K"};

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

        mTextPaint.setTextSize(sp(16));
        mBlack = Color.parseColor("#333333");
        mRed = Color.parseColor("#EF3331");

        downIn = 0;
        choiceIn = 0;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeigh = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        calculateValue();
        canvas.drawColor(Color.parseColor("#F3F3F3"));
        drawText(canvas);
        drawSelect(canvas);
    }

    private void drawSelect(Canvas canvas) {

    }

    private void drawText(Canvas canvas) {
        for (int i = 0; i < mTextArray.length; i++) {
            if (i == choiceIn) {
                mTextPaint.setColor(mRed);
                canvas.drawText(mTextArray[i], mTextX[i],mHeigh/2+sp(5),mTextPaint);

                mPaint.setColor(mRed);
                mPaint.setStrokeWidth(px(3));
                canvas.drawLine(mLineXStart[i],mHeigh-px(1), mLineXEnd[i],mHeigh-px(1),mPaint);
                continue;
            }
            mTextPaint.setColor(mBlack);
            canvas.drawText(mTextArray[i], mTextX[i],mHeigh/2+sp(5),mTextPaint);

        }

    }

    private void calculateValue() {
        mItemWidth = (mWidth-mLeftPadding-mRightPadding)*1f / mTextArray.length;

        mHalfTextWidth = mTextPaint.measureText(mTextArray[0]) / 2.0f;
        mHalfTextWidth1 = mTextPaint.measureText(mTextArray[1]) / 2.0f;
        mLineHalfWidth = mHalfTextWidth *2*0.7f;
        mLineHalfWidth1 = mHalfTextWidth1 *2*0.7f;
        mTextX = new Float[]{mLeftPadding+mItemWidth/2- mHalfTextWidth,mLeftPadding+mItemWidth+mItemWidth/2- mHalfTextWidth1,mLeftPadding+mItemWidth*2+mItemWidth/2- mHalfTextWidth1,mLeftPadding+mItemWidth*3+mItemWidth/2- mHalfTextWidth1};
        mLineXStart = new Float[]{mLeftPadding+mItemWidth/2-mLineHalfWidth,mLeftPadding+mItemWidth+mItemWidth/2- mLineHalfWidth1,mLeftPadding+mItemWidth*2+mItemWidth/2- mLineHalfWidth1,mLeftPadding+mItemWidth*3+mItemWidth/2- mLineHalfWidth1};
        mLineXEnd = new Float[]{mLeftPadding+mItemWidth/2+mLineHalfWidth,mLeftPadding+mItemWidth+mItemWidth/2+mLineHalfWidth1,mLeftPadding+mItemWidth*2+mItemWidth/2+mLineHalfWidth1,mLeftPadding+mItemWidth*3+mItemWidth/2+mLineHalfWidth1};
        mTextRect = new RectF[]{new RectF(mLeftPadding, 0, mLeftPadding+mItemWidth, mHeigh),new RectF(mLeftPadding+mItemWidth, 0, mLeftPadding+mItemWidth * 2, mHeigh),new RectF(mLeftPadding+mItemWidth*2, 0,mLeftPadding+mItemWidth * 3, mHeigh),new RectF(mLeftPadding+mItemWidth*3, 0, mLeftPadding+mItemWidth * 4, mHeigh)};
        new RectF(mTextX[0], 0, mTextX[0] + mHalfTextWidth * 2, mHeigh);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (int i = 0; i < mTextRect.length; i++) {
                    if (mTextRect[i].contains(event.getX(), event.getY())) {
                        downIn = i;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (mTextRect[downIn].contains(event.getX(), event.getY())) {
                    choiceIn=downIn;
                    invalidate();
                    if (mListener !=null) {
                        mListener.onSelectChanged(downIn);
                        //return true;
                    }
                }
                break;
        }
        return true;
    }

    public interface OnSelectedListener{
        void onSelectChanged(int index);
    }
    public void setOnSelectChanged(OnSelectedListener listener){
        if (listener!= null) {
            mListener=listener;
        }
    }

    private float getBaseLine(TextPaint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (fm.descent - fm.ascent - fm.bottom - fm.top) / 2;
    }

    private float getTextHeigh(TextPaint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.descent - fm.ascent;
    }

    private int px(float dp) {
        return (int)(dp * getContext().getResources().getDisplayMetrics().density + 0.5f);
    }

    private int sp(float dp) {
        return (int) (dp * getContext().getResources().getDisplayMetrics().scaledDensity + 0.5f);
    }

    private int getColor(int resId) {
        return ContextCompat.getColor(getContext(), resId);
    }
}
