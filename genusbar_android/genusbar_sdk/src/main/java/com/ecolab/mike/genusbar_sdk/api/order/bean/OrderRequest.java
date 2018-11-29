package com.ecolab.mike.genusbar_sdk.api.order.bean;

import java.io.Serializable;

public class OrderRequest {


        private int id;
        private String name;
        private String email;
        private int resStatus;
        private String remark;
        private String resDate;
        private String number;

        public OrderRequest(String name, String email, String remark, String resDate) {
            this.id = 0;
            this.name = name;
            this.email = email;
            this.resStatus = 0;
            this.remark = remark;
            this.resDate = resDate;
            this.number = "0";
        }

        public int getId() {
            return id;
        }

        public String getNumber() {
            return number;
        }

        public int getRestatus() {
            return resStatus;
        }

        public String getResDate() {
            return resDate;
        }

        public String getEmail() {
            return email;
        }

        public String getName() {
            return name;
        }

        public String getRemark() {
            return remark;
        }


}
