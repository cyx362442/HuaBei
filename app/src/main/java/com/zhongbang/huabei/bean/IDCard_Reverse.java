package com.zhongbang.huabei.bean;

import java.util.List;

/**
 * Created by Administrator on 2017-08-29.
 */

public class IDCard_Reverse {

    /**
     * status : OK
     * data : {"facade":"1","item":{"name":[],"cardno":[],"sex":[],"folk":[],"birthday":[],"address":[],"issue_authority":"安溪县公安局","valid_period":"2008.06.16-2018.06.16","header_pic":[]}}
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
         * facade : 1
         * item : {"name":[],"cardno":[],"sex":[],"folk":[],"birthday":[],"address":[],"issue_authority":"安溪县公安局","valid_period":"2008.06.16-2018.06.16","header_pic":[]}
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
             * name : []
             * cardno : []
             * sex : []
             * folk : []
             * birthday : []
             * address : []
             * issue_authority : 安溪县公安局
             * valid_period : 2008.06.16-2018.06.16
             * header_pic : []
             */

            private String issue_authority;
            private String valid_period;
            private List<?> name;
            private List<?> cardno;
            private List<?> sex;
            private List<?> folk;
            private List<?> birthday;
            private List<?> address;
            private List<?> header_pic;

            public String getIssue_authority() {
                return issue_authority;
            }

            public void setIssue_authority(String issue_authority) {
                this.issue_authority = issue_authority;
            }

            public String getValid_period() {
                return valid_period;
            }

            public void setValid_period(String valid_period) {
                this.valid_period = valid_period;
            }

            public List<?> getName() {
                return name;
            }

            public void setName(List<?> name) {
                this.name = name;
            }

            public List<?> getCardno() {
                return cardno;
            }

            public void setCardno(List<?> cardno) {
                this.cardno = cardno;
            }

            public List<?> getSex() {
                return sex;
            }

            public void setSex(List<?> sex) {
                this.sex = sex;
            }

            public List<?> getFolk() {
                return folk;
            }

            public void setFolk(List<?> folk) {
                this.folk = folk;
            }

            public List<?> getBirthday() {
                return birthday;
            }

            public void setBirthday(List<?> birthday) {
                this.birthday = birthday;
            }

            public List<?> getAddress() {
                return address;
            }

            public void setAddress(List<?> address) {
                this.address = address;
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
