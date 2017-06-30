package cn.isaac.mystockchart.tryit.hengsheng;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;

import java.io.IOException;
import java.util.HashMap;

import cn.isaac.mystockchart.R;
import cn.isaac.mystockchart.tryit.Const;
import cn.isaac.mystockchart.tryit.MinuteActivity;
import cn.isaac.mystockchart.tryit.entity.KlineData;
import cn.isaac.mystockchart.tryit.entity.RealData;
import cn.isaac.mystockchart.tryit.entity.TickData;
import cn.isaac.mystockchart.tryit.entity.TokenData;
import cn.isaac.mystockchart.tryit.entity.TrendData;
import cn.isaac.mystockchart.tryit.view.ChartHeadView;
import cn.isaac.mystockchart.tryit.view.GrpTickView;
import cn.isaac.mystockchart.tryit.view.KlineView;
import cn.isaac.mystockchart.tryit.view.MinuteView;
import cn.isaac.mystockchart.tryit.view.MoreParamView;
import cn.isaac.mystockchart.tryit.view.SelectBar;
import okhttp3.Response;

/**
 * Created by RaoWei on 2017/6/22 11:05.
 */

public class StockChartActivity extends Activity {
    private String mCode;
    private MinuteView mMinuteView;
    private SelectBar mSelectBar;
    private View mMinuteLayout;
    private KlineView klineView;
    private int mIndex;
    private GrpTickView grpTickView;
    private ChartHeadView headView;
    private MoreParamView moreParamView;
    private PopupWindow pw;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_chart);
        mMinuteView = (MinuteView) findViewById(R.id.minute_view);
        mCode = getIntent().getStringExtra("code");
        if (TextUtils.isEmpty(mCode)) {
            mCode = "002305.SZ";
        }
        findView();
        init();
    }

    private void findView() {
        mSelectBar = (SelectBar) findViewById(R.id.select_bar);
        mMinuteLayout = findViewById(R.id.minute);
        klineView = (KlineView) findViewById(R.id.kline);
        grpTickView = (GrpTickView) findViewById(R.id.grp_tick_view);
        headView = (ChartHeadView) findViewById(R.id.head_view);
        mSelectBar.setOnSelectChanged(new SelectBar.OnSelectedListener() {
            @Override
            public void onSelectChanged(int index) {
                mIndex = index;
                init();
                if (index == 0) {
                    mMinuteLayout.setVisibility(View.VISIBLE);
                    klineView.setVisibility(View.GONE);
                } else {
                    mMinuteLayout.setVisibility(View.GONE);
                    klineView.setVisibility(View.VISIBLE);
                }
            }
        });
        headView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();
            }
        });
    }

    public static void lanuch(Context context, String code) {
        Intent intent = new Intent(context, StockChartActivity.class);
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
                        map.clear();
                        map.put("access_token", hscloudToken);
                        map.put("en_prod_code", mCode);
                        map.put("fields", "open_px,high_px,business_balance,low_px,turnover_ratio,vol_ratio,preclose_px," +
                                "amplitude,eps,business_amount_in,business_amount_out,entrust_rate,circulation_value,market_value," +
                                "pre_rate,up_px,down_px,dyn_pb_rate,bid_grp,offer_grp,last_px,px_change,px_change_rate,entrust_rate,pe_rate");
                        Response realResponse = OkGo.get(Const.url_real).params(map).execute();
                        if (realResponse.isSuccessful() && realResponse.code() == 200) {
                            String realBody = realResponse.body().string().replace(mCode, "prod_code");
                            final RealData realData = gson.fromJson(realBody, RealData.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setRealData(realData);
                                }
                            });
                        } else {
                            Log.e("canvas： real ==>>", "请求失败");
                        }

                        //请求tick
                        map.clear();
                        map.put("access_token", hscloudToken);
                        map.put("prod_code", mCode);
                        map.put("data_count", "10");
                        //map.put("fields", "business_time,hq_px,business_amount" );
                        Response tickResponse = OkGo.get(Const.url_tick).params(map).execute();
                        if (tickResponse.isSuccessful() && tickResponse.code() == 200) {
                            String realBody = tickResponse.body().string().replace(mCode, "prod_code");
                            final TickData tickData = gson.fromJson(realBody, TickData.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    grpTickView.setTickData(tickData);
                                }
                            });
                        } else {
                            Log.e("canvas： tick ==>>", "请求失败");
                        }

                        switch (mIndex) {
                            case 0:  //分时
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

                                break;
                            case 1:  //日K
                            case 2:  //周K
                            case 3:  //月K
                                //请求kline
                                map.clear();
                                map.put("access_token", hscloudToken);
                                map.put("prod_code", mCode);
                                map.put("get_type", "offset");
                                map.put("candle_period", 6+mIndex-1+"");
                                map.put("prod_code", mCode);
                                map.put("data_count", "80");
                                map.put("fields", "open_px,high_px,low_px,close_px,business_amount");
                                Response responseLline = OkGo.get(Const.url_kline).params(map).execute();
                                if (responseLline.isSuccessful() && responseLline.code() == 200) {
                                    String trendBody = responseLline.body().string().replace(mCode, "prod_code");
                                    final KlineData klineData = gson.fromJson(trendBody, KlineData.class);
                                    //Log.e("canvas： trend ==>>", "" + trendData.getData().getTrend().getProd_code().size());
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            klineView.setKlineData(klineData);
                                        }
                                    });
                                }
                                break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void setRealData(RealData realData) {
        headView.setHeadData(realData);
        grpTickView.setGrpData(realData);
        if (moreParamView!=null) {
            moreParamView.setParamData(realData);
        }
    }

    private void showPopup() {
        if (moreParamView == null||pw==null) {
            View inflate = LayoutInflater.from(this).inflate(R.layout.layout_more_param_popup, null);
            moreParamView = (MoreParamView) inflate.findViewById(R.id.more_param);
            pw = new PopupWindow(inflate, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        }
        init();
        pw.showAsDropDown(headView);
    }
}
