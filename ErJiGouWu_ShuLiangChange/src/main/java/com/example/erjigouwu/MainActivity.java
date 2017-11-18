package com.example.erjigouwu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.erjigouwu.adapter.MyAdpater;

public class MainActivity extends AppCompatActivity {
    private ExpandableListView listview;
    private MyAdpater adpater;
    private TextView checked_shop;
    private TextView price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview = (ExpandableListView) findViewById(R.id.listview);
        adpater = new MyAdpater(this);
        listview.setAdapter(adpater);
        final CheckBox checkAll = (CheckBox) findViewById(R.id.checkAll);
        price = (TextView) findViewById(R.id.price);
        checked_shop = (TextView) findViewById(R.id.checked_shop);
        checkAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置商品全部选中
                adpater.checkAllShop(checkAll.isChecked());
                //计算选中的价格和数量
                String shopPrice = adpater.getShopPrice();
                //判断商品是否全部选中
                boolean b = adpater.selectAll();

                String[] split = shopPrice.split(",");
                price.setText(split[0]);
                checked_shop.setText(split[1]);
                checkAll.setChecked(b);
            }
        });
        adpater.getAdapterData(new MyAdpater.AdapterData() {
            @Override
            public void Data(View v, String str, boolean b) {
                String[] split = str.split(",");
                price.setText(split[0]);
                checked_shop.setText(split[1]);
                checkAll.setChecked(b);
            }
        });

        checkAll.setChecked(adpater.selectAll());
        adpater.notifyDataSetChanged();
    }
}
