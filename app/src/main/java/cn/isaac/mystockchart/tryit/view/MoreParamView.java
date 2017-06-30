package cn.isaac.mystockchart.tryit.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.FloatProperty;
import android.widget.RelativeLayout;

import java.util.List;

import cn.isaac.mystockchart.R;
import cn.isaac.mystockchart.tryit.entity.RealData;
import cn.isaac.mystockchart.tryit.tools.CanvasTools;

/**
 * Created by RaoWei on 2017/6/26 11:50.
 */

public class MoreParamView extends RelativeLayout{

    private TextPaint mTextPaint;
    private int mTopPadding;
    private int mBottomPadding;
    private int mLeftPadding;
    private int mRightPadding;
    private int mVerticalSpace;
    private int mWidth;
    private int mHeigh;
    private int mVerticalGap;
    private String mText;
    private int mItemWidth;
    private float mBaseLine0;
    private float mBaseLine1;
    private float mBaseLine2;
    private float mBaseLine3;
    private float mBaseLine4;
    private float mBaseLine5;
    private int mText0X;
    private int mText1X;
    private int mText2X;
    private int mText3X;
    private int mBlack;
    private int mGreen;
    private int mRed;
    private List<String> mList;

    public MoreParamView(Context context) {
        super(context);
        init();
    }

    public MoreParamView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MoreParamView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        mTopPadding = px(10);
        mBottomPadding = px(10);
        mLeftPadding = px(40);
        mRightPadding = px(0);
        mVerticalSpace = px(20);
        mVerticalGap = px(6);

        mGreen = Color.parseColor("#008000");
        mBlack = Color.parseColor("#333333");
        mRed = Color.parseColor("#EF3331");


        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(sp(15));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeigh = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mList==null) {
            return;
        }
        canvas.drawColor(getResources().getColor(R.color.chart_white));
        calculateValue();
        drawKey(canvas);
        drawValue(canvas);
    }

    private void drawValue(Canvas canvas) {
        mTextPaint.setColor(mBlack);
        mText = mList.get(8);
        canvas.drawText(mText, mText0X, mBaseLine1, mTextPaint);

        mTextPaint.setColor(mGreen);
        mText = CanvasTools.formateNum100(mList.get(11));
        canvas.drawText(mText, mText1X, mBaseLine1, mTextPaint);

        mTextPaint.setColor(mBlack );
        mText = CanvasTools.formateNum(mList.get(14));
        canvas.drawText(mText, mText2X, mBaseLine1, mTextPaint);

        mTextPaint.setColor(mRed );
        mText = mList.get(16);
        canvas.drawText(mText, mText3X, mBaseLine1, mTextPaint);

        mTextPaint.setColor(mBlack );
        mText = mList.get(9)+"%";
        canvas.drawText(mText, mText0X, mBaseLine3, mTextPaint);

        mTextPaint.setColor(mRed );
        mText = CanvasTools.formateNum100(mList.get(12));
        canvas.drawText(mText, mText1X, mBaseLine3, mTextPaint);

        mTextPaint.setColor(mBlack );
        mText = CanvasTools.formateNum(mList.get(15));
        canvas.drawText(mText, mText2X, mBaseLine3, mTextPaint);

        mTextPaint.setColor(mGreen );
        mText = mList.get(17);
        canvas.drawText(mText, mText3X, mBaseLine3, mTextPaint);

        mTextPaint.setColor(mBlack );
        mText = mList.get(10);
        canvas.drawText(mText, mText0X, mBaseLine5, mTextPaint);

        mTextPaint.setColor(Float.parseFloat(mList.get(24))<0?mGreen:mRed);
        mText = mList.get(24)+"%";
        canvas.drawText(mText, mText1X, mBaseLine5, mTextPaint);

        mTextPaint.setColor(mBlack );
        mText = mList.get(25);
        canvas.drawText(mText, mText2X, mBaseLine5, mTextPaint);

        mTextPaint.setColor(mBlack );
        mText = mList.get(18);
        canvas.drawText(mText, mText3X, mBaseLine5, mTextPaint);
    }

    private void drawKey(Canvas canvas) {
        mTextPaint.setColor(Color.parseColor("#8F8E8E"));
        mText = "昨收";
        canvas.drawText(mText, mText0X, mBaseLine0, mTextPaint);

        mText = "内盘";
        canvas.drawText(mText, mText1X, mBaseLine0, mTextPaint);

        mText = "流通市值";
        canvas.drawText(mText, mText2X, mBaseLine0, mTextPaint);

        mText = "涨停";
        canvas.drawText(mText, mText3X, mBaseLine0, mTextPaint);

        mText = "振幅";
        canvas.drawText(mText, mText0X, mBaseLine2, mTextPaint);

        mText = "外盘";
        canvas.drawText(mText, mText1X, mBaseLine2, mTextPaint);

        mText = "总市值";
        canvas.drawText(mText, mText2X, mBaseLine2, mTextPaint);

        mText = "跌停";
        canvas.drawText(mText, mText3X, mBaseLine2, mTextPaint);

        mText = "每股收益";
        canvas.drawText(mText, mText0X, mBaseLine4, mTextPaint);

        mText = "委比";
        canvas.drawText(mText, mText1X, mBaseLine4, mTextPaint);

        mText = "市盈率";
        canvas.drawText(mText, mText2X, mBaseLine4, mTextPaint);

        mText = "市净率";
        canvas.drawText(mText, mText3X, mBaseLine4, mTextPaint);
    }

    private void calculateValue() {
        mItemWidth = (mWidth - mLeftPadding - mRightPadding) / 4;
        mText0X = mLeftPadding;
        mText1X = mLeftPadding + mItemWidth;
        mText2X = mLeftPadding + mItemWidth*2;
        mText3X = mLeftPadding + mItemWidth*3;

        mBaseLine0 = mTopPadding + getBaseLine(mTextPaint);
        mBaseLine1 = mBaseLine0 + mVerticalGap + getBaseLine(mTextPaint);
        mBaseLine2 = mBaseLine1 + mVerticalSpace + getBaseLine(mTextPaint);
        mBaseLine3 = mBaseLine2 + mVerticalGap + getBaseLine(mTextPaint);
        mBaseLine4 = mBaseLine3 + mVerticalSpace + getBaseLine(mTextPaint);
        mBaseLine5 = mBaseLine4 + mVerticalGap + getBaseLine(mTextPaint);
    }

    public void setParamData(RealData data) {
        if (data!=null) {
            mList = data.getData().getSnapshot().getProd_code();
            invalidate();
        }
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
