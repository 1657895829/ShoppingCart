package com.example.cart.adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.cart.R;
import com.example.cart.bean.CartBean;
import com.example.cart.util.ImageLoaderUtil;
import com.example.cart.view.CustomView;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
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
        //防止checkbox 滑动 错乱
        holder.checkbox.setChecked(list.get(position).isCheck());
        holder.title.setText(list.get(position).getProductName());
        holder.price.setText("￥ " + list.get(position).getPrice());

        //获取自定义view页面EditText输入框的值
        holder.customviewId.setEditText(list.get(position).getCount());
        ImageLoader.getInstance().displayImage(list.get(position).getDefaultPic(), holder.dianImage, ImageLoaderUtil.getDefaultOption());
        //复选按钮的选中点击事件,获取数据价格
        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.get(position).setCheck(holder.checkbox.isChecked());
                notifyDataSetChanged();
                if (checkBoxListener != null) {
                    checkBoxListener.check(position, holder.customviewId.getCurrentCount(), holder.checkbox.isChecked(), list);
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

                if (customViewListener != null){
                    customViewListener.click(count,list);
                }
            }
        });

        //删除按钮的监听事件 ,删除当前条目数据
        holder.btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                notifyDataSetChanged();

                if (delListener != null){
                    delListener.del(position,list);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.checkbox)
        CheckBox checkbox;
        @Bind(R.id.dianImage)
        ImageView dianImage;
        @Bind(R.id.price)
        TextView price;
        @Bind(R.id.customviewId)
        CustomView customviewId;
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.btn_del)
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