package com.zhongbang.huabei.bean;

/**
 * Created by Administrator on 2017-09-20.
 */

public class CommitGroup {

    /**
     * code : 1
     * data : {"facePassed":false,"handCardImg":"http://chinaqmf.cn/img/152602026903.jpg"}
     */

    private String code;
    private DataBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * facePassed : false
         * handCardImg : http://chinaqmf.cn/img/152602026903.jpg
         */

        private boolean facePassed;
        private String handCardImg;

        public boolean isFacePassed() {
            return facePassed;
        }

        public void setFacePassed(boolean facePassed) {
            this.facePassed = facePassed;
        }

        public String getHandCardImg() {
            return handCardImg;
        }

        public void setHandCardImg(String handCardImg) {
            this.handCardImg = handCardImg;
        }
    }
}
