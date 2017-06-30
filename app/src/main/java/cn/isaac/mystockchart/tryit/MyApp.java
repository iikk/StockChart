package cn.isaac.mystockchart.tryit;

import android.app.Application;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.store.PersistentCookieStore;

import java.util.logging.Level;

/**
 * Created by RaoWei on 2017/6/19 13:07.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initOkHttp();
    }

    private void initOkHttp() {
        //必须调用初始化
        OkGo.init(this);

        //以下设置的所有参数是全局参数,同样的参数可以在请求的时候再设置一遍,那么对于该请求来讲,请求中的参数会覆盖全局参数
        //好处是全局参数统一,特定请求可以特别定制参数
        try {
            //以下都不是必须的，根据需要自行选择,一般来说只需要 debug,缓存相关,cookie相关的 就可以了
            OkGo.getInstance()
                    // 打开该调试开关,打印级别INFO,并不是异常,是为了显眼,不需要就不要加入该行
                    // 最后的true表示是否打印okgo的内部异常，一般打开方便调试错误
                    //.debug("OkGo", Level.INFO, true)  //放到下方，单独开启，方便调试
                    //如果使用默认的 60秒,以下三行也不需要传
                    .setConnectTimeout(OkGo.DEFAULT_MILLISECONDS)  //全局的连接超时时间
                    .setReadTimeOut(OkGo.DEFAULT_MILLISECONDS)     //全局的读取超时时间
                    .setWriteTimeOut(OkGo.DEFAULT_MILLISECONDS)    //全局的写入超时时间
                    //可以全局统一设置缓存模式,默认是不使用缓存,可以不传,具体其他模式看 github 介绍 https://github.com/jeasonlzy/
                    .setCacheMode(CacheMode.NO_CACHE)
                    //可以全局统一设置超时重连次数,默认为三次,那么最差的情况会请求4次(一次原始请求,三次重连请求),不需要可以设置为0
                    .setRetryCount(3)
                    //cookie持久化存储，如果cookie不过期，则一直有效，有可能出现token没有赋值，但仍旧可以请求会员数据的问题
                    .setCookieStore(new PersistentCookieStore())
                    .setCertificates()
            ;
                OkGo.getInstance().debug("OkGo", Level.INFO, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
