package com.zhongbang.huabei.bean;

/**
 * Created by Administrator on 2017-09-19.
 */

public class CommitUser {

    /**
     * code : 1
     * data : {"address":"好吃唱歌","city":"南平市","idNumber":"3505241984******35","idcardBackImg":"http://chinaqmf.cn/img/152602026902.jpg","idcardExpireDate":"2008.06.16-2018.06.16","idcardFrontImg":"http://chinaqmf.cn/img/152602026901.jpg","merchantName":"刚刚好","name":"*银圆","province":"福建省"}
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
         * address : 好吃唱歌
         * city : 南平市
         * idNumber : 3505241984******35
         * idcardBackImg : http://chinaqmf.cn/img/152602026902.jpg
         * idcardExpireDate : 2008.06.16-2018.06.16
         * idcardFrontImg : http://chinaqmf.cn/img/152602026901.jpg
         * merchantName : 刚刚好
         * name : *银圆
         * province : 福建省
         */

        private String address;
        private String city;
        private String idNumber;
        private String idcardBackImg;
        private String idcardExpireDate;
        private String idcardFrontImg;
        private String merchantName;
        private String name;
        private String province;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getIdNumber() {
            return idNumber;
        }

        public void setIdNumber(String idNumber) {
            this.idNumber = idNumber;
        }

        public String getIdcardBackImg() {
            return idcardBackImg;
        }

        public void setIdcardBackImg(String idcardBackImg) {
            this.idcardBackImg = idcardBackImg;
        }

        public String getIdcardExpireDate() {
            return idcardExpireDate;
        }

        public void setIdcardExpireDate(String idcardExpireDate) {
            this.idcardExpireDate = idcardExpireDate;
        }

        public String getIdcardFrontImg() {
            return idcardFrontImg;
        }

        public void setIdcardFrontImg(String idcardFrontImg) {
            this.idcardFrontImg = idcardFrontImg;
        }

        public String getMerchantName() {
            return merchantName;
        }

        public void setMerchantName(String merchantName) {
            this.merchantName = merchantName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }
    }
}
