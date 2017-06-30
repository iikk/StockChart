package cn.isaac.mystockchart.tryit.entity;

import java.util.ArrayList;

/**
 * Created by RaoWei on 2017/6/20 14:13.
 */

public class KlineData {
    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    private DataBean data;

    public class DataBean {
        public CandleBean getCandle() {
            return candle;
        }

        public void setCandle(CandleBean candle) {
            this.candle = candle;
        }

        private CandleBean candle;

        public class CandleBean {
            private ArrayList<String> fields;

            public ArrayList<String> getFields() {
                return fields;
            }

            public void setFields(ArrayList<String> fields) {
                this.fields = fields;
            }

            public ArrayList<ArrayList<Float>> getProd_code() {
                return prod_code;
            }

            public void setProd_code(ArrayList<ArrayList<Float>> prod_code) {
                this.prod_code = prod_code;
            }

            private ArrayList<ArrayList<Float>> prod_code;
        }
    }
}
