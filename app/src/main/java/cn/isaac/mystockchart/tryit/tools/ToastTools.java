package cn.isaac.mystockchart.tryit.tools;

import android.widget.Toast;

import cn.isaac.mystockchart.tryit.MyApp;

/**
 * Created by RaoWei on 2017/7/5 16:44.
 */

public class ToastTools {

    private static Toast toast;

    public static void showShort(String content) {
        if (toast == null) {
            toast = Toast.makeText(MyApp.context, content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }
}
