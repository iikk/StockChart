package cn.isaac.mystockchart.chartExample.chart;

import android.content.ComponentName;
import android.content.Context;
import android.util.AttributeSet;

import cn.isaac.mystockchart.chartlib.BaseKChartView;
import cn.isaac.mystockchart.chartlib.draw.BOLLDraw;
import cn.isaac.mystockchart.chartlib.draw.KDJDraw;
import cn.isaac.mystockchart.chartlib.draw.MACDDraw;
import cn.isaac.mystockchart.chartlib.draw.MainDraw;
import cn.isaac.mystockchart.chartlib.draw.RSIDraw;

/**
 * Created by RaoWei on 2017/6/16 16:41.
 */

public class KChartView extends BaseKChartView {
    public KChartView(Context context) {
        super(context);
    }

    public KChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addChildDraw("MACD", new MACDDraw(context));
        addChildDraw("KDJ", new KDJDraw(context));
        addChildDraw("RSI", new RSIDraw(context));
        addChildDraw("BOLL", new BOLLDraw(context));
        setMainDraw(new MainDraw(context));
    }

    @Override
    public void onRightSide() {

    }

    @Override
    public void onLeftSide() {

    }
}
