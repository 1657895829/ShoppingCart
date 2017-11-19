package com.dhw.shopcart.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.dhw.shopcart.R;
import com.dhw.shopcart.bean.Group;
import java.util.ArrayList;

public class MyAdapter extends BaseExpandableListAdapter {
    Context context;
    ArrayList<Group> groups;

    public MyAdapter(Context context, ArrayList<Group> groups) {
        this.context = context;
        this.groups = groups;
    }

    //监听加减事件回调接口
    public interface onNumChangeListener{
        void onNumChange(int groupID,int childID,boolean isAdd);
    }
    private onNumChangeListener mOnNumChangeListener;
    public void setOnNumChangeListener(onNumChangeListener mOnNumChangeListener){
        this.mOnNumChangeListener = mOnNumChangeListener;
    }

    //监听多选框点击事件回调接口。
    public interface onCheckChangeListener{
        void onGroupClick(int groupID);
        void onChildClick(int groupID,int childID);
    }
    private onCheckChangeListener mOnCheckChangeListener;
    public void setmOnCheckChangeListener(onCheckChangeListener mOnCheckChangeListener){
        this.mOnCheckChangeListener = mOnCheckChangeListener;
    }

    //监听价格需要更新回调接口
    public interface onShouldChangeMoneyListener{
        void onShouldChnageMoney();
    }
    private onShouldChangeMoneyListener mOnShouldChangeMoneyListener;
    public void setmOnShouldChangeMoneyListener(onShouldChangeMoneyListener mOnShouldChangeMoneyListener){
        this.mOnShouldChangeMoneyListener = mOnShouldChangeMoneyListener;
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return groups.get(i).getChildren().size();
    }

    @Override
    public Object getGroup(int i) {
        return groups.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return groups.get(i).getChildren().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int i, boolean b, View view, ViewGroup viewGroup) {
        GroupHolder holder = null;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.group_item,viewGroup,false);
            holder  = new GroupHolder();
            holder.checkBox = (CheckBox) view.findViewById(R.id.group_check);
            view.setTag(holder);
        }else{
            holder = (GroupHolder) view.getTag();
        }
        holder.checkBox.setText(groups.get(i).getName());
        holder.checkBox.setChecked(groups.get(i).isCheck());
        if (holder.checkBox != null){
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnCheckChangeListener.onGroupClick(i);
                    mOnShouldChangeMoneyListener.onShouldChnageMoney();
                }
            });
        }
        return view;
    }

    @Override
    public View getChildView(final int i,final int i1, boolean b, View view, ViewGroup viewGroup) {
        ChildHolder holder = null;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.child_item,viewGroup,false);
            holder = new ChildHolder();
            holder.checkBox = (CheckBox) view.findViewById(R.id.child_check);
            holder.imageView = (ImageView) view.findViewById(R.id.child_img);
            holder.name = (TextView) view.findViewById(R.id.child_name);
            holder.num = (TextView) view.findViewById(R.id.child_num);
            holder.jian = (TextView) view.findViewById(R.id.child_jian);
            holder.jia = (TextView) view.findViewById(R.id.child_jia);
            holder.price = (TextView) view.findViewById(R.id.child_price);
            view.setTag(holder);
        }else{
            holder = (ChildHolder) view.getTag();
        }
        holder.checkBox.setChecked(groups.get(i).getChildren().get(i1).isCheck());
        holder.imageView.setImageResource(R.mipmap.ic_launcher);
        holder.name.setText(groups.get(i).getChildren().get(i1).getName());
        holder.num.setText(groups.get(i).getChildren().get(i1).getNum()+"");
        holder.price.setText(groups.get(i).getChildren().get(i1).getPrice()+"");
        if (holder.jian!=null){
            holder.jian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnNumChangeListener.onNumChange(i,i1,false);
                    mOnShouldChangeMoneyListener.onShouldChnageMoney();
                }
            });
        }
        if (holder.jia!=null){
            holder.jia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnNumChangeListener.onNumChange(i,i1,true);
                    mOnShouldChangeMoneyListener.onShouldChnageMoney();
                }
            });
        }
        if (holder.checkBox!=null){
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnCheckChangeListener.onChildClick(i,i1);
                    mOnShouldChangeMoneyListener.onShouldChnageMoney();
                }
            });
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
    class GroupHolder {
        CheckBox checkBox;
    }
    class ChildHolder{
        CheckBox checkBox;
        ImageView imageView;
        TextView name,num,jian,jia,price;
    }
}