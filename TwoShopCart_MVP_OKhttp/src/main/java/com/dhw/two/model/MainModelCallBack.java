package com.dhw.two.model;

import com.dhw.two.bean.ShopBean;

public interface MainModelCallBack {


    public void success(ShopBean bean);
    public void failure(Exception e);

}
