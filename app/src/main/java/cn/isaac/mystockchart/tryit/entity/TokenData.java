package cn.isaac.mystockchart.tryit.entity;

/**
 * Created by RaoWei on 2017/6/19 13:10.
 */

public class TokenData {

    /**
     * code : 0
     * data : {"expiresIn":85199,"hscloudToken":"08E7548C0234486494F785526F76D8CF2017061911245468884413"}
     * message : success
     */

    private int code;
    private DataBean data;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
        /**
         * expiresIn : 85199
         * hscloudToken : 08E7548C0234486494F785526F76D8CF2017061911245468884413
         */

        private int expiresIn;
        private String hscloudToken;

        public int getExpiresIn() {
            return expiresIn;
        }

        public void setExpiresIn(int expiresIn) {
            this.expiresIn = expiresIn;
        }

        public String getHscloudToken() {
            return hscloudToken;
        }

        public void setHscloudToken(String hscloudToken) {
            this.hscloudToken = hscloudToken;
        }
    }
}
