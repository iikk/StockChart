package cn.isaac.mystockchart.chartlib.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import cn.isaac.mystockchart.R;
import cn.isaac.mystockchart.chartlib.entityImpl.CandleImpl;
import cn.isaac.mystockchart.chartlib.impl.IKChartView;
import cn.isaac.mystockchart.chartlib.utils.ViewUtil;

/**
 * Created by RaoWei on 2017/6/16 15:23.
 */

public class MainDraw extends BaseDraw<CandleImpl> {

    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mSelectorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public MainDraw(Context context) {
        super(context);
        mTextPaint.setColor(ContextCompat.getColor(context, R.color.chart_text));
        mTextPaint.setTextSize(context.getResources().getDimension(R.dimen.chart_text_size));
        mSelectorPaint.setColor(ContextCompat.getColor(context, R.color.chart_selector));
        mSelectorPaint.setAlpha(200);
    }

    @Override
    public void drawTranslated(@Nullable CandleImpl lastPoint, @NonNull CandleImpl curPoint, float lastX, float curX, Canvas canvas, @NonNull IKChartView view, int position) {
        drawCandle(view, canvas, curX, curPoint.getHighPrice(), curPoint.getLowPrice(), curPoint.getOpenPrice(), curPoint.getClosePrice());
        //画ma5
        if (lastPoint.getMA5Price() != 0) {
            view.drawMainLine(canvas, ma5Paint, lastX, lastPoint.getMA5Price(), curX,  curPoint.getMA5Price());
        }
        //画ma10
        if (lastPoint.getMA10Price() != 0) {
            view.drawMainLine(canvas, ma10Paint, lastX, lastPoint.getMA10Price(), curX,  curPoint.getMA10Price());
        }
        //画ma20
        if (lastPoint.getMA20Price() != 0) {
            view.drawMainLine(canvas, ma20Paint, lastX, lastPoint.getMA20Price(), curX,  curPoint.getMA20Price());
        }

    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull IKChartView view, int position, float x, float y) {
        CandleImpl point = (CandleImpl) view.getItem(position);
        String text = "MA5:" + view.formatValue(point.getMA5Price()) + " ";
        canvas.drawText(text, x, y, ma5Paint);
        x += ma5Paint.measureText(text);
        text = "MA10:" + view.formatValue(point.getMA10Price()) + " ";
        canvas.drawText(text, x, y, ma10Paint);
        x += ma10Paint.measureText(text);
        text = "MA20:" + view.formatValue(point.getMA20Price()) + " ";
        canvas.drawText(text, x, y, ma20Paint);
        if (view.isLongPress()) {
            drawSelector(view, canvas);
        }
    }

    @Override
    public float getMaxValue(CandleImpl point) {
        return 0;
    }

    @Override
    public float getMinValue(CandleImpl point) {
        return 0;
    }

    /**
     * 画candle
     * @param view
     * @param canvas
     * @param x         x轴坐标
     * @param hight     最高价
     * @param low       最低价
     * @param open      开盘价
     * @param close     收盘价
     */
    private void drawCandle(IKChartView view, Canvas canvas, float x, float hight, float low, float open, float close) {
        hight = view.getMainY(hight);
        low = view.getMainY(low);
        open = view.getMainY(open);
        close = view.getMainY(close);
        int r = mCandleWidth / 2;
        int lineR = mCandleLineWidth / 2;
        if (open > close) {
            //实心
            canvas.drawRect(x - r, close, x + r, open, redPaint);
            canvas.drawRect(x - lineR, hight, x + lineR, low, redPaint);
        } else if (open < close) {
            canvas.drawRect(x - r, open, x + r, close, greenPaint);
            canvas.drawRect(x - lineR, hight, x + lineR, low, greenPaint);
        } else {
            canvas.drawRect(x - r, open, x + r ,close + 1, redPaint);
            canvas.drawRect(x - lineR, hight, x + lineR, low, redPaint);
        }
    }

    private void drawSelector(IKChartView view, Canvas canvas) {
        Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
        float textHeight = metrics.descent - metrics.ascent;

        int index = view.getSelectedIndex();
        float padding = ViewUtil.dp2px(getContext(), 5);
        float margin = ViewUtil.dp2px(getContext(), 5);
        float width = 0;
        float left;
        float top = margin;
        float height = padding * 8 + textHeight * 5;

        CandleImpl point = (CandleImpl) view.getItem(index);
        List<String> strings = new ArrayList<>();
        strings.add(view.formatDateTime(view.getAdapter().getDate(index)));
        strings.add("高：" + point.getHighPrice());
        strings.add("低：" + point.getLowPrice());
        strings.add("开：" + point.getOpenPrice());
        strings.add("收：" + point.getClosePrice());

        for (String s : strings) {
            width = Math.max(width, mTextPaint.measureText(s));
        }
        width += padding * 2;
        float x = view.translateXToX(view.getX(index));
        if (x > view.getChartWidth() / 2) {
            left = margin;
        } else {
            left = view.getChartWidth() - width - margin;
        }

        RectF r = new RectF(left, top, left + width, top + height);
        canvas.drawRoundRect(r, padding, padding, mSelectorPaint);

        float y = top + padding * 2 + (textHeight - metrics.bottom - metrics.top) / 2;
        for (String s : strings) {
            canvas.drawText(s, left + padding, y, mTextPaint);
            y += textHeight + padding;
        }
    }
}
