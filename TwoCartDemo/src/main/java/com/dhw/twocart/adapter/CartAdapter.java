package com.dhw.twocart.adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.dhw.twocart.R;
import com.dhw.twocart.bean.CartBean;
import com.dhw.twocart.util.ImageLoaderUtil;
import com.dhw.twocart.view.CustomView;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

//购物车商品适配器
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ItemViewHolder> {
    private Context context;
    private List<CartBean.OrderDataBean.CartlistBean> list;

    public CartAdapter(Context context) {
        this.context = context;
    }

    //添加数据的方法
    public void add(List<CartBean.OrderDataBean.CartlistBean> list) {
        if (this.list == null) {
            this.list = new ArrayList<>();
        }
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item, null);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        /**
         * 适配器Adapter里面onBindViewHolder里面设置进入页面是否显示或隐藏商家
           根据position判断是第几个条目,然后显示默认的状态
         */
        if (position > 0) {
            if (list.get(position).getIsFirst() == 1) {
                //显示商家
                holder.shopName.setVisibility(View.VISIBLE);
                holder.shopCheckbox.setVisibility(View.VISIBLE);
                holder.shopName.setText(list.get(position).getShopName());
                holder.shopCheckbox.setChecked(list.get(position).isShopCheck());
            } else {
                // 隐藏商家
                holder.shopName.setVisibility(View.GONE);
                holder.shopCheckbox.setVisibility(View.GONE);
            }
        } else {
            //显示商家
            holder.shopName.setVisibility(View.VISIBLE);
            holder.shopCheckbox.setVisibility(View.VISIBLE);
            holder.shopName.setText(list.get(position).getShopName());
            holder.shopCheckbox.setChecked(list.get(position).isShopCheck());
        }

        //防止checkbox 滑动 错乱
        holder.checkbox.setChecked(list.get(position).isCheck());
        holder.title.setText(list.get(position).getProductName());
        holder.price.setText("￥ " + list.get(position).getPrice());

        //获取自定义view页面EditText输入框的值
        holder.customviewId.setEditText(list.get(position).getCount());
        ImageLoader.getInstance().displayImage(list.get(position).getDefaultPic(), holder.dianImage, ImageLoaderUtil.getDefaultOption());

        /**
         * 设置商铺前面的shopCheckbox的点击事件,.调用商品的 监听,(同一个,都是在Activity里面求和)
         */
        holder.shopCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.get(position).setShopCheck(holder.shopCheckbox.isChecked());
                for(int i=0;i<list.size();i++){
                    if(list.get(position).getShopId() == list.get(i).getShopId()){
                        list.get(i).setCheck(holder.shopCheckbox.isChecked());
                    }
                }

                notifyDataSetChanged();

                if(checkBoxListener != null){
                    checkBoxListener.check(position,holder.customviewId.getCurrentCount(),holder.checkbox.isChecked(),list);
                }
            }
        });

        /**
         * checkbox 点击事件  商品的checkbox
         */
        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.get(position).setCheck(holder.checkbox.isChecked());
                // 控制商家 checkbox 的状态
                for(int i=0;i<list.size();i++){
                    for(int j=0;j<list.size();j++){
                        //如果是同一家商铺的商品，并且其中一个商品是未选中，那么商铺的全选勾选取消
                        if(list.get(j).getShopId() == list.get(i).getShopId() && !list.get(j).isCheck()){
                            list.get(i).setShopCheck(false);
                            break;
                        } else {
                            //如果是同一家商铺的商品，并且所有商品是选中，那么商铺的选中全选勾选
                            list.get(i).setShopCheck(true);
                        }
                    }
                }
                notifyDataSetChanged();

                if(checkBoxListener != null){
                    checkBoxListener.check(position,holder.customviewId.getCurrentCount(),holder.checkbox.isChecked(),list);
                }
            }
        });

        //加减按钮的监听事件 ,更新当前条目数据价格
        holder.customviewId.setListener(new CustomView.ClickListener() {
            @Override
            public void click(int count) {
                //更改变化后的商品数量
                list.get(position).setCount(count);
                notifyDataSetChanged();

                if (customViewListener != null) {
                    customViewListener.click(count, list);
                }
            }
        });

        //删除按钮的监听事件 ,删除当前条目数据
        holder.btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                notifyDataSetChanged();

                if (delListener != null) {
                    delListener.del(position, list);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.shop_checkbox)
        CheckBox shopCheckbox;
        @BindView(R.id.shop_name)
        TextView shopName;

        @BindView(R.id.checkbox)
        CheckBox checkbox;
        @BindView(R.id.dianImage)
        ImageView dianImage;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.customviewId)
        CustomView customviewId;
        @BindView(R.id.btn_del)
        Button btnDel;
        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public List<CartBean.OrderDataBean.CartlistBean> getList() {
        return list;
    }

    //定义checkbox 点击事件的回调接口
    public CheckBoxListener checkBoxListener;

    public void setCheckBoxListener(CheckBoxListener listener) {
        this.checkBoxListener = listener;
    }

    public interface CheckBoxListener {
        public void check(int position, int count, boolean check, List<CartBean.OrderDataBean.CartlistBean> list);
    }

    //定义加减按钮 点击事件的回调接口
    public CustomViewListener customViewListener;

    public void setCustomViewListener(CustomViewListener customViewListener) {
        this.customViewListener = customViewListener;
    }

    public interface CustomViewListener {
        public void click(int count, List<CartBean.OrderDataBean.CartlistBean> list);
    }

    //定义删除按钮 点击事件的回调接口
    public DelListener delListener;

    public void setDelListener(DelListener delListener) {
        this.delListener = delListener;
    }

    public interface DelListener {
        public void del(int position, List<CartBean.OrderDataBean.CartlistBean> list);
    }
}