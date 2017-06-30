package cn.isaac.mystockchart.tryit.entity;

import java.util.List;

/**
 * Created by RaoWei on 2017/6/19 14:44.
 */

public class RealData {

    /**
     * data : {"snapshot":{"fields":["data_timestamp","shares_per_hand","preclose_px","up_px","down_px"],"600975.SS":[144052810,100,5.97,6.57,5.37]}}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * snapshot : {"fields":["data_timestamp","shares_per_hand","preclose_px","up_px","down_px"],"600975.SS":[144052810,100,5.97,6.57,5.37]}
         */

        private SnapshotBean snapshot;

        public SnapshotBean getSnapshot() {
            return snapshot;
        }

        public void setSnapshot(SnapshotBean snapshot) {
            this.snapshot = snapshot;
        }

        public static class SnapshotBean {
            private List<String> fields;
            private List<String> prod_code;

            public List<String> getFields() {
                return fields;
            }

            public void setFields(List<String> fields) {
                this.fields = fields;
            }

            public List<String> getProd_code() {
                return prod_code;
            }

            public void setProd_code(List<String> prod_code) {
                this.prod_code = prod_code;
            }
        }
    }
}
