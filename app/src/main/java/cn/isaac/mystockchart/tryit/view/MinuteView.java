package cn.isaac.mystockchart.tryit.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.isaac.mystockchart.R;
import cn.isaac.mystockchart.tryit.entity.RealData;
import cn.isaac.mystockchart.tryit.entity.TrendData;
import cn.isaac.mystockchart.tryit.tools.CanvasTools;

/**
 * Created by RaoWei on 2017/6/19 9:40.
 */

public class MinuteView extends RelativeLayout {

    private int mGridRows;
    private int mMainChildSpace;
    private int mTopPadding;
    private int mBottomPadding;
    private int mMainHeight;
    private int mChildHeight;
    private int mWidth;
    private int mGridColumns;
    private TextPaint mTextPaint;
    private TrendData mTrendData;
    private float mUp_px;
    private float mPreclose_px;
    private float mDown_px;
    private Paint mPaint;
    private String mLast_px;
    private String mLast_avg;
    private float max_px;
    private float min_px;
    private float max_amount;
    private ArrayList<Float> MinuteAmounts;
    private ArrayList<Float> MinuteAmountsTrend;
    private int mHeight;
    private float mPointWidth;
    private List<List<String>> mList;
    private float mRowSpace;
    private float mColumnSpace;
    private int mTextSpace;
    private int mTrendColor;
    private int mGreenColor;
    private int mRedColor;
    private int mGrayColor;
    private int mTrendBackgroundColor;
    private int mMinuteAvgColor;
    private int mBlackColor;
    private int mAvgTextColor;
    private int mLeftPadding;
    private int mRightPadding;


    public MinuteView(@NonNull Context context) {
        super(context);
        init();
    }

    public MinuteView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MinuteView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        mGridRows = 4;
        mGridColumns = 4;
        mTopPadding = dp2px(15);
        mBottomPadding = dp2px(5);
        mLeftPadding = dp2px(4);
        mRightPadding = dp2px(0);
        mMainChildSpace = dp2px(20);
        mTextSpace = dp2px(8);

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        
        mTrendColor = Color.parseColor("#EF9089");
        mTrendBackgroundColor = Color.parseColor("#FCEAE9");
        mMinuteAvgColor = Color.parseColor("#DDC565");
        mGrayColor = Color.parseColor("#EEEEEE");
        mGreenColor = Color.parseColor("#008057");
        mRedColor = Color.parseColor("#ED3331");
        mBlackColor = Color.parseColor("#000000");
        mAvgTextColor = Color.parseColor("#D1B02D");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        int displayHeight = h - mTopPadding - mBottomPadding - mMainChildSpace;
        int displayWidth = w - mLeftPadding - mRightPadding;
        mMainHeight = (int) (displayHeight * 0.66f);
        mChildHeight = (int) (displayHeight * 0.33f);
        mPointWidth = displayWidth / (4*60f + 1);

        mRowSpace = mMainHeight / mGridRows;
        mColumnSpace = displayWidth / mGridColumns;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mTrendData == null) {
            return;
        }
        //画背景
        canvas.drawColor(Color.parseColor("#FFFFFF"));
        //计算画布大小
        calculateValue();
        //画分时线
        drawMinute(canvas);
        //画分时均线
        drawMinuteAvg(canvas);
        //画柱状图
        drawBusiness(canvas);
        //画网格
        drawGrid(canvas);
        //画文字
        drawText(canvas);
    }

    private void drawBusiness(Canvas canvas) {
        float lHight = mChildHeight /  max_amount;
        //float r = 1.5f;
        mPaint.setStrokeWidth(dp2px(1));
        for (int i = 1; i < mList.size(); i++) {
            if (MinuteAmountsTrend.get(i) >= 0) {
                mPaint.setColor(mRedColor);
            } else {
                mPaint.setColor(mGreenColor);
            }
            //canvas.drawRect(i * mPointWidth - r,  mMainChildSpace + mMainHeight + 2 * (mMainHeight / mGridRows)  - lHight * MinuteAmounts.get(i),i * mPointWidth + r , mMainChildSpace + mMainHeight + 2 * (mMainHeight / mGridRows), mLinePaint);
            canvas.drawLine(getChartX(i), getChartBusinessY(lHight * MinuteAmounts.get(i)), getChartX(i),  getChartBusinessY(0), mPaint);
        }
    }

    private void calculateValue() {

    }

    private void drawGrid(Canvas canvas) {
        mPaint.setColor(mGrayColor);
        mPaint.setStrokeWidth(1);
        //分时图背景网格
        //横线
        for (int i = 0; i <= mGridRows; i++) {
            canvas.drawLine(mLeftPadding, mRowSpace * i + mTopPadding, mWidth - mRightPadding, mRowSpace * i + mTopPadding, mPaint);
        }

        //竖线
        for (int i = 0; i <= mGridColumns; i++) {
            canvas.drawLine(getColumnX(i), mTopPadding, getColumnX(i), mMainHeight + mTopPadding, mPaint);
            canvas.drawLine(getColumnX(i), mMainChildSpace + mMainHeight + mTopPadding, getColumnX(i), mMainChildSpace + mMainHeight + mChildHeight + mTopPadding, mPaint);
        }
        //分时图下方柱状图网格
        //横线
        for (int i = 0; i <= 3; i++) {
            canvas.drawLine(mLeftPadding, mMainChildSpace + mMainHeight + i * mRowSpace + mTopPadding, mWidth-mRightPadding, mMainChildSpace + mMainHeight + i * mRowSpace + mTopPadding, mPaint);
        }
    }

    private void drawText(Canvas canvas) {
        mTextPaint.setColor(mAvgTextColor);
        mTextPaint.setTextSize(sp2px(12));
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        float baseLine = (textHeight - fm.bottom - fm.top) / 2;

        String text;
        text = "均价：" + mLast_avg;
        canvas.drawText(text, mLeftPadding, baseLine, mTextPaint);
        float width = mTextPaint.measureText(text) + mTextSpace + mLeftPadding;

        float v1 = Float.parseFloat(mLast_px) - mPreclose_px;
        if (v1 >= 0) {
            mTextPaint.setColor(mRedColor);
        } else {
            mTextPaint.setColor(mGreenColor);
        }

        text = "最新：" + mLast_px;
        canvas.drawText(text, width, baseLine, mTextPaint);
        width += mTextPaint.measureText(text) + mTextSpace;
        text = CanvasTools.leave2point(v1);
        canvas.drawText(text, width, baseLine, mTextPaint);
        width += mTextPaint.measureText(text) + mTextSpace;
        text =  CanvasTools.leave2point(v1/mPreclose_px * 100) + "%";
        canvas.drawText(text, width, baseLine, mTextPaint);

        //纵轴
        mTextPaint.setTextSize(sp2px(10));
        mTextPaint.setColor(mRedColor);

        text = CanvasTools.leave2point(mUp_px);
        canvas.drawText(text, mLeftPadding, textHeight + mTopPadding, mTextPaint);
        text = CanvasTools.leave2point((mUp_px - mPreclose_px) * 100f / mPreclose_px) + "%";
        width = mTextPaint.measureText(text);
        canvas.drawText(text, mWidth - width ,textHeight + mTopPadding, mTextPaint);

        text = String.valueOf(mPreclose_px);
        mTextPaint.setColor(mBlackColor);
        canvas.drawText(text, mLeftPadding, mTopPadding + mMainHeight / 2, mTextPaint);
        text = "0.00%";
        width = mTextPaint.measureText(text);
        canvas.drawText(text, mWidth - width, mTopPadding + mMainHeight / 2, mTextPaint);

        text = CanvasTools.leave2point(mDown_px);
        mTextPaint.setColor(mGreenColor);
        canvas.drawText(text, mLeftPadding, mTopPadding + mMainHeight, mTextPaint);
        text = CanvasTools.leave2point((mDown_px - mPreclose_px) * 100f / mPreclose_px) + "%";
        width = mTextPaint.measureText(text);
        canvas.drawText(text, mWidth - width, mTopPadding + mMainHeight, mTextPaint);

        mTextPaint.setColor(mBlackColor);
        text = "09:30";
        canvas.drawText(text, mLeftPadding, mTopPadding + mMainHeight + textHeight, mTextPaint);
        text = "11:30";
        width = mTextPaint.measureText(text);
        canvas.drawText(text, (mWidth - width) / 2, mTopPadding + mMainHeight + textHeight, mTextPaint);
        text = "15:00";
        width = mTextPaint.measureText(text);
        canvas.drawText(text, mWidth - width, mTopPadding + mMainHeight + textHeight, mTextPaint);

        text = "成交量：" + (int)(MinuteAmounts.get(MinuteAmounts.size() - 1)/100);
        mTextPaint.setColor(mBlackColor);
        canvas.drawText(text, mLeftPadding, mMainChildSpace + mMainHeight + mTopPadding*2 , mTextPaint);
    }

    private void drawMinute(Canvas canvas) {
        mPaint.setStrokeWidth(dp2px(1));
        mPaint.setColor(mTrendColor);
        Path path = new Path();
        float num0;
        float num1;
        path.moveTo(mLeftPadding, mMainHeight+mTopPadding);
        for (int i = 0; i < mList.size() - 1; i++) {
            num0 = Float.parseFloat(mList.get(i).get(1));
            num1 = Float.parseFloat(mList.get(i + 1).get(1));

            path.lineTo(getChartX(i), getChartMinuteY(num0));
            canvas.drawLine(getChartX(i), getChartMinuteY(num0), getChartX(i + 1), getChartMinuteY(num1), mPaint);
            if (i == mList.size() - 2) {
                mLast_px = mList.get(i + 1).get(1);
                mLast_avg = mList.get(i + 1).get(2);
            }
        }
        path.lineTo(getChartX(mList.size() - 1), getChartMinuteY(Float.parseFloat(mList.get(mList.size() - 1).get(1))));
        path.lineTo(getChartX(mList.size() - 1), mMainHeight+mTopPadding);
        path.close();
        mPaint.setColor(mTrendBackgroundColor);
        canvas.drawPath(path, mPaint);
    }

    private void drawMinuteAvg(Canvas canvas) {
        mPaint.setColor(mMinuteAvgColor);
        for (int i = 0; i < mList.size() - 1; i++) {
            float num0 = Float.parseFloat(mList.get(i).get(2));
            float num1 = Float.parseFloat(mList.get(i + 1).get(2));

            canvas.drawLine(getChartX(i), getChartMinuteY(num0), getChartX(i + 1), getChartMinuteY(num1), mPaint);
        }
    }

    public void setTrendData(TrendData data) {
        mTrendData = data;
        int size = mTrendData.getData().getTrend().getProd_code().size();
        if (size <= 1) {
            Toast.makeText(getContext(), "暂时没有分时数据", Toast.LENGTH_LONG).show();
            return;
        }

        handleData();
        invalidate();
    }

    private void handleData() {
        mList = mTrendData.getData().getTrend().getProd_code();

        float max ;
        float min ;
        float amount0;
        float amount1;
        float amount;
        float lastAmount = 0;
        max = min = Float.parseFloat(mList.get(0).get(1));
        ArrayList<Float> amounts = new ArrayList<>();
        ArrayList<Float> amountsTrend = new ArrayList<>();

        amounts.add(0f);
        amountsTrend.add(0f);
        for (int i = 1; i < mList.size(); i++) {
            float v0 = Float.parseFloat(mList.get(i - 1).get(1));
            float v1 = Float.parseFloat(mList.get(i).get(1));
            amount0 = Float.parseFloat(mList.get(i - 1).get(3));
            amount1 = Float.parseFloat(mList.get(i).get(3));
            amount = amount1 - amount0;
            amounts.add(amount);
            amountsTrend.add(v1 - v0);
            if (amount > lastAmount) {
                lastAmount = amount;
            }
            if (v1 > max) {
                max = v1;
            }
            if (v1 < min) {
                min = v1;
            }
        }
        max_px = max;
        min_px = min;
        MinuteAmounts = amounts;
        MinuteAmountsTrend = amountsTrend;
        max_amount = lastAmount;

        mPreclose_px = (float) mTrendData.getData().getTrend().getPreclose_px().getProd_code();
        float gap = Math.max(Math.abs(max_px - mPreclose_px), Math.abs(min_px - mPreclose_px));
        mDown_px = mPreclose_px - gap;
        mUp_px = mPreclose_px + gap;
    }

    private float getColumnX(float i) {
        return mColumnSpace * i + mLeftPadding;
    }

    private float getChartX(float i) {
        return mPointWidth * i + mLeftPadding;
    }

    private float getChartMinuteY(float y) {
        return (1 - (y - mDown_px) / (mUp_px - mDown_px)) * mMainHeight + mTopPadding;
    }

    private float getChartBusinessY(float amount) {
        return mMainChildSpace + mMainHeight + 2 * (mMainHeight / mGridRows) + mTopPadding  - amount;
    }

    private int dp2px(float dp) {
        return (int)(dp * getContext().getResources().getDisplayMetrics().density + 0.5f);
    }

    private int sp2px(float sp) {
        return (int)(sp * getContext().getResources().getDisplayMetrics().scaledDensity + 0.5f);
    }
}
