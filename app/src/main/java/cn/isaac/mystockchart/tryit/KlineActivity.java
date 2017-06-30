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

import cn.isaac.mystockchart.R;
import cn.isaac.mystockchart.tryit.entity.KlineData;
import cn.isaac.mystockchart.tryit.entity.TokenData;
import cn.isaac.mystockchart.tryit.view.KlineView;
import okhttp3.Response;

/**
 * Created by RaoWei on 2017/6/20 14:19.
 */

public class KlineActivity extends Activity {

    private KlineView mKlineView;
    private String mCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kline);
        mKlineView = (KlineView) findViewById(R.id.kline_view);
        mCode = getIntent().getStringExtra("code");
        if (TextUtils.isEmpty(mCode)) {
            mCode = "002305.SZ";
        }
        init();
    }

    public static void launch(Context context, String code) {
        Intent intent = new Intent(context, KlineActivity.class);
        intent.putExtra("code", code);
        context.startActivity(intent);
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
                        map.put("get_type", "range");
                        map.put("candle_period", "6");
                        map.put("prod_code", mCode);
                        map.put("start_date", "20170310");
                        map.put("fields", "open_px,high_px,low_px,close_px,business_amount");
                        Response responseTrend = OkGo.get(Const.url_kline).params(map).execute();
                        if (responseTrend.isSuccessful() && responseTrend.code() == 200) {
                            String trendBody = responseTrend.body().string().replace(mCode, "prod_code");
                            final KlineData klineData = gson.fromJson(trendBody, KlineData.class);
                            //Log.e("canvas： trend ==>>", "" + trendData.getData().getTrend().getProd_code().size());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mKlineView.setKlineData(klineData);
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
}
