package com.gwkj.qixiubaodian.thread.item;

/**
 * Created by carrot on 2018/8/31.
 */

public class CacheItem {

    /**
     * status : ok
     * data : {"content":"{start_welcome, index_new_funtion}"}
     * token : null
     */

    private String status;
    private DataBean data;
    private Object token;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public Object getToken() {
        return token;
    }

    public void setToken(Object token) {
        this.token = token;
    }

    public static class DataBean {
        /**
         * content : {start_welcome, index_new_funtion}
         */

        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
