package com.dhw.two.model;

import com.dhw.two.bean.ShopBean;
import com.dhw.two.okhttp.AbstractUiCallBack;
import com.dhw.two.okhttp.OkhttpUtils;

public class MainModel {



    public void getData(final MainModelCallBack callBack){


        OkhttpUtils.getInstance().asy(null, "http://120.27.23.105/product/getCarts?uid=100", new AbstractUiCallBack<ShopBean>() {
            @Override
            public void success(ShopBean bean) {

                callBack.success(bean);
            }

            @Override
            public void failure(Exception e) {

                callBack.failure(e);
            }
        });

    }


}
