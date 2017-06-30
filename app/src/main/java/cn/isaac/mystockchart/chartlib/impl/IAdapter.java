package cn.isaac.mystockchart.chartlib.impl;

import android.database.DataSetObserver;

import java.util.Date;

/**
 * 数据适配器
 * Created by RaoWei on 2017/6/15 14:05.
 */

public interface IAdapter {

    /**
     * @return  获取点的数目
     */
    int getCount();

    /**
     * 通过序号获取item
     *
     * @param position
     * @return
     */
    Object getItem(int position);

    /**
     * 通过序号获取时间
     *
     * @param position
     * @return
     */
    Date getDate(int position);

    /**
     * 注册一个数据观察者
     *
     * @param observer
     */
    void registerDataSetObserver(DataSetObserver observer);

    /**
     * 移除一个数据观察者
     *
     * @param observer
     */
    void unregisterDataSetObserver(DataSetObserver observer);

    /**
     * 当数据发生变化时调用
     */
    void notifyDataSetChanged();
}
