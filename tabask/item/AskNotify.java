package com.gwkj.qixiubaodian.fragment.tabask.item;

import java.util.List;

/**
 * Created by carrot on 2019/4/19.
 */

public class AskNotify {

    /**
     * status : ok
     * data : {"list":[{"id":"1000","title":"测试","url":"http://csqxbd.haohaoxiuche.com/qxbd_new/web/main/home/index","tag":"置顶","status":"1","weight":"100","createdat":"2019-04-19 09:53:55"},{"id":"1001","title":"再测试","url":"http://csqxbd.haohaoxiuche.com/qxbd_new/web/datum/home/index","tag":"公告","status":"1","weight":"110","createdat":"2019-04-19 09:55:42"}]}
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
        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * id : 1000
             * title : 测试
             * url : http://csqxbd.haohaoxiuche.com/qxbd_new/web/main/home/index
             * tag : 置顶
             * status : 1
             * weight : 100
             * createdat : 2019-04-19 09:53:55
             */

            private String id;
            private String title;
            private String url;
            private String tag;
            private String status;
            private String weight;
            private String createdat;
            private String color;

            public String getColor() {
                return color;
            }

            public void setColor(String color) {
                this.color = color;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getTag() {
                return tag;
            }

            public void setTag(String tag) {
                this.tag = tag;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getWeight() {
                return weight;
            }

            public void setWeight(String weight) {
                this.weight = weight;
            }

            public String getCreatedat() {
                return createdat;
            }

            public void setCreatedat(String createdat) {
                this.createdat = createdat;
            }
        }
    }
}
