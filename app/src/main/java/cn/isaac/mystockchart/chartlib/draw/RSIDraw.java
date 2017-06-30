package cn.isaac.mystockchart.chartlib.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import cn.isaac.mystockchart.chartlib.entityImpl.RSIImpl;
import cn.isaac.mystockchart.chartlib.impl.IKChartView;

/**
 * Created by RaoWei on 2017/6/16 15:16.
 */

public class RSIDraw extends BaseDraw<RSIImpl> {
    public RSIDraw(Context context) {
        super(context);
    }

    @Override
    public void drawTranslated(@Nullable RSIImpl lastPoint, @NonNull RSIImpl curPoint, float lastX, float curX, Canvas canvas, @NonNull IKChartView view, int position) {
        view.drawChildLine(canvas, ma5Paint, lastX, lastPoint.getRsi1(), curX, curPoint.getRsi1());
        view.drawChildLine(canvas, ma10Paint, lastX, lastPoint.getRsi2(), curX, curPoint.getRsi2());
        view.drawChildLine(canvas, ma20Paint, lastX, lastPoint.getRsi3(), curX, curPoint.getRsi3());
    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull IKChartView view, int position, float x, float y) {
        String text = "";
        RSIImpl point = (RSIImpl) view.getItem(position);
        text = "RSI1:" + view.formatValue(point.getRsi1()) + " ";
        canvas.drawText(text, x ,y, ma5Paint);
        x += ma5Paint.measureText(text);
        text = "RSI2:" + view.formatValue(point.getRsi1()) + " ";
        canvas.drawText(text, x ,y, ma10Paint);
        x += ma10Paint.measureText(text);
        text = "RSI3:" + view.formatValue(point.getRsi1()) + " ";
        canvas.drawText(text, x ,y, ma20Paint);
    }

    @Override
    public float getMaxValue(RSIImpl point) {
        return Math.max(point.getRsi1(), Math.max(point.getRsi2(), point.getRsi3()));
    }

    @Override
    public float getMinValue(RSIImpl point) {
        return Math.min(point.getRsi1(), Math.min(point.getRsi2(), point.getRsi3()));
    }
}
