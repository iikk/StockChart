package cn.isaac.mystockchart.tryit.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.support.annotation.Px;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ScaleGestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.OverScroller;
import android.widget.RelativeLayout;

import cn.isaac.mystockchart.tryit.tools.CanvasTools;

/**
 * Created by RaoWei on 2017/6/29 17:07.
 */

public abstract class ScrollAndScaleView extends RelativeLayout implements ScaleGestureDetector.OnScaleGestureListener, GestureDetector.OnGestureListener {
    protected ScaleGestureDetector mScaleDetector;
    private float mScaleX;
    private float mScaleOldX;
    private float mScaleMinX;
    private float mScaleMaxX;
    private boolean mScaleEnable;
    private boolean isLongPress;
    protected GestureDetectorCompat mDetector;
    private OverScroller mScroller;
    private boolean isMultipTouch;
    private boolean isTouch;
    protected int mScrollX;
    private boolean mScrollEnable;

    public ScrollAndScaleView(Context context) {
        super(context);
        init();
    }

    public ScrollAndScaleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScrollAndScaleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        mScaleDetector = new ScaleGestureDetector(getContext(), this);
        mDetector = new GestureDetectorCompat(getContext(), this);
        mScroller = new OverScroller(getContext());

        mScrollX = 0;
        mScaleX = 1;
        mScaleOldX = 1;
        mScaleMaxX = 2f;
        mScaleMinX = 0.5f;

        isMultipTouch = false;
        isTouch = false;
        mScaleEnable = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                isTouch = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() == 1) {
                    //长按之后移动
                    if (isLongPress) {
                        onLongPress(event);
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                isLongPress = false;
                isTouch = false;
                invalidate();
                break;
        }
        isMultipTouch = event.getPointerCount()>1;
        mScaleDetector.onTouchEvent(event);
        mDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        if (!isScaleEnable()) {
            return false;
        }
        mScaleOldX = mScaleX;
        mScaleX *= detector.getScaleFactor();
        if (mScaleX < mScaleMinX) {
            mScaleX = mScaleMinX;
        } else if (mScaleX > mScaleMaxX) {
            mScaleX = mScaleMaxX;
        } else {
            onScaleChanged(mScaleX, mScaleOldX);
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

    protected void onScaleChanged(float scale, float oldScale){
        invalidate();
    }


    public boolean isScaleEnable(){
        //return mScaleEnable;
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (!isLongPress&&!isMultipTouch) {
            scrollBy(Math.round(distanceX), 0);
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        isLongPress = true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (isTouch) {
            mScroller.fling(mScrollX,0,Math.round(velocityX/mScaleX),0,Integer.MIN_VALUE,Integer.MAX_VALUE,0,0);
        }
        return true;
    }

    @Override
    public void scrollBy(@Px int x, @Px int y) {
        scrollTo(mScrollX - Math.round(x/mScaleX), 0);
    }

    @Override
    public void scrollTo(@Px int x, @Px int y) {
        if (!isScaleEnable()) {
            return;
        }
        int oldX = mScrollX;
        mScrollX = x;
        checkAndFixScrollX();
        onScrollChanged(mScrollX, 0, oldX, 0);
        invalidate();
    }

    protected void checkAndFixScrollX() {
        if (mScrollX < getMinScrollX()) {
            mScaleX = getMinScrollX();
            onRightSide();
            mScroller.forceFinished(true);
        } else if (mScrollX > getMaxScrollX()) {
            mScrollX = getMaxScrollX();
            onLeftSide();
            mScroller.forceFinished(true);
        }
    }

    /**
     * 滑动到了最左边
     */
    protected abstract void onLeftSide();

    /**
     * 获取位移的最大值
     * @return
     */
    protected abstract int getMaxScrollX();

    /**
     * 滑动到了最右边
     */
    protected abstract void onRightSide();

    /**
     * 获取位移的最小值
     * @return
     */
    protected abstract int getMinScrollX();

    /**
     * 设置是否可以滑动
     * @param b
     */
    public void setScrollEnable(boolean b) {
        mScrollEnable = b;
    }

    /**
     * 设置是否可以缩放
     * @param b
     */
    public void setScaleEnable(boolean b) {
        mScaleEnable = b;
    }
}
