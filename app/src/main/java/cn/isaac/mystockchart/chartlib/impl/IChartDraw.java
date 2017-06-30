package cn.isaac.mystockchart.chartlib.impl;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by RaoWei on 2017/6/15 14:23.
 */

public interface IChartDraw<T> {

    /**
     * 需要滑动 draw
     *
     * @param lastPoint 上一个点
     * @param curPoint  当前点
     * @param lastX     上一个点的x坐标
     * @param curX      当前点的x坐标
     * @param canvas    canvas
     * @param view      k线图View
     * @param position  当前点的位置
     */
    void drawTranslated(@Nullable T lastPoint, @NonNull T curPoint, float lastX, float curX, Canvas canvas,@NonNull IKChartView view, int position);

    /**
     *
     * @param canvas
     * @param view
     * @param position  该点的位置
     * @param x         x的起始坐标
     * @param y         y的起始坐标
     */
    void drawText(@NonNull Canvas canvas,@NonNull IKChartView view, int position, float x, float y);

    /**
     * 获取当前实体中最大的值
     *
     * @param point
     * @return
     */
    float getMaxValue(T point);

    /**
     * 获取当前实体中最小的值
     *
     * @param point
     * @return
     */
    float getMinValue(T point);
}
