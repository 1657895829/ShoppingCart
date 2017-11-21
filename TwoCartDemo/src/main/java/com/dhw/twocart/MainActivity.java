package com.dhw.twocart;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.CheckBox;
import android.widget.TextView;
import com.dhw.twocart.adapter.CartAdapter;
import com.dhw.twocart.bean.CartBean;
import com.dhw.twocart.util.StringUtil;
import com.google.gson.Gson;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//主页面功能代码
public class MainActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.selectAll)
    CheckBox selectAll;
    @BindView(R.id.sumCount)
    TextView sumCount;
    @BindView(R.id.sumPrice)
    TextView sumPrice;
    private List<CartBean.OrderDataBean.CartlistBean> list = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getData();      //获取数据
        selectAll.setTag(1);     //默认复选按钮都为 未选中 状态

        //设置布局管理器,适配器
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new CartAdapter(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        setFirst(list);         //是否需要 显示商家
        adapter.add(list);     //添加数据

        //适配器单个条目复选按钮的点击事件,价格变化
        adapter.setCheckBoxListener(new CartAdapter.CheckBoxListener() {
            @Override
            public void check(int position, int count, boolean check, List<CartBean.OrderDataBean.CartlistBean> list) {
                sumPrice(list);
            }
        });

        //适配器单个条目加减号按钮的点击事件,价格变化
        adapter.setCustomViewListener(new CartAdapter.CustomViewListener() {
            @Override
            public void click(int count, List<CartBean.OrderDataBean.CartlistBean> list) {
                sumPrice(list);
            }
        });

        //适配器单个条目删除按钮的点击事件,总价变化
        adapter.setDelListener(new CartAdapter.DelListener() {
            @Override
            public void del(int position, List<CartBean.OrderDataBean.CartlistBean> list) {
                sumPrice(list);
            }
        });
    }

    //模拟网络请求数据
    public void getData() {
        try {
            InputStream inputStream = getAssets().open("shop.json");
            String data = StringUtil.streamToString(inputStream,"utf-8");
            Gson gson = new Gson();
            CartBean cartBean = gson.fromJson(data, CartBean.class);
            for (int i = 0; i < cartBean.getOrderData().size(); i++) {
                int length = cartBean.getOrderData().get(i).getCartlist().size();
                for (int j = 0; j < length; j++) {
                    list.add(cartBean.getOrderData().get(i).getCartlist().get(j));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Activity里面写一个setFirst()方法,并调用, 设置字段里面的isfirst的值
     * 是否需要 显示商家
     */
    private void setFirst(List<CartBean.OrderDataBean.CartlistBean> list) {
        if (list.size() > 0) {
            //如果是第一条数据 就设置是第一条为1
            list.get(0).setIsFirst(1);
            //从第二条开始遍历
            for (int i = 1; i < list.size(); i++) {
                //如果两个挨着的是同一个商家的
                if (list.get(i).getShopId() == list.get(i - 1).getShopId()) {
                    //就设置为2  不是第一个条目 ,隐藏商家
                    list.get(i).setIsFirst(2);
                } else {
                    //设置为1  是第一个条目 显示商家
                    list.get(i).setIsFirst(1);
                }
            }
        }
    }

    //计算商品总价,设置没选择商品时总价为0，商品数量为0
    float price = 0;
    int count;
    public void sumPrice(List<CartBean.OrderDataBean.CartlistBean> list) {
        price = 0;
        count = 0;
        boolean allCheck = true;
        //遍历数据源
        for (CartBean.OrderDataBean.CartlistBean bean : list) {
            if (bean.isCheck()) {        //商品选中时，计算总价
                price += bean.getPrice() * bean.getCount();
                count += bean.getCount();
            } else {                    //只要有一个商品未选中，全选按钮 应该设置成 未选中
                allCheck = false;
            }
        }

        //设置商品总数及总价
        sumPrice.setText("总价：￥" + price);
        sumCount.setText("商品总数：" + count + "  件");

        //全选按钮是否选中时的tag值
        if (allCheck) {
            selectAll.setTag(2);
            selectAll.setChecked(true);
        } else {
            selectAll.setTag(1);
            selectAll.setChecked(false);
        }
    }

    /**
     * 全选按钮 点击事件
     * 点击全选按钮设置所有复选框状态为true，否则为false
     * 1：未选中，2：选中
     */
    boolean select = false;
    @OnClick(R.id.selectAll)
    public void onViewClicked() {
            int tag = (Integer) selectAll.getTag();
            if (tag == 1) {
                selectAll.setTag(2);
                select = true;
            } else {
                selectAll.setTag(1);
                select = false;
            }

            //遍历数据源,根据tag值设置选择状态，然后刷新改变后的数据，重新添加
            for (CartBean.OrderDataBean.CartlistBean bean : list) {
                bean.setCheck(select);
                bean.setShopCheck(select);
            }
            adapter.notifyDataSetChanged();
            sumPrice(adapter.getList());
        }
}
