package com.dhw.shopcart.bean;

import java.util.ArrayList;

/**
 * Created by Zzw on 2017/10/25.
 */

public class Group {
    private String name;
    private boolean check;
    private ArrayList<Child> children;

    public Group(String name, boolean check,ArrayList<Child> children) {
        this.name = name;
        this.check = check;
        this.children = children;
    }

    public Group() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
    public void setChildren(ArrayList<Child> children){
        this.children = children;
    }
    public ArrayList<Child> getChildren(){
        return children;
    }

    @Override
    public String toString() {
        return "Group{" +
                "name='" + name + '\'' +
                ", check=" + check +
                '}';
    }
}