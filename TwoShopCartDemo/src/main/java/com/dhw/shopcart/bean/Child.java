package com.dhw.shopcart.bean;
public class Child {
    private String name;
    private String img;
    private int num;
    private boolean check;
    private int price;

    public Child(String name, String img, int num, boolean check, int price) {
        this.name = name;
        this.img = img;
        this.num = num;
        this.check = check;
        this.price = price;
    }

    public Child() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Child{" +
                "name='" + name + '\'' +
                ", img='" + img + '\'' +
                ", num=" + num +
                ", check=" + check +
                ", price=" + price +
                '}';
    }
}
