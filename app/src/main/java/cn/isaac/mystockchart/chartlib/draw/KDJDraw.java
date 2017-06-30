package cn.isaac.mystockchart.chartlib.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import cn.isaac.mystockchart.chartlib.entityImpl.KDJImpl;
import cn.isaac.mystockchart.chartlib.impl.IKChartView;

/**
 * Created by RaoWei on 2017/6/16 14:45.
 */

public class KDJDraw extends BaseDraw<KDJImpl> {
    public KDJDraw(Context context) {
        super(context);
    }

    @Override
    public void drawTranslated(@Nullable KDJImpl lastPoint, @NonNull KDJImpl curPoint, float lastX, float curX, Canvas canvas, @NonNull IKChartView view, int position) {
        view.drawChildLine(canvas, ma5Paint, lastX, lastPoint.getK(), curX, curPoint.getK());
        view.drawChildLine(canvas, ma10Paint, lastX, lastPoint.getD(), curX, curPoint.getD());
        view.drawChildLine(canvas, ma20Paint, lastX, lastPoint.getJ(), curX, curPoint.getJ());
    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull IKChartView view, int position, float x, float y) {
        String text = "";
        KDJImpl point = (KDJImpl) view.getItem(position);
        text = "K:" + view.formatValue(point.getK()) + " ";
        canvas.drawText(text, x, y, ma5Paint);
        text = "D:" + view.formatValue(point.getD()) + " ";
        canvas.drawText(text, x, y, ma10Paint);
        text = "J:" + view.formatValue(point.getJ()) + " ";
        canvas.drawText(text, x, y, ma20Paint);
    }

    @Override
    public float getMaxValue(KDJImpl point) {
        return Math.max(point.getK(), Math.max(point.getD(), point.getJ()));
    }

    @Override
    public float getMinValue(KDJImpl point) {
        return Math.min(point.getK(), Math.min(point.getD(), point.getJ()));
    }
}
