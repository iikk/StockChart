package cn.isaac.mystockchart.tryit.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewDebug;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.isaac.mystockchart.R;
import cn.isaac.mystockchart.tryit.entity.RealData;
import cn.isaac.mystockchart.tryit.tools.CanvasTools;

/**
 * Created by RaoWei on 2017/6/22 15:51.
 */

public class ChartHeadView extends RelativeLayout {
    private int mHigh;
    private int mWidth;
    private float mTopPadding;
    private float mBottomPadding;
    private float mLeftPadding;
    private float mRightPadding;
    private float mStockNameHigh;
    private float mY;
    private Paint mPaint;
    private TextPaint mTextPaint;
    private int mTextSize;
    private String text;
    private List<String> mList;
    private int mGrayColor;
    private int mBlackColor;
    private int mGreenColor;
    private int mGrayTextColor;
    private int mRedColor;

    public ChartHeadView(Context context) {
        super(context);
        init();
    }

    public ChartHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChartHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        mTopPadding = px(10);
        mBottomPadding = px(10);
        mLeftPadding = px(10);
        mRightPadding = px(10);

        mTextSize = sp(15);

        mY = 0;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mGrayColor = Color.parseColor("#DDDDDD");
        mRedColor = Color.parseColor("#ED3331");
        mBlackColor = Color.parseColor("#333333");
        mGreenColor = Color.parseColor("#008000");
        mGrayTextColor = Color.parseColor("#8F8E8E");

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHigh = h;
        mWidth = w;
        float dispayHeigh = h - mTopPadding - mBottomPadding;
        float dispayWidth = w - mLeftPadding - mRightPadding;
        mStockNameHigh = dispayHeigh *2 /7;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int statusBarHeight = getStatusBarHeight();
        Log.e("status", "statusBarHeight=" + statusBarHeight);
        if (mList == null || mList.size() == 0) {
            return;
        }
        canvas.drawColor(getResources().getColor(R.color.chart_white));
        //canvas.translate(0, statusBarHeight);
        //计算参数
        calualateValue(canvas);
        //画股票名
        drawName(canvas);
        //画分割线
        drawLine(canvas);
        //画价格
        drawLastpx(canvas);
    }

    private void calualateValue(Canvas canvas) {
        mY = 0;
    }

    private void drawName(Canvas canvas) {
        text = "南国置业";
        mTextPaint.setColor(getColor(R.color.black));
        mTextPaint.setTextSize(sp(20));
        mPaint.setFakeBoldText(true);
        canvas.drawText(text,mLeftPadding ,0 + getBaseLine(mTextPaint) + mTopPadding, mTextPaint);

        mY += getTextHeigh(mTextPaint) + mTopPadding;

        float x = mTextPaint.measureText(text);
        text = "(002305.SZ)";
        mPaint.setFakeBoldText(false);
        mPaint.setColor(mBlackColor);
        canvas.drawText(text, x + mLeftPadding, 0 + getBaseLine(mTextPaint) + mTopPadding, mTextPaint);
    }

    private void drawLine(Canvas canvas) {
        float topPadding = px(8);
        float bottomPadding = px(8);
        float leftPadding = px(10);
        float rightPadding = px(10);
        mPaint.setColor(mGrayColor);
        canvas.drawLine(leftPadding,mY + topPadding,mWidth - rightPadding, mY + topPadding, mPaint);
        mY += topPadding + bottomPadding;
    }

    private void drawLastpx(Canvas canvas) {
        float top = mY;
        float padding0 = px(15);
        float gapVertical = px(8);
        boolean isRise = Float.parseFloat(mList.get(22)) < 0 ? false : true;
        text = mList.get(21);
        mTextPaint.setColor(isRise?mRedColor:mGreenColor);
        mTextPaint.setTextSize(sp(30));
        canvas.drawText(text, mLeftPadding, mY + getBaseLine(mTextPaint), mTextPaint);
        mY += getTextHeigh(mTextPaint);

        text = mList.get(22) + "  " + mList.get(23)+"%";
        mTextPaint.setTextSize(sp(15));
        canvas.drawText(text, mLeftPadding, mY + getBaseLine(mTextPaint), mTextPaint);
        mY += getTextHeigh(mTextPaint);

        float width0 = mTextPaint.measureText(text);
        float itemWidth = (mWidth - mLeftPadding - mRightPadding - width0 - padding0) / 3;

        mTextPaint.setTextSize(sp(13));
        float topPadding = (mY - top - 2 * getTextHeigh(mTextPaint) - gapVertical) / 2;
        mTextPaint.setColor(mGrayTextColor);

        float textBaseLine0 = top + topPadding + getBaseLine(mTextPaint);

        text = "开 ";
        float text0 = mTextPaint.measureText(text);
        canvas.drawText(text,mLeftPadding + width0 + padding0, textBaseLine0, mTextPaint);

        text = "额 ";
        float text1 = mTextPaint.measureText(text);
        canvas.drawText(text,mLeftPadding + width0 + padding0 + itemWidth, textBaseLine0, mTextPaint);

        text = "换手 ";
        float text2 = mTextPaint.measureText(text);
        canvas.drawText(text,mLeftPadding + width0 + padding0 + 2*itemWidth, textBaseLine0, mTextPaint);

        float textBaseLine1 = textBaseLine0 + getBaseLine(mTextPaint) + gapVertical;
        text = "高 ";
        canvas.drawText(text,mLeftPadding + width0 + padding0, textBaseLine1, mTextPaint);

        text = "低 ";
        canvas.drawText(text,mLeftPadding + width0 + padding0 + itemWidth, textBaseLine1, mTextPaint);

        text = "量比 ";
        canvas.drawText(text,mLeftPadding + width0 + padding0 + 2*itemWidth, textBaseLine1, mTextPaint);

        text = mList.get(2);
        mTextPaint.setColor(Float.parseFloat(mList.get(2)) - Float.parseFloat(mList.get(8)) <0?mGreenColor:mRedColor);
        canvas.drawText(text, mLeftPadding + width0 + padding0 + text0, textBaseLine0, mTextPaint);

        text = CanvasTools.formateNum(mList.get(4));
        mTextPaint.setColor(mBlackColor);
        canvas.drawText(text, mLeftPadding + width0 + padding0 + text1 + itemWidth, textBaseLine0, mTextPaint);

        text = mList.get(6)+"%";
        mTextPaint.setColor(mBlackColor);
        canvas.drawText(text, mLeftPadding + width0 + padding0 + text2 + itemWidth*2, textBaseLine0, mTextPaint);

        text = mList.get(3);
        mTextPaint.setColor(mRedColor);
        canvas.drawText(text, mLeftPadding + width0 + padding0 + text0, textBaseLine1, mTextPaint);

        text = mList.get(5);
        mTextPaint.setColor(mGreenColor);
        canvas.drawText(text, mLeftPadding + width0 + padding0 + text1 + itemWidth, textBaseLine1, mTextPaint);

        text = mList.get(7);
        mTextPaint.setColor(mBlackColor);
        canvas.drawText(text, mLeftPadding + width0 + padding0 + text2 + itemWidth *2, textBaseLine1, mTextPaint);
    }

    public void setHeadData(RealData data) {
        if (data!=null) {
            mList = data.getData().getSnapshot().getProd_code();
            invalidate();
        }
    }

    /**
     * 获取系统状态栏高度
     * @return
     */
    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId>0) {
            result = getContext().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
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
