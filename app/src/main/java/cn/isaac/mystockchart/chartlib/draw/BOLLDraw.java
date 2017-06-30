package cn.isaac.mystockchart.chartlib.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import cn.isaac.mystockchart.chartlib.entityImpl.BOLLImpl;
import cn.isaac.mystockchart.chartlib.impl.IKChartView;

/**
 * Created by RaoWei on 2017/6/16 14:26.
 */

public class BOLLDraw extends BaseDraw<BOLLImpl> {
    public BOLLDraw(Context context) {
        super(context);
    }

    @Override
    public void drawTranslated(@Nullable BOLLImpl lastPoint, @NonNull BOLLImpl curPoint, float lastX, float curX, Canvas canvas, @NonNull IKChartView view, int position) {
        view.drawChildLine(canvas, ma5Paint, lastX, lastPoint.getUp(), curX, curPoint.getUp());
        view.drawChildLine(canvas, ma10Paint, lastX, lastPoint.getMb(), curX, curPoint.getMb());
        view.drawChildLine(canvas, ma20Paint, lastX, lastPoint.getDn(), curX, curPoint.getDn());
    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull IKChartView view, int position, float x, float y) {
        String text = "";
        BOLLImpl point = (BOLLImpl) view.getItem(position);
        text = "UP:" + view.formatValue(point.getUp()) + " ";
        canvas.drawText(text, x, y, ma5Paint);
        x += ma5Paint.measureText(text);
        text = "MB:" + view.formatValue(point.getMb()) + " ";
        canvas.drawText(text, x, y, ma10Paint);
        text = "DN:" + view.formatValue(point.getDn()) + " ";
        canvas.drawText(text, x, y, ma20Paint);
    }

    @Override
    public float getMaxValue(BOLLImpl point) {
        if (Float.isNaN(point.getUp())) {  //判断是否是一个非数字
            return point.getMb();
        }
        return point.getUp();
    }

    @Override
    public float getMinValue(BOLLImpl point) {
        if (Float.isNaN(point.getDn())) {
            return point.getMb();
        }
        return point.getDn();
    }
}
