package cn.isaac.mystockchart.chartlib;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.view.View;
import android.widget.TextView;

import cn.isaac.mystockchart.R;
import cn.isaac.mystockchart.chartlib.utils.ViewUtil;

/**
 * k线图中间位置的TabBar
 * Created by RaoWei on 2017/6/15 16:00.
 */

public class KChartTabView extends RelativeLayout implements View.OnClickListener {

    private LinearLayout mLlContainer;
    private TextView mTvFullScreen;
    //当前选择的index
    private int mSelectedIndex = 0;
    private TabSelectListener mTabSelectListener;

    public KChartTabView(Context context) {
        super(context);
        init();
    }

    public KChartTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KChartTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_tab, null, false);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewUtil.dp2px(getContext(), 30));
        view.setLayoutParams(layoutParams);
        addView(view);
        mLlContainer = (LinearLayout) findViewById(R.id.ll_container);
        mTvFullScreen = (TextView) findViewById(R.id.tv_fullScreen);
        mTvFullScreen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isVertical = getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
                Activity activity = (Activity) getContext();
                if (isVertical) {
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);  //设置纵向显示，但可以根据传感器指示的方向来进行改变
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (mSelectedIndex >= 0 && mSelectedIndex < mLlContainer.getChildCount()) {
            mLlContainer.getChildAt(mSelectedIndex).setSelected(false);
        }
        mSelectedIndex = mLlContainer.indexOfChild(v);
        v.setSelected(true);
        mTabSelectListener.onTabSelected(mSelectedIndex);  // TODO: 2017/6/15 这里不判空，会崩溃吗？
    }

    public interface TabSelectListener {

        /**
         * 选择tab的位置序号
         * @param position
         */
        void onTabSelected(int position);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }

    public void addTab(String text) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_tab, null);
        TextView textView = (TextView) view.findViewById(R.id.tab_text);
        textView.setText(text);
        view.setOnClickListener(this);
        mLlContainer.addView(view);
        //第一个默认选中
        if (mLlContainer.getChildCount() == 1) {
            view.setSelected(true);
            mSelectedIndex = 0;
            onTabSelected(mSelectedIndex);
        }
    }

    public void setOnTabSelectListener(TabSelectListener listener) {
        mTabSelectListener = listener;
        if (mLlContainer.getChildCount() > 0 && mTabSelectListener != null) {
            mTabSelectListener.onTabSelected(mSelectedIndex);
        }
    }

    private void onTabSelected(int position) {
        if (mTabSelectListener != null) {
            mTabSelectListener.onTabSelected(position);
        }
    }
}
