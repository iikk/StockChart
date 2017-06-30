package cn.isaac.mystockchart.chartExample;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.util.EncodingUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.isaac.mystockchart.R;
import cn.isaac.mystockchart.chartExample.chart.KChartAdapter;
import cn.isaac.mystockchart.chartExample.chart.KLineEntity;
import cn.isaac.mystockchart.chartlib.formatter.DateFormatter;
import cn.isaac.mystockchart.chartlib.impl.IKChartView;
import cn.isaac.mystockchart.chartlib.utils.ViewUtil;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.title_view)
    RelativeLayout mTitleView;
    @Bind(R.id.tv_price)
    TextView mTvPrice;
    @Bind(R.id.tv_percent)
    TextView mTvPercent;
    @Bind(R.id.ll_status)
    LinearLayout mLlStatus;
    @Bind(R.id.kchart_view)
    cn.isaac.mystockchart.chartExample.chart.KChartView mKchartView;
    private KChartAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        initView();
        initData();
    }

    private void initView() {
        mAdapter = new KChartAdapter();
        mKchartView.setAdapter(mAdapter);
        mKchartView.setDateTimeFormatter(new DateFormatter());
        mKchartView.setGridRows(4);
        mKchartView.setGridColumns(4);
        mKchartView.setOnSelectedChangedListener(new IKChartView.OnSelectedChangedListener() {
            @Override
            public void onSelectedChanged(IKChartView view, Object point, int index) {
                KLineEntity data = (KLineEntity) point;
                Log.i("onSelectedChanged", "index:" + index + " closePrice:" + data.getClosePrice());
            }
        });
        mKchartView.setOverScrollRange(ViewUtil.dp2px(this, 100));
    }

    private void initData() {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        String fileName = "ibm.json";  //k线图的数据
                        String res = "";
                        try {
                            InputStream in = getResources().getAssets().open(fileName);
                            int length = in.available();
                            byte[] buffer = new byte[length];
                            in.read(buffer);
                            res = EncodingUtils.getString(buffer, "UTF-8");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        final List<KLineEntity> data = new Gson().fromJson(res, new TypeToken<List<KLineEntity>>(){}.getType());
                                DataHelper.calculate(data);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.addFooterData(data);
                                mKchartView.startAnimation();
                            }
                        });
                    }
                }
        ).start();
    }
}
