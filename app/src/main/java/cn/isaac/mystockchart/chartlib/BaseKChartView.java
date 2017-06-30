package cn.isaac.mystockchart.chartlib;

import android.animation.ValueAnimator;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.ScaleGestureDetector;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.isaac.mystockchart.R;
import cn.isaac.mystockchart.chartlib.KChartTabView;
import cn.isaac.mystockchart.chartlib.ScrollAndScaleView;
import cn.isaac.mystockchart.chartlib.entityImpl.KLineImpl;
import cn.isaac.mystockchart.chartlib.formatter.TimeFormatter;
import cn.isaac.mystockchart.chartlib.formatter.ValueFormatter;
import cn.isaac.mystockchart.chartlib.impl.IAdapter;
import cn.isaac.mystockchart.chartlib.impl.IChartDraw;
import cn.isaac.mystockchart.chartlib.impl.IDateTimeFormatter;
import cn.isaac.mystockchart.chartlib.impl.IKChartView;
import cn.isaac.mystockchart.chartlib.impl.IValueFormatter;
import cn.isaac.mystockchart.chartlib.utils.ViewUtil;

/**
 * Created by RaoWei on 2017/6/15 15:22.
 */

public abstract class BaseKChartView extends ScrollAndScaleView implements
        IKChartView, IKChartView.OnSelectedChangedListener {

    private int mChildDrawPosition = 0;
    //x轴的偏移量
    private float mTranslateX = Float.MIN_VALUE;
    //k线图形区域的高度
    private int mMainHeight = 0;
    //图形区域的高度
    private int mWidth = 0;
    //下方子图的高度
    private int mChildHeight = 0;
    //图像上padding
    private int mTopPadding = 15;
    //图像下padding
    private int mBottomPadding = 15;
    //k线图和子图像之间的间隙
    private int mMainChildSpace = 30;
    //k线图y轴的缩放
    private float mMainScaleY = 1;
    //子图轴的缩放
    private float mChildScaleY = 1;
    //数据的真实长度
    private int mDataLen = 0;
    //k线图当前显示区域的最大值
    private float mMainMaxValue = Float.MAX_VALUE;
    //k线图当前显示区域的最小值
    private float mMainMinValue = Float.MIN_VALUE;
    //子视图当前显示区域的最大值
    private float mChildMaxValue = Float.MAX_VALUE;
    //子视图当前显示区域的最小值
    private float mChildMinValue = Float.MIN_VALUE;
    //显示区域中x开始点在数组的位置
    private int mStartIndex = 0;
    //显示区域中x结束点在数组的位置
    private int mStopIndex = 0;
    //一个点的宽度
    private int mPointWidth = 6;
    //grid的行数
    private int mGridRows = 4;
    //grid的列数
    private int mGridColumns = 4;
    //字体大小
    private int mTextSize = 10;
    //表格画笔
    private Paint mGridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //文字画笔
    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //背景画笔
    private Paint mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //长按之后选择的点的序号
    private int SelectedIndex;
    //Main区域的画图方法
    private IChartDraw mMainDraw;
    //数据适配器
    private IAdapter mAdapter;
    //数据观察者
    private DataSetObserver mDataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            mItemCount = getAdapter().getCount();
            notifyChanged();
        }

        @Override
        public void onInvalidated() {
            mItemCount = getAdapter().getCount();
            notifyChanged();
        }
    };

    //当前点的个数
    private int mItemCount;
    //每个点的x坐标
    private List<Float> mXs = new ArrayList<>();
    private IChartDraw mChildDraw;
    private List<IChartDraw> mChildDraws = new ArrayList<>();

    private IValueFormatter mValueFormatter;
    private IDateTimeFormatter mDateTimeFormatter;

    private KChartTabView mKChartTabView;

    private ValueAnimator mAnimator;

    private long mAnimationDouration = 500;

    private int mBackgroundColor = Color.parseColor("#202326");

    private float mOverScrollRange = 0;

    private OnSelectedChangedListener mOnSelectedChangedListener = null;
    private GestureDetectorCompat mDetector;
    private ScaleGestureDetector mGestureDetector;
    //x轴的偏移量
    private int mScrollX = 0;
    private int mScaleX = 1;
    //长按之后选择的点的序号
    private int mSelectedIndex;


    public BaseKChartView(Context context) {
        super(context);
        init();
    }

    public BaseKChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseKChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        mDetector = new GestureDetectorCompat(getContext(), this);
        mGestureDetector = new ScaleGestureDetector(getContext(), this);
        mTopPadding = dp2px(mTopPadding);
        mBottomPadding = dp2px(mBottomPadding);
        mMainChildSpace = dp2px(mMainChildSpace);
        mPointWidth = dp2px(mPointWidth);
        mTextSize = dp2px(mTextSize);
        mBackgroundPaint.setColor(getResColor(R.color.chart_background));
        mGridPaint.setColor(getResColor(R.color.chart_grid_line));
        mGridPaint.setStrokeWidth(dp2px(1));
        mTextPaint.setColor(getResColor(R.color.chart_text));
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setStrokeWidth(dp2px(.5f));

        mKChartTabView = new KChartTabView(getContext());
        addView(mKChartTabView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mKChartTabView.setOnTabSelectListener(new KChartTabView.TabSelectListener() {
            @Override
            public void onTabSelected(int position) {
                setChildDraw(position);
            }
        });

        mAnimator = ValueAnimator.ofFloat(0f, 1f);
        mAnimator.setDuration(mAnimationDouration);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidate();
            }
        });
        startAnimation();
    }

    private void setChildDraw(int position) {
        mChildDraw = mChildDraws.get(position);
        mChildDrawPosition = position;
        invalidate();
    }

    private int dp2px(float dp) {
        return ViewUtil.dp2px(getContext(), dp);
    }

    private int px2dp(float px) {
        return ViewUtil.px2dp(getContext(), px);
    }

    private int getResColor(int resId) {
        return ContextCompat.getColor(getContext(), resId);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mMainChildSpace = mKChartTabView.getMeasuredHeight() + dp2px(1);
        int displayHeight = h - mTopPadding - mBottomPadding - mMainChildSpace;
        mMainHeight = (int) (displayHeight * 0.75f);
        mChildHeight = (int) (displayHeight * 0.25f);
        mWidth = w;
        mKChartTabView.setTranslationY(mMainHeight + mTopPadding);
        setTranslateXFromScrollX(mScrollX);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(mBackgroundColor);
        if (mWidth == 0 || mMainHeight == 0 || mItemCount == 0) {
            return;
        }
        calculateValue();
        canvas.save();
        canvas.translate(0, mTopPadding);
        canvas.scale(1, 1);
        drawGrid(canvas);
        drawK(canvas);
        drawText(canvas);
        drawValue(canvas, isLongPress() ? mSelectedIndex : mStopIndex);
        canvas.restore();
    }

    /**
     * 画值 显示某个点的值
     * @param canvas
     * @param position
     */
    private void drawValue(Canvas canvas, int position) {
        if (position >= 0 && position < mItemCount) {
            if (mMainDraw != null) {
                float y = -dp2px(1);
                float x = dp2px(1);
                mMainDraw.drawText(canvas, this, position, x, y);
            }
            if (mChildDraw != null) {
                Paint.FontMetrics fm = mTextPaint.getFontMetrics();
                float textHeight = fm.descent - fm.ascent;
                float baseLine = (textHeight - fm.bottom - fm.top) / 2;
                float y = mMainHeight + mMainChildSpace + baseLine;
                float x = mTextPaint.measureText(formatValue(mChildMaxValue) + " ");
                mChildDraw.drawText(canvas, this, position, x,y);
            }
        }
    }

    /**
     * 画文字
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        float baseLine = (textHeight - fm.bottom - fm.top) / 2;
        //--------------------画上方k线图的值-------------------
        if (mMainDraw != null) {
            canvas.drawText(formatValue(mMainMaxValue), 0, baseLine, mTextPaint);
            canvas.drawText(formatValue(mMainMinValue), 0, mMainHeight, mTextPaint);
            float rowValue = (mMainMaxValue - mMainMinValue) / mGridRows;
            float rowSpace = mMainHeight / mGridRows;
            for (int i = 0; i < mGridRows; i++) {
                String text = formatValue(rowValue * (mGridRows - i) + mMainMinValue);
                canvas.drawText(text, 0, fixTextY(rowSpace * i), mTextPaint);
            }
        }
        //--------------------画下方子图的值-------------------
        if (mChildDraw != null) {
            canvas.drawText(formatValue(mChildMaxValue), 0, mMainHeight + mMainChildSpace + baseLine, mTextPaint);
            canvas.drawText(formatValue(mChildMinValue), 0, mMainHeight + mMainChildSpace + mChildHeight, mTextPaint);
        }
        //--------------------画时间-------------------
        float columnSpace = mWidth / mGridColumns;
        float y = mMainHeight + mMainChildSpace + mChildHeight + baseLine;

        float startX = getX(mStartIndex) - mPointWidth / 2;
        float stopX = getX(mStopIndex) + mPointWidth / 2;

        for (int i = 0; i < mGridColumns; i++) {
            float translateX = xToTranslateX(columnSpace * i);
            if (translateX >= startX && translateX <= stopX) {
                int index = indexOfTranslateX(translateX);
                String text = formatDateTime(mAdapter.getDate(index));
                canvas.drawText(text, columnSpace * i - mTextPaint.measureText(text) / 2, y, mTextPaint);
            }
        }

        float translateX = xToTranslateX(0);
        if (translateX >= startX && translateX <= stopX) {
            canvas.drawText(formatDateTime(getAdapter().getDate(mStartIndex)), 0 ,y, mTextPaint);
        }
        translateX = xToTranslateX(mWidth);
        if (translateX >= startX && translateX <= stopX) {
            String text = formatDateTime(getAdapter().getDate(mStopIndex));
            canvas.drawText(text, mWidth - mTextPaint.measureText(text), y, mTextPaint);
        }
        if (isLongPress()) {
            KLineImpl point = (KLineImpl) getItem(mSelectedIndex);
            String text = formatValue(point.getClosePrice());
            float r = textHeight / 2;
            y = getMainY(point.getClosePrice());
            float x;
            if (translateXToX(getX(mSelectedIndex)) < getChartWidth() / 2) {
                x = 0;
                canvas.drawRect(x, y - r, mTextPaint.measureText(text), y + r, mBackgroundPaint);
            } else {
                x = mWidth - mTextPaint.measureText(text);
                canvas.drawRect(x, y - r, mWidth, y + r, mBackgroundPaint);
            }
            canvas.drawText(text, x, fixTextY(y), mTextPaint);
        }

    }

    /**
     * 解决text居中的问题
     * @param y
     * @return
     */
    private float fixTextY(float y) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        return y + (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent;
    }

    /**
     * 画k线图
     * @param canvas
     */
    private void drawK(Canvas canvas) {
        //保存之前的平移，缩放
        canvas.save();
        canvas.translate(mTranslateX * mScaleX, 0);
        canvas.scale(mScaleX, 1);
        for (int i = mStartIndex; i <= mStopIndex; i++) {
            Object currentPoint = getItem(i);
            float currentPointX = getX(i);
            Object lastPoint = i == 0 ? currentPoint : getItem(i - 1);
            float lastX = i == 0 ? currentPointX : getX(i - 1);
            if (mMainDraw != null) {
                mMainDraw.drawTranslated(lastPoint, currentPoint, lastX, currentPointX, canvas, this, i);
            }
            if (mChildDraw != null) {
                mChildDraw.drawTranslated(lastPoint, currentPoint, lastX, currentPointX, canvas, this, i);
            }
        }
        //画选择线
        if (isLongPress()) {
            KLineImpl point = (KLineImpl) getItem(mSelectedIndex);
            float x = getX(mSelectedIndex);
            float y = getMainY(point.getClosePrice());
            canvas.drawLine(x, 0, x, mMainHeight, mTextPaint);
            canvas.drawLine(-mTranslateX, y, -mTranslateX + mWidth / mScaleX, y, mTextPaint);
            canvas.drawLine(x, mMainHeight + mMainChildSpace, x, mMainHeight + mMainChildSpace + mChildHeight, mTextPaint);
        }
        //还原 平移缩放
        canvas.restore();
    }

    /**
     * 画表格线
     * @param canvas
     */
    private void drawGrid(Canvas canvas) {
        //-------------------上方k线图------------------
        //横向的grid
        float rowSpace = mMainHeight / mGridRows;
        for (int i = 0; i <= mGridRows; i++) {
            canvas.drawLine(0, rowSpace * i, mWidth, rowSpace * i, mGridPaint);
        }
        //-------------------下方子图------------------
        canvas.drawLine(0, mMainHeight + mMainChildSpace, mWidth, mMainHeight + mMainChildSpace, mGridPaint);
        canvas.drawLine(0, mMainHeight + mMainChildSpace + mChildHeight, mWidth, mMainHeight + mMainChildSpace + mChildHeight, mGridPaint);
        //纵向的grid
        float columnSpace = mWidth / mGridColumns;
        for (int i = 0; i <= mGridColumns; i++) {
            canvas.drawLine(columnSpace * i, 0, columnSpace * i, mMainHeight, mGridPaint);
            canvas.drawLine(columnSpace * i, mMainHeight + mMainChildSpace, columnSpace * i, mMainHeight + mMainChildSpace + mChildHeight, mGridPaint);
        }
    }

    /**
     * 计算当前的显示区域
     */
    private void calculateValue() {
        if (!isLongPress()) {
            mSelectedIndex = -1;
        }
        mMainMaxValue = Float.MIN_VALUE;
        mMainMinValue = Float.MAX_VALUE;
        mChildMaxValue = Float.MAX_VALUE;
        mChildMinValue = Float.MIN_VALUE;
        mStartIndex = indexOfTranslateX(xToTranslateX(0));
        mStopIndex = indexOfTranslateX(xToTranslateX(mWidth));
        for (int i = mStartIndex; i <= mStopIndex; i++) {
            KLineImpl point = (KLineImpl) getItem(i);
            if (mMainDraw != null) {
                mMainMaxValue = Math.max(mMainMaxValue, mMainDraw.getMaxValue(point));
                mMainMinValue = Math.min(mMainMinValue, mMainDraw.getMinValue(point));
            }
            if (mChildDraw != null) {
                mChildMaxValue = Math.max(mChildMaxValue, mMainDraw.getMaxValue(point));
                mChildMinValue = Math.min(mChildMinValue, mChildDraw.getMinValue(point));
            }
        }
        float padding = (mMainMaxValue - mMainMinValue) * 0.05f;
        mMainMaxValue += padding;
        mMainMinValue -= padding;
        mMainScaleY = mMainHeight * 1f / (mMainMaxValue - mMainMinValue);
        mChildScaleY = mChildHeight * 1f / (mChildMaxValue - mChildMinValue);
        if (mAnimator.isRunning()) {
            float value = (float) mAnimator.getAnimatedValue();
            mStopIndex = mStartIndex + Math.round(value * (mStopIndex - mStartIndex));
        }
    }

    private int indexOfTranslateX(float translateX) {
        return indexOfTranslateX(translateX, 0, mItemCount - 1);
    }

    /**
     * 二分查找当前值的index
     * @param translateX
     * @param start
     * @param end
     * @return
     */
    private int indexOfTranslateX(float translateX, int start, int end) {
        if (start == end) {
            return start;
        }
        if (end - start == 1) {
            float startValue = getX(start);
            float endValue = getX(end);
            return Math.abs(translateX - startValue) < Math.abs(translateX - endValue) ? start : end;
        }
        int mid = start + (end - start) / 2;
        float midValue = getX(mid);
        if (translateX < midValue) {
            return indexOfTranslateX(translateX, start, mid);
        } else if (translateX > midValue) {
            return indexOfTranslateX(translateX, mid, end);
        } else {
            return mid;
        }
    }

    public void setTranslateXFromScrollX(int scrollX) {
        mTranslateX = scrollX + getMinTranslateX();
    }

    /**
     * 获取平移的最小值
     * @return
     */
    private int getMinTranslateX() {
        return -mDataLen + mWidth / mScaleX - mPointWidth / 2;
    }

    /**
     * 获取平移的最大值
     * @return
     */
    private float getMaxTranslateX() {
        if (!isFullScreen()) {
            return getMinTranslateX();
        }
        return mPointWidth / 2;
    }

    /**
     * 数据是否充满屏幕
     * @return
     */
    private boolean isFullScreen() {
        return mDataLen >= mWidth / mScaleX;
    }

    public void notifyChanged() {
        if (mItemCount != 0) {
            mXs.clear();
            mDataLen = (mItemCount - 1) * mPointWidth;
            for (int i = 0; i < mItemCount; i++) {
                float x = i * mPointWidth;
                mXs.add(x);
            }
            checkAndFixScrollX();
            setTranslateXFromScrollX(mScrollX);
        }else {setScrollX(0);}
        invalidate();
    }

    /**
     * 设置表格行数
     * @param gridRows
     */
    public void setGridRows(int gridRows) {
        if (gridRows < 1) {
            gridRows = 1;
        }
        mGridRows = gridRows;
        invalidate();
    }

    public void setGridColumns(int gridColumns) {
        if (gridColumns < 1) {
            gridColumns = 1;
        }
        mGridColumns = gridColumns;
        invalidate();
    }

    @Override
    public void setAdapter(IAdapter adapter) {
        if (mAdapter != null && mDataSetObserver != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }
        mAdapter = adapter;
        if (mAdapter != null) {
            mAdapter.registerDataSetObserver(mDataSetObserver);
            mItemCount = mAdapter.getCount();
        } else {
            mItemCount = 0;
        }
        notifyChanged();
    }

    @Override
    public IAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void addChildDraw(String name, @NonNull IChartDraw draw) {
        mChildDraws.add(draw);
        mKChartTabView.addTab(name);
    }

    /**
     * 设置主区域的IChartDraw
     * @param mainDraw
     */
    public void setMainDraw(IChartDraw mainDraw) {
        mMainDraw = mainDraw;
    }

    @Override
    public float getMainY(float value) {
        return (mMainMinValue - value) * mMainScaleY;
    }

    @Override
    public float getChildY(float value) {
        return (mChildMaxValue - value) * mChildScaleY + mMainHeight + mMainChildSpace;
    }

    @Override
    public void drawMainLine(Canvas canvas, Paint paint, float startX, float startValue, float stopX, float stopValue) {
        canvas.drawLine(startX, getMainY(startValue), stopX, getMainY(stopValue), paint);
    }

    @Override
    public void drawChildLine(Canvas canvas, Paint paint, float startX, float startValue, float stopX, float stopValue) {
        canvas.drawLine(startX, getChildY(startValue), stopX, getChildY(stopValue), paint);
    }

    @Override
    public Object getItem(int position) {
        if (mAdapter != null) {
            return mAdapter.getItem(position);
        }
        return null;
    }

    @Override
    public float getX(int position) {
        return mXs.get(position);
    }

    @Override
    public String formatValue(float value) {
        if (getValueFormatter() == null) {
            setValueFormatter(new ValueFormatter());
        }
        return getValueFormatter().format(value);
    }

    /**
     * 设置ValueFormatter
     * @param valueFormatter
     */
    private void setValueFormatter(IValueFormatter valueFormatter) {
        mValueFormatter = valueFormatter;
    }

    /**
     * 获取ValueFormatter
     * @return
     */
    private IValueFormatter getValueFormatter() {
        return mValueFormatter;
    }

    @Override
    public String formatDateTime(Date date) {
        if (getDateTimeFormatter() == null) {
            setDateTimeFormatter(new TimeFormatter());
        }
        return getDateTimeFormatter().format(date);
    }

    public void setDateTimeFormatter(IDateTimeFormatter timeFormatter) {
        mDateTimeFormatter = timeFormatter;
    }

    /**
     * 获取DatetimeFormatter
     * @return
     */
    private IDateTimeFormatter getDateTimeFormatter() {
        return mDateTimeFormatter;
    }

    @Override
    public void startAnimation() {
        if (mAnimator != null) {
            mAnimator.start();
        }
    }

    @Override
    public void setAnimationDuration(long duration) {
        if (mAnimator != null) {
            mAnimator.setDuration(duration);
        }
    }

    @Override
    public int getChartWidth() {
        return mWidth;
    }

    @Override
    public float getTopPadding() {
        return mTopPadding;
    }

    @Override
    public int getSelectedIndex() {
        return mSelectedIndex;
    }

    @Override
    public void setOnSelectedChangedListener(OnSelectedChangedListener l) {
        mOnSelectedChangedListener = l;
    }

    @Override
    public float xToTranslateX(float x) {
        return -mTranslateX + x / mScaleX;
    }

    @Override
    public float translateXToX(float translateX) {
        return (translateX + mTranslateX) * mScaleX;
    }

    @Override
    public void setOverScrollRange(float overScrollRange) {
        if (overScrollRange < 0) {
            overScrollRange = 0;
        }
        mOverScrollRange = overScrollRange;
    }

    @Override
    public void onSelectedChanged(IKChartView view, Object point, int index) {
        if (mOnSelectedChangedListener != null) {
            mOnSelectedChangedListener.onSelectedChanged(view, point, index);
        }
    }

    @Override
    public int getMinScrollX() {
        return (int) -(mOverScrollRange / mScaleX);
    }

    @Override
    public int getMaxScrollX() {
        return Math.round(getMaxTranslateX() - getMinTranslateX());
    }

}
