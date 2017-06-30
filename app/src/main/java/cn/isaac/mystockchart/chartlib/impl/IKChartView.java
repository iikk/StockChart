package cn.isaac.mystockchart.chartlib.impl;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.widget.AdapterView;

import java.util.Date;

/**
 * KChartView的抽象类
 * Created by RaoWei on 2017/6/15 14:34.
 */

public interface IKChartView {

    /**
     * 设置数据适配器
     * @param adapter
     */
    void setAdapter(IAdapter adapter);

    /**
     * 获取适配器
     * @return
     */
    IAdapter getAdapter();

    /**
     * 给子区域添加画图方法
     * @param name  显示的文字标签
     * @param draw  IChartView
     */
    void addChildDraw(String name, @NonNull IChartDraw draw);

    /**
     * 将实际值转换为主图的坐标值
     * @param value
     * @return
     */
    float getMainY(float value);

    /**
     * 将实际值转换为子图的坐标值
     * @param value
     * @return
     */
    float getChildY(float value);

    /**
     * 在主区域画线
     * @param startX        开始点的横坐标
     * @param startValue    开始点的值
     * @param stopX         结束点的横坐标
     * @param stopValue     结束点的值
     */
    void drawMainLine(Canvas canvas, Paint paint, float startX, float startValue, float stopX, float stopValue);

    /**
     * 在子区域画线
     * @param canvas
     * @param paint
     * @param startX        开始点的横坐标
     * @param startValue    开始点的值
     * @param stopX         结束点的横坐标
     * @param stopValue     结束点的值
     */
    void drawChildLine(Canvas canvas, Paint paint, float startX, float startValue, float stopX, float stopValue);

    /**
     * 通过序号获取item
     * @param position
     * @return
     */
    Object getItem(int position);

    /**
     * 获取当前点的x坐标
     * @param position
     * @return
     */
    float getX(int position);

    /**
     * 格式化value
     * @param value
     * @return
     */
    String formatValue(float value);

    /**
     * 格式化date
     * @param date
     * @return
     */
    String formatDateTime(Date date);

    /**
     * 开始动画
     */
    void startAnimation();

    /**
     * 设置动画时间
     * @param duration  时间（毫秒）
     */
    void setAnimationDuration(long duration);

    /**
     * 获取view宽度
     * @return
     */
    int getChartWidth();

    /**
     * 获取上方padding
     * @return
     */
    float getTopPadding();

    /**
     * 是否是长按状态
     * @return
     */
    boolean isLongPress();

    /**
     * 获取长按状态下选中的索引
     * @return  返回选择点的索引 非长按状态下返回-1
     */
    int getSelectedIndex();

    /**
     * 设置选中变化监听
     * @param l
     */
    void setOnSelectedChangedListener(OnSelectedChangedListener l);

    /**
     * view中的x转化为TranslateX
     * @param x
     * @return
     */
    float xToTranslateX(float x);

    /**
     * translateX转化为view中的x
     * @param translateX
     * @return
     */
    float translateXToX(float translateX);

    /**
     * 设置超出右方后可滑动的范围
     * @param overScrollRange
     */
    void setOverScrollRange(float overScrollRange);


    /**
     * 选中点变化时的监听
     */
    interface OnSelectedChangedListener {

        /**
         * 当选中点变化时
         * @param view  当前view
         * @param point 选中的点
         * @param index 选中点的索引
         */
        void onSelectedChanged(IKChartView view, Object point, int index);
    }
}
