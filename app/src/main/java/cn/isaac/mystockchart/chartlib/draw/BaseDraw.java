package cn.isaac.mystockchart.chartlib.draw;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;

import cn.isaac.mystockchart.R;
import cn.isaac.mystockchart.chartlib.impl.IChartDraw;
import cn.isaac.mystockchart.chartlib.utils.ViewUtil;

/**
 * Created by RaoWei on 2017/6/16 14:09.
 */

public abstract class BaseDraw<T> implements IChartDraw<T> {
    protected float mTextSize = 0;
    //candle的宽度
    protected int mCandleWidth = 0;
    //candle线的宽度
    protected int mCandleLineWidth = 0;
    //线的宽度
    protected int mLineWidth = 0;
    protected Paint redPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected Paint greenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected Paint ma5Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected Paint ma10Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected Paint ma20Paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    protected Context mContext;

    public BaseDraw(Context context) {
        mContext = context;
        mCandleWidth = dp2px(4);
        mCandleLineWidth = dp2px(1);
        mLineWidth = dp2px(.5f);
        mTextSize = getResDimen(R.dimen.chart_text_size);
        redPaint.setColor(getResColor(R.color.chart_red));
        greenPaint.setColor(getResColor(R.color.chart_green));

        ma5Paint.setColor(getResColor(R.color.chart_ma5));
        ma5Paint.setStrokeWidth(mLineWidth);
        ma5Paint.setTextSize(mTextSize);

        ma10Paint.setColor(getResColor(R.color.chart_ma10));
        ma10Paint.setStrokeWidth(mLineWidth);
        ma10Paint.setTextSize(mTextSize);

        ma20Paint.setColor(getResColor(R.color.chart_ma20));
        ma20Paint.setStrokeWidth(mLineWidth);
        ma20Paint.setTextSize(mTextSize);
    }

    public Context getContext() { return mContext; }


    private int dp2px(float dp) {
        return ViewUtil.dp2px(mContext, dp);
    }

    private int px2dp(float px) {
        return ViewUtil.px2dp(mContext, px);
    }

    private int getResColor(int resId) {
        return ContextCompat.getColor(mContext, resId);
    }

    private float getResDimen(int resId) {
        return mContext.getResources().getDimension(resId);
    }

}
