package cn.isaac.mystockchart.tryit.tools;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by RaoWei on 2017/6/19 16:29.
 */

public class CanvasTools {



    public static String leave2point(float num) {
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format(num);
    }

    public static String formateTime(float time) {
        int year = (int)(time/10000);
        int day = (int)(time%100);
        int month = (int)((time%10000)-day)/100;
        return year+"/"+month+"/"+day;
    }

    public static String formateTime2(String s) {
        return s.substring(s.length() - 6, s.length() - 4) + ":" + s.substring(s.length() - 4, s.length() - 2);
    }

    public static String formateNum(String num) {
        float v = Float.parseFloat(num);
        DecimalFormat df = new DecimalFormat("0.##");
        if (v>=50000000) {
            return df.format(v/100000000)+"亿";
        } else if (v>=1000) {
            return df.format(v / 10000) + "万";
        }
        return num;
    }

    public static String formateNum100(String num) {
        float v = Float.parseFloat(num);
        DecimalFormat df = new DecimalFormat("0.##");
        if (v>=100000) {
            return df.format(v / 1000000) + "万";
        }
        return num;
    }

    public static String formateNum1(String num) {
        float v = Float.parseFloat(num);
        DecimalFormat df = new DecimalFormat("0.##");
        if (v>=100000) {
            return df.format(v / 1000000) + "万";
        }
        return num;
    }

    public static ArrayList<String[]> formateGrp(String s) {
        Pattern pattern = Pattern.compile("[,]+");
        String[] split = pattern.split(s);
        ArrayList<String[]> list = new ArrayList<>();
        for (int i = 0; i < split.length-1;) {
            list.add(new String[]{split[i],split[i+1].substring(0,split[i+1].length()-2)});
            i += 3;
        }
        return list;
    }
}
