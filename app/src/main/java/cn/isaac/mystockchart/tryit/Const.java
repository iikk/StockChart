package cn.isaac.mystockchart.tryit;

/**
 * Created by RaoWei on 2017/6/19 11:46.
 */

public interface Const {
    //String url_token = "http://192.168.90.206:8080/v1/hscloud/token";
    String url_token = "http://api.niuke.cn/v1/hscloud/token";
    String url_trend = "https://open.hscloud.cn/quote/v1/trend";
    String url_kline = "https://open.hscloud.cn/quote/v1/kline";
    String url_real = "https://open.hscloud.cn/quote/v1/real";
    String url_tick = "https://open.hscloud.cn/quote/v1/tick";
    String url_secu_info = "https://open.hscloud.cn/info/v2/query/f10_secu_info";  //证券信息

    String param_real = "";
}
