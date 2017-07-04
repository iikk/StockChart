package cn.isaac.mystockchart.tryit.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Collections;

import cn.isaac.mystockchart.tryit.entity.KlineData;
import cn.isaac.mystockchart.tryit.tools.CanvasTools;

/**
 * Created by RaoWei on 2017/6/20 14:21.
 */

public class KlineView extends RelativeLayout{
    private int mHeight;
    private int mWidth;
    private int mTopPadding;
    private int mMainChildSpace;
    private int mBottomPadding;
    private float mMainHeight;
    private float mChildHeight;
    private TextPaint mTextPaint;
    private Paint mPaint;
    private KlineData mKlineData;
    private int mGridColumn;
    private int mGridRow;
    private ArrayList<ArrayList<Float>> mList;
    private ArrayList<ArrayList<Float>> mNewList;
    private float mHorizontalSpace;
    private float mMaxHight;
    private float mMinLow;
    private float mMaxBusinessAmount;
    private ArrayList<Float> mRiseFall;
    private Float mMin_time;
    private Float mMax_time;
    private int pillarGreenR;
    private int pillarRedR;
    private ArrayList<ArrayList<Float>> mFloatList;
    private float mCandleMaxMin;
    private ArrayList<Float> mMa5List;
    private ArrayList<Float> mMa10List;
    private ArrayList<Float> mMa20List;
    private int ma20Color;
    private int ma10Color;
    private int ma5Color;
    private int ma60Color;
    private ArrayList<Float> mAmountList;
    private ArrayList<Float> mAmountMa5List;
    private ArrayList<Float> mAmountMa10List;
    private float mMaxAmount;
    private int mRiseColor;
    private int mFallColor;
    private ArrayList<Float> mMa60List;
    private int mLeftPadding;
    private int mRightPadding;
    private float mRowSpace;
    private float mColumnSpace;
    private int blackColor;
    private float horizontalTextSpace;
    private int maStart;
    private int mDefaultCount;
    private boolean mAllowMaStartNot0;
    private int[] mMaStartGap;

    public KlineView(Context context) {
        super(context);
        init();
    }

    public KlineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KlineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        mDefaultCount = 80;
        mGridColumn = 4;
        mGridRow = 4;
        pillarRedR = 7;
        pillarGreenR = 8;
        mTopPadding = dp2px(18);
        mBottomPadding = dp2px(0);
        mLeftPadding = dp2px(4);
        mRightPadding = dp2px(4);
        mMainChildSpace = dp2px(18);
        horizontalTextSpace = dp2px(10);

        blackColor = Color.parseColor("#000000");
        ma5Color = Color.parseColor("#939393");
        ma10Color = Color.parseColor("#D1B02D");
        ma20Color = Color.parseColor("#FD63A0");
        ma60Color = Color.parseColor("#62A991");
        mFallColor = Color.parseColor("#008000");
        mRiseColor = Color.parseColor("#FF0000");

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setStyle(Paint.Style.FILL);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(dp2px(1));

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
        mWidth = w;
        int displayHeight = h - mTopPadding - mMainChildSpace - mBottomPadding;
        int displayWidth = w -mLeftPadding-mRightPadding;
        mMainHeight = displayHeight * 0.66f;
        mChildHeight = displayHeight * 0.33f;

        mRowSpace = mMainHeight / mGridRow;
        mColumnSpace = displayWidth / mGridColumn;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mKlineData == null || mKlineData.getData().getCandle().getProd_code().size() < 2) {
            //Toast.makeText(getContext(), "数据为空", Toast.LENGTH_LONG).show();
            return;
        }
        canvas.drawColor(Color.WHITE);
        //数据计算，整理
        calculateValue();
        //画蜡烛图
        drawCandle(canvas);
        //画ma线
        drawMainMa(canvas);
        //画柱状图
        drawChildPillar(canvas);
        //画子图ma线
        drawChildMa(canvas);
        //画背景表格
        drawGrid(canvas);
        //画文字
        drawText(canvas);
    }

    private void calculateValue() {

        mMaxAmount = 0;
        mHorizontalSpace = (mWidth-mLeftPadding-mRightPadding)*1.0f / (60);
        mMin_time = mNewList.get(0).get(0);
        mMax_time = mNewList.get(mNewList.size() - 1).get(0);
        float open_px;
        float close_px;
        float high_px;
        float low_px;
        float amount;
        mMaxHight = mNewList.get(0).get(2);
        mMinLow = mNewList.get(0).get(3);
        mMaxBusinessAmount = mNewList.get(0).get(4);
        if (mRiseFall == null) {
            mRiseFall = new ArrayList<>();
        } else {
            mRiseFall.clear();
        }
        if (mFloatList == null) {
            mFloatList = new ArrayList<>();
        } else {
            mFloatList.clear();
        }
        if (mAmountList == null) {
            mAmountList = new ArrayList<>();
        } else {
            mAmountList.clear();
        }
        if (mAmountMa5List == null) {
            mAmountMa5List = new ArrayList<>();
        } else {
            mAmountMa5List.clear();
        }
        if (mAmountMa10List == null) {
            mAmountMa10List = new ArrayList<>();
        } else {
            mAmountMa10List.clear();
        }
        if (mMaStartGap == null) {
            mMaStartGap = new int[3];
        } else {
            mMaStartGap[0] = 0;
            mMaStartGap[1] = 0;
            mMaStartGap[2] = 0;
        }
        Float ma5;
        Float ma10;
        Float ma20;
        Float ma60;
        Float amountMa5;
        Float amountMa10;
        if (mMa5List == null) {
            mMa5List = new ArrayList<>();
        } else {
            mMa5List.clear();
        }
        if (mMa10List == null) {
            mMa10List = new ArrayList<>();
        } else {
            mMa10List.clear();
        }
        if (mMa20List == null) {
            mMa20List = new ArrayList<>();
        } else {
            mMa20List.clear();
        }
        if (mMa60List == null) {
            mMa60List = new ArrayList<>();
        } else {
            mMa60List.clear();
        }
        for (int i = 0; i < mNewList.size(); i++) {
            int oldIndex = mList.size() - mNewList.size() + i;
            ArrayList<Float> item = mList.get(oldIndex);
            ArrayList<Float> floats = new ArrayList<>();
            open_px = item.get(1);
            close_px = item.get(4);
            high_px = item.get(2);
            low_px = item.get(3);
            floats.add(open_px);
            floats.add(close_px);
            floats.add(high_px);
            floats.add(low_px);
            mFloatList.add(floats);
            mRiseFall.add(close_px - open_px);
            decideMaxheigh(high_px);
            decideMinLow(low_px);

            if (mAllowMaStartNot0) {
                if (oldIndex==5) {
                    mMaStartGap[0] = i;
                }
                if (oldIndex==10) {
                    mMaStartGap[1] = i;
                }
                if (oldIndex==20) {
                    mMaStartGap[2] = i;
                }
            }
            if (oldIndex >= 5) {
                ma5 = 0f;
                amountMa5 = 0f;
                for (int j = oldIndex; j > oldIndex - 5; j--) {
                    ma5 += mList.get(j).get(4);
                    amountMa5 += mList.get(j).get(5);
                }
                mMa5List.add(ma5 / 5f);
                mAmountMa5List.add(amountMa5/5);
            }

            if (oldIndex >= 10) {
                ma10 = 0f;
                amountMa10 = 0f;
                for (int j = oldIndex; j > oldIndex - 10; j--) {
                    ma10 += mList.get(j).get(4);
                    amountMa10 += mList.get(j).get(5);
                }
                mMa10List.add(ma10 / 10f);
                mAmountMa10List.add(amountMa10/10);
            }

            if (oldIndex >= 20) {
                ma20 = 0f;
                for (int j = oldIndex; j > oldIndex - 20; j--) {
                    ma20 += mList.get(j).get(4);
                }
                mMa20List.add(ma20 / 20f);
            }

            //if (oldIndex >= 60) {
            //    ma60 = 0f;
            //    for (int j = oldIndex; j > oldIndex - 60; j--) {
            //        ma60 += mList.get(j).get(4);
            //    }
            //    mMa60List.add(ma60 / 60f);
            //}

            mAmountList.add(mNewList.get(i).get(5));
            if (mMaxAmount < mNewList.get(i).get(5)) {
                mMaxAmount = mNewList.get(i).get(5);
            }
        }

        mCandleMaxMin = mMaxHight - mMinLow;

    }

    private void decideMaxheigh(float high) {
        if (mMaxHight < high) {
            mMaxHight = high;
        }
    }

    private void decideMinLow(float low) {
        if (mMinLow > low) {
            mMinLow = low;
        }
    }

    private float getChartX(float i) {
        return mLeftPadding+mHorizontalSpace *i+mHorizontalSpace/2;
    }
    private float getChartY(float y) {
        return mMainChildSpace + mMainHeight+(1- y / mMaxAmount) * mChildHeight + mTopPadding;
    }
    private float getMainY(float y) {
        return (1-(y - mMinLow)/mCandleMaxMin)*mMainHeight + mTopPadding;
    }

    private float getGridX(float i) {
        return mLeftPadding + i * mColumnSpace;
    }

    private void drawCandle(Canvas canvas) {
        mPaint.setStrokeWidth(dp2px(1));
        for (int i = 0; i < mNewList.size(); i++) {
            float lineTop = getMainY(mFloatList.get(i).get(2));
            float lineBottom = getMainY(mFloatList.get(i).get(3));
            float close = getMainY(mFloatList.get(i).get(1));
            float open = getMainY(mFloatList.get(i).get(0));
            if (mRiseFall.get(i) >= 0) {
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(mRiseColor);
                canvas.drawRect(getChartX(i)-pillarRedR, close, getChartX(i)+pillarRedR, open,mPaint);
                if (lineTop<close) {
                canvas.drawLine(getChartX(i), lineTop, getChartX(i), close, mPaint);
                }
                if (lineBottom > open) {
                canvas.drawLine(getChartX(i), open, getChartX(i), lineBottom, mPaint);
                }
            } else {
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setColor(mFallColor);
                canvas.drawLine(getChartX(i), lineTop, getChartX(i), lineBottom, mPaint);
                canvas.drawRect(getChartX(i)-pillarGreenR,open, getChartX(i)+pillarGreenR,close,mPaint);
            }
        }
    }

    private void drawMainMa(Canvas canvas) {
        canvas.save();
        canvas.clipRect(mLeftPadding, mTopPadding, mWidth - mRightPadding, mMainHeight + mTopPadding);  //只对之后的drawXXX有作用，已经画出来的不受影响
        for (int i = 0; i < mMa5List.size() - 1; i++) {
            mPaint.setColor(ma5Color);
            canvas.drawLine(getChartX(mAllowMaStartNot0?i+mMaStartGap[0]:i), getMainY(mMa5List.get(i)), getChartX(mAllowMaStartNot0?i+1+mMaStartGap[0]:i+1), getMainY(mMa5List.get(i+1)), mPaint);
        }
        for (int i = 0; i < mMa10List.size() - 1; i++) {
            mPaint.setColor(ma10Color);
            canvas.drawLine(getChartX(mAllowMaStartNot0?i+mMaStartGap[1]:i), getMainY(mMa10List.get(i)), getChartX(mAllowMaStartNot0?i+1+mMaStartGap[1]:i+1), getMainY(mMa10List.get(i+1)), mPaint);
        }
        for (int i = 0; i < mMa20List.size() - 1; i++) {
            mPaint.setColor(ma20Color);
            canvas.drawLine(getChartX(mAllowMaStartNot0?i+mMaStartGap[2]:i), getMainY(mMa20List.get(i)), getChartX(mAllowMaStartNot0?i+1+mMaStartGap[2]:i+1), getMainY(mMa20List.get(i+1)), mPaint);
        }
        //for (int i = 0; i < mMa60List.size() - 1; i++) {
        //    mPaint.setColor(ma60Color);
        //    canvas.drawLine(getChartX(i), (1-(mMa60List.get(i) - mMinLow)/mCandleMaxMin)*mMainHeight, getChartX(i + 1), (1-(mMa60List.get(i + 1) - mMinLow)/mCandleMaxMin)*mMainHeight, mPaint);
        //}
        canvas.restore();
    }

    private void drawChildPillar(Canvas canvas) {
        for (int i = 0; i < mAmountList.size(); i++) {
            if (mRiseFall.get(i) > 0) {
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(mRiseColor);
            canvas.drawRect(getChartX(i) - pillarRedR, getChartY(mAmountList.get(i)), getChartX(i) + pillarRedR, getChartY(0), mPaint);
            } else {
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setColor(mFallColor);
            canvas.drawRect(getChartX(i) - pillarGreenR, getChartY(mAmountList.get(i)), getChartX(i) + pillarGreenR, getChartY(0), mPaint);
            }
        }
    }

    private void drawChildMa(Canvas canvas) {
        canvas.save();
        canvas.clipRect(mLeftPadding,mTopPadding+mMainHeight+mMainChildSpace,mWidth-mRightPadding,mHeight-mBottomPadding);
        for (int i = 0; i < mAmountMa5List.size() - 1; i++) {
            mPaint.setColor(ma5Color);
            canvas.drawLine(getChartX(mAllowMaStartNot0?i+mMaStartGap[0]:i), getChartY(mAmountMa5List.get(i)), getChartX(mAllowMaStartNot0?i+1+mMaStartGap[0]:i+1), getChartY(mAmountMa5List.get(i + 1)), mPaint);
        }
        for (int i = 0; i < mAmountMa10List.size() - 1; i++) {
            mPaint.setColor(ma10Color);
            canvas.drawLine(getChartX(mAllowMaStartNot0?i+mMaStartGap[1]:i), getChartY(mAmountMa10List.get(i)), getChartX(mAllowMaStartNot0?i+1+mMaStartGap[1]:i+1), getChartY(mAmountMa10List.get(i + 1)), mPaint);
        }
        canvas.restore();
    }

    private void drawGrid(Canvas canvas) {

        mPaint.setColor(Color.parseColor("#D7D7D7"));
        mPaint.setStrokeWidth(1);
        //画横线
        for (int i = 0; i <= mGridRow; i++) {
            canvas.drawLine(mLeftPadding, mRowSpace * i + mTopPadding, mWidth - mRightPadding, mRowSpace * i + mTopPadding, mPaint);
        }
        for (int i = 0; i <= 2; i++) {
            canvas.drawLine(mLeftPadding, mRowSpace * i + mMainHeight + mMainChildSpace + mTopPadding, mWidth - mRightPadding, mRowSpace * i + mMainHeight + mMainChildSpace + mTopPadding, mPaint);
        }
        //画竖线
        for (int i = 0; i <= mGridColumn; i++) {
            canvas.drawLine(getGridX(i), mTopPadding, getGridX(i), mMainHeight + mTopPadding, mPaint);
            canvas.drawLine(getGridX(i), mMainHeight + mMainChildSpace + mTopPadding, getGridX(i), mMainHeight + mChildHeight + mMainChildSpace + mTopPadding , mPaint);
        }

    }

    private void drawText(Canvas canvas) {
        mTextPaint.setTextSize(dp2px(13));
        //画蜡烛图ma值
        String text = "";
        float textWidth = 0;
        if (mMa5List.size()>0) {
            text = "MA5:" + CanvasTools.leave2point(mMa5List.get(mMa5List.size() - 1));
            mTextPaint.setColor(ma5Color);
            canvas.drawText(text ,mLeftPadding, mTopPadding-5, mTextPaint);
        }

        if (mMa10List.size()>0) {
            textWidth = mTextPaint.measureText(text) + horizontalTextSpace;
            text = "10:" + CanvasTools.leave2point(mMa10List.get(mMa10List.size() - 1));
            mTextPaint.setColor(ma10Color);
            canvas.drawText(text, textWidth, mTopPadding-5, mTextPaint);
        }

        if (mMa20List.size()>0) {
            textWidth += mTextPaint.measureText(text) + horizontalTextSpace;
            text = "20:" + CanvasTools.leave2point(mMa20List.get(mMa20List.size() - 1));
            mTextPaint.setColor(ma20Color);
            canvas.drawText(text, textWidth, mTopPadding-5, mTextPaint);
        }

        //画纵坐标
        mTextPaint.setTextSize(dp2px(10));
        mTextPaint.setColor(blackColor);
        text = mMaxHight+ "";
        canvas.drawText(text, mLeftPadding, getBaseLine(mTextPaint)+mTopPadding, mTextPaint);
        text = (mMaxHight+mMinLow)/2+ "";
        canvas.drawText(text, mLeftPadding,  mMainHeight/2+mTopPadding, mTextPaint);
        text = mMinLow+ "";
        canvas.drawText(text, mLeftPadding,  mMainHeight+mTopPadding, mTextPaint);

        //画横轴
        text = CanvasTools.formateTime(mNewList.get(0).get(0));
        canvas.drawText(text, mLeftPadding, mMainHeight + getBaseLine(mTextPaint) + 5 + mTopPadding, mTextPaint);
        text = CanvasTools.formateTime(mNewList.get(mNewList.size()-1).get(0));
        float v = mTextPaint.measureText(text);
        canvas.drawText(text, mWidth - v - mRightPadding, mMainHeight + getBaseLine(mTextPaint) + 5 + mTopPadding, mTextPaint);

        //成交量
        mTextPaint.setColor(blackColor);
        text = "成交量：" + CanvasTools.leave2point(mAmountList.get(mAmountList.size()-1)/1000000) + "万";
        canvas.drawText(text, mLeftPadding, mMainHeight+ mMainChildSpace + mTopPadding*2, mTextPaint);

        if (mAmountMa5List.size()>0) {
            mTextPaint.setColor(ma5Color);
            v = mTextPaint.measureText(text) + horizontalTextSpace;
            text = "MA5:" + CanvasTools.leave2point(mAmountMa5List.get(mAmountMa5List.size()-1)/1000000) + "万";
            canvas.drawText(text, v, mMainHeight + mMainChildSpace + mTopPadding*2, mTextPaint);
        }

        if (mAmountMa10List.size()>0) {
            mTextPaint.setColor(ma10Color);
            v += mTextPaint.measureText(text) + horizontalTextSpace;
            text = "MA10:"+CanvasTools.leave2point(mAmountMa10List.get(mAmountMa10List.size()-1)/1000000) + "万";
            canvas.drawText(text, v, mMainHeight + mMainChildSpace + mTopPadding*2, mTextPaint);
        }
    }

    public void setKlineData(KlineData data) {
        mKlineData = data;
        mList = mKlineData.getData().getCandle().getProd_code();
        if (mList.size() >= 120) {
            //maStart = 60;
        } else if (mList.size() >= 40) {
            maStart = 20;
        } else if (mList.size() >= 20) {
            maStart = 10;
        } else if (mList.size() >= 10) {
            maStart = 5;
        } else {
            maStart = 0;
        }
        if (mNewList == null) {
            mNewList = new ArrayList<>();
        } else {
            mNewList.clear();
        }
        if (mList.size() < mDefaultCount) {
            mAllowMaStartNot0 = true;

        } else {
            mAllowMaStartNot0 = false;

        }
        for (int i = mList.size()-1; i >= 0; i--) {
            if (mNewList.size()>=60 || i <(mAllowMaStartNot0?0:maStart-1)) {
                break;
            }
            mNewList.add(mList.get(i));
        }
        Collections.reverse(mNewList);
        invalidate();
    }

    private float getBaseLine(TextPaint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (fm.descent - fm.ascent - fm.bottom - fm.top) / 2;
    }

    private int dp2px(float dp) {
        return (int)(dp * getContext().getResources().getDisplayMetrics().density + 0.5f);
    }

}
