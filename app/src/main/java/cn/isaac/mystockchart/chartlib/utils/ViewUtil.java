package cn.isaac.mystockchart.chartlib.utils;

import android.content.Context;

/**
 * Created by RaoWei on 2017/6/15 15:11.
 */

public class ViewUtil {
    public static int dp2px(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5);
    }

    public static int px2dp(Context context, float px) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5f);
    }

    public static int sp2px(Context context, float sp) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * fontScale + 0.5f);
    }
}
