package cn.isaac.mystockchart.tryit;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import cn.isaac.mystockchart.R;
import cn.isaac.mystockchart.test.TestScaleActivity;
import cn.isaac.mystockchart.tryit.hengsheng.StockChartActivity;

/**
 * Created by RaoWei on 2017/6/21 14:05.
 */

public class InterActivity extends Activity {

    private String mCodeText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inter);
        final EditText code = (EditText) findViewById(R.id.code);

         findViewById(R.id.minute).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                mCodeText = code.getText().toString();
                 MinuteActivity.launch(InterActivity.this, mCodeText);
             }
         });
        findViewById(R.id.kline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCodeText = code.getText().toString();
                KlineActivity.launch(InterActivity.this, mCodeText);
            }
        });
        findViewById(R.id.all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCodeText = code.getText().toString();
                StockChartActivity.launch(InterActivity.this, mCodeText);
            }
        });
        findViewById(R.id.scale).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestScaleActivity.launch(InterActivity.this);
            }
        });
        findViewById(R.id.restart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restart();
            }
        });
    }

    private void restart() {
        Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.set(AlarmManager.RTC, System.currentTimeMillis() + 10, pendingIntent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
