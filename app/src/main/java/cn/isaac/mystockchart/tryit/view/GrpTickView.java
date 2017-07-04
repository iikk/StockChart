package cn.isaac.mystockchart.tryit.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import cn.isaac.mystockchart.R;
import cn.isaac.mystockchart.tryit.entity.RealData;
import cn.isaac.mystockchart.tryit.entity.TickData;
import cn.isaac.mystockchart.tryit.tools.CanvasTools;

/**
 * 五档和明细
 * Created by RaoWei on 2017/6/26 15:53.
 */

public class GrpTickView extends RelativeLayout {

    private int mWidth;
    private int mHeigh;
    private Paint mPaint;
    private TextPaint mTextPaint;
    private int mGray;
    private int mGray66;
    private int mBlack;
    private int mRed;
    private int mGreen;
    private int mBlue;
    private float mLeftPadding;
    private float mTopPadding;
    private float mBottomPadding;
    private float mRightPadding;
    private float mTextSizeBig;
    private float mTextSizeSmall;
    private int mTitleHeigh;
    private int mTitleGap;
    private int halfWidth;
    private float mContentHeigh;
    private String mText;
    private RectF[] mTouchRectF;
    private int downIn;
    private int choiceOne;
    private String[] mTitle;
    private Float[] mTitleStartx;
    private List<String> mGrpList;
    private ArrayList<String[]> mBidGrpList;
    private ArrayList<String[]> mOfferGrpList;
    private ArrayList<ArrayList<String>> mTickList;
    private ArrayList<String[]> mTickSimple;

    public GrpTickView(Context context) {
        super(context);
        init();
    }

    public GrpTickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GrpTickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mGray = Color.parseColor("#F3F3F3");
        mGray66 = Color.parseColor("#666666");
        mBlack = Color.parseColor("#333333");
        mRed = Color.parseColor("#ED3331");
        mGreen = Color.parseColor("#008000");
        mBlue = Color.parseColor("#21AEFF");

        mTextSizeBig = sp(18);
        mTextSizeSmall = sp(13);
        mLeftPadding = px(3);
        mRightPadding = px(3);
        mTopPadding = px(15);
        mBottomPadding = px(10);
        mTitleHeigh = px(30);
        mTitleGap = px(1);
        choiceOne = 0;


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeigh = h;
        halfWidth = mWidth/2;
        mContentHeigh = mHeigh - mTitleHeigh;
        mTouchRectF = new RectF[]{
                new RectF(0,mTopPadding,halfWidth,mTopPadding+mTitleHeigh),
                new RectF(halfWidth,mTopPadding,mWidth,mTopPadding+mTitleHeigh),
                new RectF(0,mTopPadding+mTitleHeigh,mWidth,mHeigh)
        };

        mTitle = new String[]{"五档","明细"};
        mTextPaint.setTextSize(mTextSizeBig);
        float v = mTextPaint.measureText(mTitle[0]);
        mTitleStartx = new Float[]{mWidth/4-v/2,halfWidth+mWidth/4-v/2};

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(getResources().getColor(R.color.chart_white));
        drawTitle(canvas);
        drawContent(canvas);
    }


    private void drawContent(Canvas canvas) {
        if (choiceOne == 0) {
            if (mOfferGrpList==null) {
                return;
            }
            drawGrp(canvas);
        } else {
            if (mTickSimple== null) {
                return;
            }
            drawTick(canvas);
        }
    }

    private void drawTick(Canvas canvas) {

        mTextPaint.setTextSize(mTextSizeSmall);
        float verticalSpace = (mContentHeigh - getTextHeigh(mTextPaint) * 10 - mBottomPadding) / 10;
        mText = "5.71";
        float v = mTextPaint.measureText(mText);
        float startx0 = halfWidth - v / 2;
        float startx1;
        for (int i = 1; i <= mTickSimple.size(); i++) {
            mText = mTickSimple.get(i-1)[0];
            mTextPaint.setColor(mGray66);
            canvas.drawText(mText,mLeftPadding,i*(verticalSpace+getTextHeigh(mTextPaint))+mTitleHeigh,mTextPaint);

            mText = mTickSimple.get(i-1)[1];
            mTextPaint.setColor(mGreen);
            canvas.drawText(mText,startx0,i*(verticalSpace+getTextHeigh(mTextPaint))+mTitleHeigh,mTextPaint);

            mText = mTickSimple.get(i-1)[2];
            v = mTextPaint.measureText(mText);
            startx1 = mWidth-v-mRightPadding;
            mTextPaint.setColor(mBlue);
            canvas.drawText(mText,startx1,i*(verticalSpace+getTextHeigh(mTextPaint))+mTitleHeigh,mTextPaint);

        }
    }

    private void drawGrp(Canvas canvas) {
        mText = "卖";
        mTextPaint.setTextSize(mTextSizeSmall);
        mTextPaint.setColor(mGray66);
        float verticalSpace = (mContentHeigh - getTextHeigh(mTextPaint) * 10 - mBottomPadding) / 10;
        for (int i = 5; i > 0; i--) {
            canvas.drawText(mText+i,mLeftPadding,(6-i)*(verticalSpace+getTextHeigh(mTextPaint))+mTitleHeigh,mTextPaint);
        }

        mPaint.setStrokeWidth(px(1));
        mPaint.setColor(mGray);
        canvas.drawLine(mLeftPadding,5*(verticalSpace+getTextHeigh(mTextPaint))+mTitleHeigh + verticalSpace/2,mWidth-mRightPadding,5*(verticalSpace+getTextHeigh(mTextPaint))+mTitleHeigh + verticalSpace/2,mPaint);
        mText = "买";
        for (int i = 1; i < 6; i++) {
            canvas.drawText(mText+i,mLeftPadding,(5+i)*(verticalSpace+getTextHeigh(mTextPaint))+mTitleHeigh,mTextPaint);
        }

        mText = "5.76";
        float v = mTextPaint.measureText(mText);
        float startx = halfWidth - v / 2-px(3);
        float startx2;
        for (int i = 1; i <= 5; i++) {
            mTextPaint.setColor(mGreen);
            canvas.drawText(mOfferGrpList.get(5-i)[0],startx,i*(verticalSpace+getTextHeigh(mTextPaint))+mTitleHeigh,mTextPaint);

            mText = mOfferGrpList.get(5-i)[1];
            mTextPaint.setColor(mBlue);
            v = mTextPaint.measureText(mText);
            startx2 = mWidth-v-mRightPadding;
            canvas.drawText(mText,startx2,i*(verticalSpace+getTextHeigh(mTextPaint))+mTitleHeigh,mTextPaint);
        }
        for (int i = 6; i <= 10; i++) {
            mTextPaint.setColor(mGreen);
            canvas.drawText(mBidGrpList.get(i-6)[0],startx,i*(verticalSpace+getTextHeigh(mTextPaint))+mTitleHeigh,mTextPaint);

            mText = mBidGrpList.get(i-6)[1];
            mTextPaint.setColor(mBlue);
            v = mTextPaint.measureText(mText);
            startx2 = mWidth-v-mRightPadding;
            canvas.drawText(mText,startx2,i*(verticalSpace+getTextHeigh(mTextPaint))+mTitleHeigh,mTextPaint);
        }

    }

    private void drawTitle(Canvas canvas) {
        mPaint.setColor(mGray);
        canvas.drawRect(0, mTopPadding, halfWidth - px(0.5f), mTitleHeigh + mTopPadding, mPaint);
        canvas.drawRect(halfWidth + px(0.5f),mTopPadding,mWidth,mTitleHeigh +mTopPadding,mPaint);

        mTextPaint.setTextSize(mTextSizeBig);
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        for (int i = 0; i < mTitle.length; i++) {
            if (choiceOne==i) {
                mTextPaint.setColor(mRed);

                mPaint.setColor(mRed);
                mPaint.setStrokeWidth(px(2));
                canvas.drawLine(choiceOne==0?0:(halfWidth+px(0.5f)), mTopPadding + mTitleHeigh, choiceOne==0?halfWidth - px(0.5f):mWidth, mTopPadding + mTitleHeigh, mPaint);
            } else {
                mTextPaint.setColor(mBlack);
            }
                canvas.drawText(mTitle[i], mTitleStartx[i], (mTopPadding * 2 + mTitleHeigh - fm.bottom - fm.top) / 2, mTextPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (int i = 0; i < mTouchRectF.length; i++) {
                    if (mTouchRectF[i].contains(event.getX(), event.getY())) {
                        downIn = i;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (mTouchRectF[downIn].contains(event.getX(), event.getY())) {
                    switch (downIn) {
                        case 0:
                            choiceOne=0;
                            break;
                        case 1:
                            choiceOne=1;
                            break;
                        case 2:
                            choiceOne = choiceOne==0?1:0;
                            break;
                    }
                    invalidate();
                }
                break;
        }
        return true;
    }

    public void setGrpData(RealData data) {
        if (data!=null) {
            mGrpList = data.getData().getSnapshot().getProd_code();
            if (mGrpList.get(19).length()<3) {
                return;
            }
            mBidGrpList = CanvasTools.formateGrp(mGrpList.get(19));
            mOfferGrpList = CanvasTools.formateGrp(mGrpList.get(20));

            invalidate();
        }
    }

    public void setTickData(TickData data) {
        if (data!=null) {
            mTickList = data.getData().getTick().getProd_code();
            ArrayList<String> item;
            if (mTickSimple == null) {
                mTickSimple = new ArrayList<>();
            } else {
                mTickSimple.clear();
            }
            for (int i = 0; i < mTickList.size(); i++) {
                item = mTickList.get(i);
               mTickSimple.add( new String[]{CanvasTools.formateTime2(item.get(0)),item.get(1),item.get(2).substring(0,item.get(2).length()-2),item.get(5)});
            }
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
