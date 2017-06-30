package cn.isaac.mystockchart.chartlib;

import android.database.DataSetObservable;
import android.database.DataSetObserver;

import cn.isaac.mystockchart.chartlib.impl.IAdapter;

/**
 * Created by RaoWei on 2017/6/15 14:16.
 */

public abstract class BaseKChartAdapter implements IAdapter {

    private final DataSetObservable mDataSetObservable = new DataSetObservable();

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
       mDataSetObservable.registerObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }

    @Override
    public void notifyDataSetChanged() {
        if (getCount() > 0) {
            mDataSetObservable.notifyChanged();
        } else {
            mDataSetObservable.notifyInvalidated();
        }
    }
}
