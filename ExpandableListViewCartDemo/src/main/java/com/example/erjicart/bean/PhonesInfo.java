package com.example.erjicart.bean;

import java.util.List;

public class PhonesInfo {
    public String flag;
    public String code;
    public List<DataInfo> data;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<DataInfo> getData() {
        return data;
    }

    public void setData(List<DataInfo> data) {
        this.data = data;
    }

    public static class DataInfo {
        public String title;
        public List<DatasInfo> datas;
        public boolean allCheck = false;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<DatasInfo> getDatas() {
            return datas;
        }

        public void setDatas(List<DatasInfo> datas) {
            this.datas = datas;
        }

        public boolean isAllCheck() {
            return allCheck;
        }

        public void setAllCheck(boolean allCheck) {
            this.allCheck = allCheck;
        }

        public static class DatasInfo {
            public int price;
            public String type_name;
            public String msg;
            public String add_time;
            public boolean itemCheck = false;

            public int getPrice() {
                return price;
            }

            public void setPrice(int price) {
                this.price = price;
            }

            public String getType_name() {
                return type_name;
            }

            public void setType_name(String type_name) {
                this.type_name = type_name;
            }

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }

            public String getAdd_time() {
                return add_time;
            }

            public void setAdd_time(String add_time) {
                this.add_time = add_time;
            }

            public boolean isItemCheck() {
                return itemCheck;
            }

            public void setItemCheck(boolean itemCheck) {
                this.itemCheck = itemCheck;
            }
        }
    }
}
