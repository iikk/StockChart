package cn.isaac.mystockchart.chartlib.impl;

/**
 * Value格式化接口
 * Created by RaoWei on 2017/6/15 15:16.
 */

public interface IValueFormatter {

    /**
     * 格式化value
     * @param value  传入的value值
     * @return       反回字符串
     */
    String format(float value);
}
