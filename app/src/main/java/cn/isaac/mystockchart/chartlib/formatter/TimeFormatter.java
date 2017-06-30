package cn.isaac.mystockchart.chartlib.formatter;

import java.util.Date;

import cn.isaac.mystockchart.chartlib.impl.IDateTimeFormatter;
import cn.isaac.mystockchart.chartlib.utils.DateUtil;

/**
 * Created by RaoWei on 2017/6/15 15:15.
 */

public class TimeFormatter implements IDateTimeFormatter {
    @Override
    public String format(Date date) {
        if (date == null) {
            return "";
        }
        return DateUtil.shortTimeFormat.format(date);
    }
}
