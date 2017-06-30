package cn.isaac.mystockchart.chartlib.formatter;

import android.text.format.DateUtils;

import java.util.Date;

import cn.isaac.mystockchart.chartlib.impl.IDateTimeFormatter;
import cn.isaac.mystockchart.chartlib.utils.DateUtil;

/**
 * Created by RaoWei on 2017/6/15 15:07.
 */

public class DateFormatter implements IDateTimeFormatter {
    @Override
    public String format(Date date) {
        if (date != null) {
            return DateUtil.dateFormat.format(date);
        }
        return "";
    }
}
