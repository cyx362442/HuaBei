package com.zhongbang.huabei.bean;

import java.util.List;

/**
 * Created by Administrator on 2017-08-29.
 */

public class IDCard {
    /**
     * status : OK
     * data : {"facade":"0","item":{"name":"陈银圆","cardno":"350524198403203035","sex":"男","folk":"汉","birthday":"1984年03月20日","address":"福建省安溪县龙门镇山头村官田园34号","issue_authority":[],"valid_period":[],"header_pic":[]}}
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
         * facade : 0
         * item : {"name":"陈银圆","cardno":"350524198403203035","sex":"男","folk":"汉","birthday":"1984年03月20日","address":"福建省安溪县龙门镇山头村官田园34号","issue_authority":[],"valid_period":[],"header_pic":[]}
         */

        private String facade;
        private ItemBean item;

        public String getFacade() {
            return facade;
        }

        public void setFacade(String facade) {
            this.facade = facade;
        }

        public ItemBean getItem() {
            return item;
        }

        public void setItem(ItemBean item) {
            this.item = item;
        }

        public static class ItemBean {
            /**
             * name : 陈银圆
             * cardno : 350524198403203035
             * sex : 男
             * folk : 汉
             * birthday : 1984年03月20日
             * address : 福建省安溪县龙门镇山头村官田园34号
             * issue_authority : []
             * valid_period : []
             * header_pic : []
             */

            private String name;
            private String cardno;
            private String sex;
            private String folk;
            private String birthday;
            private String address;
            private List<?> issue_authority;
            private List<?> valid_period;
            private List<?> header_pic;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCardno() {
                return cardno;
            }

            public void setCardno(String cardno) {
                this.cardno = cardno;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public String getFolk() {
                return folk;
            }

            public void setFolk(String folk) {
                this.folk = folk;
            }

            public String getBirthday() {
                return birthday;
            }

            public void setBirthday(String birthday) {
                this.birthday = birthday;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public List<?> getIssue_authority() {
                return issue_authority;
            }

            public void setIssue_authority(List<?> issue_authority) {
                this.issue_authority = issue_authority;
            }

            public List<?> getValid_period() {
                return valid_period;
            }

            public void setValid_period(List<?> valid_period) {
                this.valid_period = valid_period;
            }

            public List<?> getHeader_pic() {
                return header_pic;
            }

            public void setHeader_pic(List<?> header_pic) {
                this.header_pic = header_pic;
            }
        }
    }
}
