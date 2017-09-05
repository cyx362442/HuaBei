package com.zhongbang.huabei.bean;

/**
 * Created by Administrator on 2017-09-03.
 */

public class BankCard {

    /**
     * status : OK
     * data : {"item":{"cardno":"6222600790001973506","bankname":"交通银行(03010000)","cardname":"太平洋借记卡","cardtype":"借记卡"}}
     */

    private String status;
    private DataBean data;

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

    public static class DataBean {
        /**
         * item : {"cardno":"6222600790001973506","bankname":"交通银行(03010000)","cardname":"太平洋借记卡","cardtype":"借记卡"}
         */

        private ItemBean item;

        public ItemBean getItem() {
            return item;
        }

        public void setItem(ItemBean item) {
            this.item = item;
        }

        public static class ItemBean {
            /**
             * cardno : 6222600790001973506
             * bankname : 交通银行(03010000)
             * cardname : 太平洋借记卡
             * cardtype : 借记卡
             */

            private String cardno;
            private String bankname;
            private String cardname;
            private String cardtype;

            public String getCardno() {
                return cardno;
            }

            public void setCardno(String cardno) {
                this.cardno = cardno;
            }

            public String getBankname() {
                return bankname;
            }

            public void setBankname(String bankname) {
                this.bankname = bankname;
            }

            public String getCardname() {
                return cardname;
            }

            public void setCardname(String cardname) {
                this.cardname = cardname;
            }

            public String getCardtype() {
                return cardtype;
            }

            public void setCardtype(String cardtype) {
                this.cardtype = cardtype;
            }
        }
    }
}
