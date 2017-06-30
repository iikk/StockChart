package cn.isaac.mystockchart.tryit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import cn.isaac.mystockchart.R;
import cn.isaac.mystockchart.tryit.entity.TokenData;
import cn.isaac.mystockchart.tryit.entity.TrendData;
import cn.isaac.mystockchart.tryit.view.MinuteView;
import okhttp3.Response;

/**
 * Created by RaoWei on 2017/6/19 9:37.
 */

public class MinuteActivity extends Activity {

    private MinuteView mMinuteView;
    private String mCode;
    private TimerTask mTask;
    private Timer mTimer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas_example);
        mMinuteView = (MinuteView) findViewById(R.id.canvas_view);
        mCode = getIntent().getStringExtra("code");
        if (TextUtils.isEmpty(mCode)) {
            mCode = "002305.SZ";
        }
        startTask();
        init();
    }


    public static void lanuch(Context context, String code) {
        Intent intent = new Intent(context, MinuteActivity.class);
        intent.putExtra("code", code);
        context.startActivity(intent);
    }

    private void startTask() {
        if (mTimer== null && mTask == null) {
            mTask = new TimerTask() {
                @Override
                public void run() {
                    init();
                }
            };
            mTimer = new Timer();
        }

            mTimer.schedule(mTask, 0, 3000);
    }


    private void stopTimer() {
        if (mTimer != null && mTask != null) {
            mTimer.cancel();
        }
    }

    private void init() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //请求token
                    Response response = OkGo.get(Const.url_token).execute();
                    if (response.isSuccessful()) {
                        String body = response.body().string();
                        Gson gson = new Gson();
                        TokenData tokenData = gson.fromJson(body, TokenData.class);
                        String hscloudToken = tokenData.getData().getHscloudToken();
                        Log.e("canvas： token ==>>", hscloudToken);
                        HashMap<String, String> map = new HashMap<>();

                        //请求real
                        /*map.clear();
                        map.put("access_token", hscloudToken);
                        map.put("en_prod_code", "002305.SZ");
                        map.put("fields", "preclose_px,up_px,down_px");
                        Response realResponse = OkGo.get(Const.url_real).params(map).execute();
                        if (realResponse.isSuccessful() && realResponse.code() == 200) {
                            String realBody = realResponse.body().string().replace("002305.SZ", "prod_code");
                            final RealData realData = gson.fromJson(realBody, RealData.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mCanvasView.setRealData(realData);
                                }
                            });
                        } else {
                            Log.e("canvas： real ==>>", "请求失败");
                        }*/

                        //请求trend
                        map.clear();
                        map.put("access_token", hscloudToken);
                        map.put("prod_code", mCode);
                        map.put("fields", "last_px,avg_px,business_amount");
                        Response responseTrend = OkGo.get(Const.url_trend).params(map).execute();
                        if (responseTrend.isSuccessful() && responseTrend.code() == 200) {
                            String trendBody = responseTrend.body().string().replace(mCode, "prod_code");
                            final TrendData trendData = gson.fromJson(trendBody, TrendData.class);
                            //Log.e("canvas： trend ==>>", "" + trendData.getData().getTrend().getProd_code().size());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mMinuteView.setTrendData(trendData);
                                }
                            });
                        } else {
                            Log.e("canvas： real ==>>", "请求失败");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
    }

}
