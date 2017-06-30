package cn.isaac.mystockchart.tryit.entity;

import java.util.ArrayList;

/**
 * Created by RaoWei on 2017/6/22 17:49.
 */

public class TickData {
    public DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public class DataBean{
        public TickBean tick;

        public TickBean getTick() {
            return tick;
        }

        public void setTick(TickBean tick) {
            this.tick = tick;
        }

        public class TickBean{
            public ArrayList<String> fields;
            public ArrayList<ArrayList<String>> prod_code;

            public ArrayList<String> getFields() {
                return fields;
            }

            public void setFields(ArrayList<String> fields) {
                this.fields = fields;
            }

            public ArrayList<ArrayList<String>> getProd_code() {
                return prod_code;
            }

            public void setProd_code(ArrayList<ArrayList<String>> prod_code) {
                this.prod_code = prod_code;
            }
        }
    }
}
