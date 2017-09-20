package com.zhongbang.huabei.bean;

/**
 * Created by Administrator on 2017-09-20.
 */

public class CommitBank {
    /**
     * code : 1
     * data : {"bankCardBackImg":"http://chinaqmf.cn/img/152602026905.jpg","bankCardBranch":"古古","bankCardCity":"黔南","bankCardFrontImg":"http://chinaqmf.cn/img/152602026904.jpg","bankCardName":"农业银行(01030000)","bankCardNumber":"6230******0537","bankCardProvince":"贵州省"}
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
         * bankCardBackImg : http://chinaqmf.cn/img/152602026905.jpg
         * bankCardBranch : 古古
         * bankCardCity : 黔南
         * bankCardFrontImg : http://chinaqmf.cn/img/152602026904.jpg
         * bankCardName : 农业银行(01030000)
         * bankCardNumber : 6230******0537
         * bankCardProvince : 贵州省
         */

        private String bankCardBackImg;
        private String bankCardBranch;
        private String bankCardCity;
        private String bankCardFrontImg;
        private String bankCardName;
        private String bankCardNumber;
        private String bankCardProvince;

        public String getBankCardBackImg() {
            return bankCardBackImg;
        }

        public void setBankCardBackImg(String bankCardBackImg) {
            this.bankCardBackImg = bankCardBackImg;
        }

        public String getBankCardBranch() {
            return bankCardBranch;
        }

        public void setBankCardBranch(String bankCardBranch) {
            this.bankCardBranch = bankCardBranch;
        }

        public String getBankCardCity() {
            return bankCardCity;
        }

        public void setBankCardCity(String bankCardCity) {
            this.bankCardCity = bankCardCity;
        }

        public String getBankCardFrontImg() {
            return bankCardFrontImg;
        }

        public void setBankCardFrontImg(String bankCardFrontImg) {
            this.bankCardFrontImg = bankCardFrontImg;
        }

        public String getBankCardName() {
            return bankCardName;
        }

        public void setBankCardName(String bankCardName) {
            this.bankCardName = bankCardName;
        }

        public String getBankCardNumber() {
            return bankCardNumber;
        }

        public void setBankCardNumber(String bankCardNumber) {
            this.bankCardNumber = bankCardNumber;
        }

        public String getBankCardProvince() {
            return bankCardProvince;
        }

        public void setBankCardProvince(String bankCardProvince) {
            this.bankCardProvince = bankCardProvince;
        }
    }
}
