package cn.isaac.mystockchart.test.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.widget.OverScroller;
import android.widget.RelativeLayout;
import android.widget.Space;

import java.util.Random;

import cn.isaac.mystockchart.R;
import cn.isaac.mystockchart.tryit.tools.ToastTools;

/**
 * Created by RaoWei on 2017/7/4 13:22.
 */

public class RulerView extends RelativeLayout {
    private final static int HANDLER_LONG_PRESSED = 1;
    private final static int LONG_PRESSED_CODE = 1;
    private final static int NOT_LONG_PRESSED_CODE = 2;
    private final static int CAN_SCROLL_CODE = 3;
    private final static int CAN_NOT_SCROLL_CODE = 4;
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
    private boolean mIsLongPressed;
    private boolean mIsMultipleTouch;
    private boolean mCanScroll;
    private Handler mHandler;
    private int longPressedInterval;
    private int canScrollInterval;
    private ScaleGestureDetector mScaleDetector;
    private float mScaleOldX;
    private float mScaleX;
    private float mScaleMinX;
    private float mScaleMaxX;

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
        mScaleMaxX = 2f;
        mScaleMinX = 0.5f;
        mScaleX = 1;
        mIsMultipleTouch = false;
        mIsLongPressed = false;
        mCanScroll = true;

        maxDistance = Integer.MAX_VALUE;
        minDistance = Integer.MIN_VALUE;

        mScroller = new OverScroller(getContext());
        mRandom = new Random();
        mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                mScaleOldX = mScaleX;
                mScaleX *= detector.getScaleFactor();
                if (mScaleX < mScaleMinX) {
                    mScaleX = mScaleMinX;
                } else if (mScaleX > mScaleMaxX) {
                    mScaleX = mScaleMaxX;
                } else {
                    ToastTools.showShort("scale:"+ mScaleX);
                    //正常放大缩小
                    invalidate();
                }
                return true;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {

            }
        });
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case LONG_PRESSED_CODE:
                        mIsLongPressed = true;
                        ToastTools.showShort("长按：lastX=" + lastX);
                        invalidate();
                        break;
                    case NOT_LONG_PRESSED_CODE:
                        mIsLongPressed = false;
                        invalidate();
                        break;
                    case CAN_SCROLL_CODE:
                        mCanScroll = true;
                        break;
                    case CAN_NOT_SCROLL_CODE:
                        mCanScroll = false;
                        break;
                }
                return false;
            }
        });
        longPressedInterval = 500;
        canScrollInterval = 800;


        ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        mMaxFlingSpeed = configuration.getScaledMaximumFlingVelocity();
        mMinFlingSpeed = configuration.getScaledMinimumFlingVelocity();

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
        canvas.save();
        //canvas.scale(mScaleX,1);
        if (mIsMultipleTouch) {
            mSpace = mWidth / 20;
            mSpace = (int) (mSpace*mScaleX);
            //if (mSpace == 0) {
            //    mSpace = 1;
            //
            //}
            //if (mSpace >= mWidth/2) {
            //    mSpace = mWidth/2;
            //}
            count = mWidth / mSpace;
        }
        //Log.e("RulerView", "mSpace:" +mSpace + ", scale:"+ mScaleX);
        canvas.drawLine(mScrollX, mHeigh/2, mWidth + mScrollX, mHeigh/2, mPaint);
        startIndex = mScrollX/mSpace;
        stopIndex = startIndex + count;
        //canvas.translate(mScrollX*mScaleX, 0);
        for (int i = startIndex; i <= stopIndex; i++) {
            canvas.drawLine(getStartX(i) ,mHeigh/2, getStartX(i), mHeigh/2 - heigh, mPaint);
            canvas.drawText(String.valueOf(i), getStartX(i), mHeigh/2 + 20, mPaint);
        }
        canvas.restore();
        canvas.drawText("mScrollX:" + mScrollX,mScrollX,mHeigh-20,mPaint);

        //长按画坐标轴
        if (mIsLongPressed && !mIsMultipleTouch) {
            canvas.drawLine(mSpace*startIndex + lastX,0,mSpace*startIndex + lastX,mHeigh,mPaint);
            canvas.drawText("当前x轴："+(startIndex+lastX/mSpace),getStartX(startIndex) + lastX,20,mPaint);
        }

        //ToastTools.showShort(getContext().getString(R.string.test));
    }

    private float getStartX(int index) {
        return mSpace/2 + mSpace*index;
    }


    float moveX;
    float lastX;
    float downX;
    float moveY;
    float downY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mCanScroll) {
            initVelocityTracker(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                downX = event.getX();
                lastX = event.getX();
                downY = event.getY();
                sendPrepareLongPressed();
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = event.getX();
                moveY = event.getY();

                if (!mIsMultipleTouch) {
                    if (mIsLongPressed) {
                        ToastTools.showShort("长按：lastX=" + lastX);
                        cancelCanScroll();
                        invalidate();
                    } else {
                        if (Math.abs(moveX - downX) > mTouchSlop || Math.abs(moveY - downY) > mTouchSlop) {
                            cancelLongPressed();
                        }
                        sendCanScroll();
                        scrollBy((int) (lastX - moveX), 0);
                        mScrollX = getScrollX();
                    }
                } else {
                    cancelLongPressed();
                    cancelCanScroll();
                }

                lastX = moveX;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mCanScroll) {
                    mVelocityTracker.computeCurrentVelocity(VelocityUnit, mMaxFlingSpeed);
                    int velocityX = -(int) mVelocityTracker.getXVelocity();
                    if (Math.abs(velocityX) > mMinFlingSpeed) {
                        mScroller.fling(getScrollX(),0, velocityX,0, minDistance,maxDistance,0,0);
                        invalidate();
                    }
                    recycleVelocityTracker();
                }
                cancelLongPressed();
                break;
        }
        mIsMultipleTouch = event.getPointerCount()>1;
        if (mIsMultipleTouch) {
            cancelCanScroll();
            mScaleDetector.onTouchEvent(event);
        }
        return true;
    }

    private void sendCanScroll() {
        mHandler.sendEmptyMessageDelayed(CAN_SCROLL_CODE, canScrollInterval);
    }


    private void cancelCanScroll() {
        mHandler.removeMessages(CAN_SCROLL_CODE);
        mHandler.sendEmptyMessage(CAN_NOT_SCROLL_CODE);
    }

    private void sendPrepareLongPressed() {
        mHandler.sendEmptyMessageDelayed(LONG_PRESSED_CODE, canScrollInterval);
    }

    private void cancelLongPressed() {
        mHandler.removeMessages(LONG_PRESSED_CODE);
        mHandler.sendEmptyMessage(NOT_LONG_PRESSED_CODE);
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
