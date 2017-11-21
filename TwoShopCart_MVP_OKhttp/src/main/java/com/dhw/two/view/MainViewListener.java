package com.dhw.two.view;

import com.dhw.two.bean.ShopBean;

public interface MainViewListener {
    public void success(ShopBean bean);
    public void failure(Exception e);

}
