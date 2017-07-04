package cn.isaac.mystockchart.test.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Random;

import cn.isaac.mystockchart.tryit.view.ScrollAndScaleView;

/**
 * Created by RaoWei on 2017/6/30 15:13.
 */

public class ScaleView extends ScrollAndScaleView {
    private float mScaleX;
    private float mScaleOldX;
    private float mHeigh;
    private float mWidth;
    private ArrayList<int[]> mList;
    private Paint mPaint;
    private float mTranslateX;

    public ScaleView(Context context) {
        super(context);
        init();
    }

    public ScaleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScaleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        mList = new ArrayList<>();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor("#ffffff"));
        mScaleX = mScaleOldX =1;
    }

    @Override
    protected void onScaleChanged(float scale, float oldScale) {
        mScaleOldX = oldScale;
        mScaleX = scale;
        checkAndFixScrollX();
        setTranslateXFromScrollX(mScrollX);
        super.onScaleChanged(scale, oldScale);
    }

    private void setTranslateXFromScrollX(float scrollX) {
        mTranslateX = scrollX + getMinTranslateX();
    }

    private float getMinTranslateX() {
        return -100;
    }

    @Override
    protected void onLeftSide() {

    }

    @Override
    protected int getMaxScrollX() {
        return 10;
    }

    @Override
    protected void onRightSide() {

    }

    @Override
    protected int getMinScrollX() {
        return 0;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeigh = h;
        Random random = new Random();
        int diff;
        int diff1;
        int x;
        int y;
        mList.clear();
        for (int i = 0; i < 10*mWidth; i++) {
        diff = random.nextInt(100);
        diff1 = random.nextInt(500);
            y = (int) (mHeigh/2 + diff1);
            x = i + diff;
            mList.add(new int[]{x, y});
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.parseColor("#000000"));
        canvas.scale(mScaleX,1);
        for (int i = 0; i < mList.size() - 1; i++) {
            canvas.drawLine(mList.get(i)[0],mList.get(i)[1],mList.get(i+1)[0],mList.get(i+1)[1],mPaint);
        }
        Log.e("scale", "canvas.getWidth()="+canvas.getWidth()+", canvas.getHeight()="+canvas.getHeight());
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        setTranslateXFromScrollX(mScrollX);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return super.onTouchEvent(event);
    }
}
