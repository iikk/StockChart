package cn.isaac.mystockchart.chartlib.formatter;

import cn.isaac.mystockchart.chartlib.impl.IValueFormatter;

/**
 * Created by RaoWei on 2017/6/15 15:18.
 */

public class ValueFormatter implements IValueFormatter {
    @Override
    public String format(float value) {

        return String.format("%.2f", value);
    }
}
