package cn.isaac.mystockchart.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import cn.isaac.mystockchart.R;

/**
 * Created by RaoWei on 2017/6/30 15:13.
 */

public class TestScaleActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_scale);
    }

    public static void launch(Context context) {
        context.startActivity(new Intent(context, TestScaleActivity.class));
    }
}
