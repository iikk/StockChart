package cn.isaac.mystockchart.test.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.widget.OverScroller;
import android.widget.RelativeLayout;

import java.util.Random;

/**
 * Created by RaoWei on 2017/7/4 13:22.
 */

public class RulerView extends RelativeLayout {
    private int mHeigh;
    private int mWidth;
    private Paint mPaint;
    private int mWhiteColor;
    private float dp1;
    private float sp10;
    private int maxValue;
    private int minValue;
    private int mBlackColor;
    private int count;
    private int mSpace;
    private int heigh;
    private OverScroller mScroller;
    private int overScrollValue;
    private int mScrollX;
    private int startIndex;
    private int stopIndex;
    private VelocityTracker mVelocityTracker;
    private int mTouchSlop;
    private int mMaxFlingSpeed;
    private int mMinFlingSpeed;
    private int mOverflingDistance;
    private int mOverscrollDistance;
    private Random mRandom;
    private int maxDistance;
    private int minDistance;
    private int VelocityUnit;

    public RulerView(Context context) {
        super(context);
        init();
    }

    public RulerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RulerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mWhiteColor = Color.parseColor("#ffffff");
        mBlackColor = Color.parseColor("#000000");
        dp1 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
        sp10 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics());

        mPaint.setColor(mWhiteColor);
        mPaint.setStrokeWidth(dp1);

        maxValue = 500;
        minValue = 0;
        count = 20;
        heigh = 50;
        overScrollValue = 100;
        stopIndex = 500;
        VelocityUnit = 500;
        startIndex = stopIndex - count;

        maxDistance = Integer.MAX_VALUE;
        minDistance = Integer.MIN_VALUE;

        mScroller = new OverScroller(getContext());
        mRandom = new Random();

        ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        mMaxFlingSpeed = configuration.getScaledMaximumFlingVelocity();
        mMinFlingSpeed = configuration.getScaledMinimumFlingVelocity();
        mOverflingDistance = configuration.getScaledOverflingDistance();
        mOverscrollDistance = configuration.getScaledOverscrollDistance();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeigh = h;
        mWidth = w;

        mSpace = mWidth / count;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(mBlackColor);
        canvas.drawLine(mScrollX, mHeigh/2, mWidth + mScrollX, mHeigh/2, mPaint);
        startIndex = mScrollX/mSpace;
        stopIndex = startIndex + count;
        //canvas.translate(mScrollX, 0);
        for (int i = startIndex; i <= stopIndex; i++) {
            canvas.drawLine(getStartX(i) ,mHeigh/2, getStartX(i), mHeigh/2 - heigh, mPaint);
            canvas.drawText(String.valueOf(i), getStartX(i), mHeigh/2 + 20, mPaint);
        }
        canvas.drawText("mScrollX:" + mScrollX,mScrollX,mHeigh-20,mPaint);
    }

    private float getStartX(int index) {
        return mSpace/2 + mSpace*index;
    }


    float moveX;
    float lastX;
    float downX;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        initVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                downX = event.getX();
                lastX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = event.getX();
                scrollBy((int) (lastX-moveX), 0);
                lastX = moveX;
                mScrollX = getScrollX();
                break;
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(VelocityUnit, mMaxFlingSpeed);
                int velocityX = -(int) mVelocityTracker.getXVelocity();
                if (Math.abs(velocityX) > mMinFlingSpeed) {
                    mScroller.fling(getScrollX(),0, velocityX,0, minDistance,maxDistance,0,0);
                    invalidate();
                }
                recycleVelocityTracker();
                break;
        }
        return true;
    }

    private void initVelocityTracker(MotionEvent ev) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mScrollX = mScroller.getCurrX();
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
        }
    }
}
