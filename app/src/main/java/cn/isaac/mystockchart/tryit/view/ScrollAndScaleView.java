package cn.isaac.mystockchart.tryit.view;

import android.content.Context;
import android.support.v4.view.ScaleGestureDetectorCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.RelativeLayout;

/**
 * Created by RaoWei on 2017/6/29 17:07.
 */

public abstract class ScrollAndScaleView extends RelativeLayout implements ScaleGestureDetector.OnScaleGestureListener {
    private ScaleGestureDetector mScaleDetector;
    private float mScaleX = 1;
    private float mScaleOldX = 1;
    private float mScaleMinX = 0.5f;
    private float mScaleMaxX = 3;
    private boolean mScaleEnable;

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

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleDetector.onTouchEvent(event);
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
}
