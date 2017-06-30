package cn.isaac.mystockchart.chartlib;

import android.content.Context;
import android.support.annotation.Px;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.OverScroller;
import android.widget.RelativeLayout;

/**
 * 可以滑动和放大的view
 * Created by RaoWei on 2017/6/15 13:06.
 */

public abstract class ScrollAndScaleView extends RelativeLayout implements
        GestureDetector.OnGestureListener,
        ScaleGestureDetector.OnScaleGestureListener{

    private GestureDetectorCompat mDetector;
    private ScaleGestureDetector mScaleDetector;
    private OverScroller mScroller;
    //是否在触摸中
    private boolean touch = false;
    //是否是长按事件
    private boolean isLongPress = false;
    //是否是多点触摸
    private boolean mMultipleTouch = false;
    //x轴的偏移量
    private int mScrollX = 0;
    private float mScaleX = 1;
    //x轴最小缩放程度
    private float mScaleXMin = 0.5f;
    //x轴最大缩放程度
    private float mScaleXMax = 2f;

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
        setWillNotDraw(false);  //ViewGroup默认情况下，处于性能考虑，会被设置成WILL_NOT_DROW,这样ondraw就不会被执行；此处设置会去掉WILL_NOT_DROW,onDraw()会被调用
        mDetector = new GestureDetectorCompat(getContext(), this);  //手势识别
        mScaleDetector = new ScaleGestureDetector(getContext(), this);  //专门检测2个手指在屏幕上做缩放手势用的
        mScroller = new OverScroller(getContext());  //为了实现View平滑滚动的一个help类
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
        if (!isLongPress() && !isMultipleTouch()) {
            scrollBy(Math.round(distanceX), 0);  //round 四舍五入后最接近的整数
            return true;
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        isLongPress = true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (isTouch()) {
            mScroller.fling(mScrollX, 0
                        , Math.round(velocityX / mScaleX), 0
                        , Integer.MIN_VALUE, Integer.MAX_VALUE
                        , 0, 0);
        }
        return true;
    }

    @Override
    public void computeScroll() {  //通过draw调用
        if (mScroller.computeScrollOffset()) {
            if (!isTouch()) {
                scrollTo(mScroller.getCurrX(), mScroller.getCurrY());  //根据scroll的计算结果移动view
            } else {
                mScroller.forceFinished(true);
            }
        }
    }

    @Override
    public void scrollBy(@Px int x, @Px int y) {
        scrollTo(mScrollX - Math.round(x / mScaleX), 0);
    }

    @Override
    public void scrollTo(@Px int x, @Px int y) {
        int oldX = mScrollX;
        mScrollX = x;
        checkAndFixScrollX();
        onScrollChanged(mScrollX, 0, oldX, 0);
        invalidate();
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float oldScale = mScaleX;
        mScrollX *= detector.getScaleFactor();
        if (mScrollX < mScaleXMin) {
            mScaleX = mScaleXMin;
        } else if (mScaleX > mScaleXMax) {
            mScaleX = mScaleXMax;
        } else {
            onScaleChanged(mScaleX, oldScale);
        }
        return true;
    }

    private void onScaleChanged(float scaleX, float oldScale) {
        invalidate();
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return false;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {  //支持多点事件处理，可使用ACTION_POINTER_DOWNHE和ACTION_POINTER_UP
            case MotionEvent.ACTION_DOWN:
                touch = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() == 1) {
                    //长按之后
                    if (isLongPress) {
                        onLongPress(event);
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                invalidate();  //触发drow
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isLongPress = false;
                touch = false;
                invalidate();
                break;
        }
        mMultipleTouch = event.getPointerCount()>1;
        mDetector.onTouchEvent(event);
        mScaleDetector.onTouchEvent(event);
        return true;
    }

    public boolean isMultipleTouch() {
        return mMultipleTouch;
    }

    public boolean isTouch(){
        return touch;
    }

    public boolean isLongPress() {
        return isLongPress;
    }

    protected void checkAndFixScrollX() {
        if (mScrollX < getMinScrollX()) {
            mScrollX = getMinScrollX();
            onRightSide();
            mScroller.forceFinished(true);
        } else if (mScrollX > getMaxScrollX()) {
            mScrollX = getMaxScrollX();
            onLeftSide();
            mScroller.forceFinished(true);
        }
    }


    /**
     * 获取横向唯一最小值
     * @return
     */
    public abstract int getMinScrollX();

    /**
     * 获取横向位移最大值
     * @return
     */
    public abstract int getMaxScrollX();

    /**
     * 滑动到了最右边
     */
    public abstract void onRightSide();

    /**
     * 滑动到了最左边
     */
    public abstract void onLeftSide();

}
