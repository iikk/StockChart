package cn.isaac.mystockchart.chartlib.entityImpl;

/**
 * 蜡烛图实体
 * Created by RaoWei on 2017/6/16 10:24.
 */

public interface CandleImpl {
    float getOpenPrice();

    float getHighPrice();

    float getLowPrice();

    float getClosePrice();

    float getMA5Price();

    float getMA10Price();

    float getMA20Price();
}
