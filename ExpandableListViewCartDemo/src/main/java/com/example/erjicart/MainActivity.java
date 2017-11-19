package com.example.erjicart;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;
import com.example.erjicart.bean.PhonesInfo;
import com.example.erjicart.okhttp.OkHttp3Utils;
import com.example.erjicart.view.CustomExpandableListView;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener{
    private CustomExpandableListView celv;
    private CheckBox checkBox;
    private TextView tv_num;
    private TextView tv_price;
    private PhonesInfo phonesInfo;
    private PhoneAdapter adapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case 1:
                    String str = (String) msg.obj;
                    Log.i("xxx", str.toString());
                    Gson gson = new Gson();
                    phonesInfo = gson.fromJson(str, PhonesInfo.class);
                    //适配数据
                    adapter = new PhoneAdapter();
                    celv.setAdapter(adapter);
                    int count = celv.getCount();
                    for (int i = 0; i < count; i++) {
                        //展开
                        celv.expandGroup(i);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//获取数据
        getData();
        //获取控件ID
        celv = (CustomExpandableListView) findViewById(R.id.celv);
        checkBox = (CheckBox) findViewById(R.id.checkbox2);
        tv_num = (TextView) findViewById(R.id.tv_num);
        tv_price = (TextView) findViewById(R.id.tv_price);
        //去掉箭头
        celv.setGroupIndicator(null);
        //CheckBox点击事件
        checkBox.setOnClickListener(this);

    }


    //获取数据
    private void getData() {
        String url = "http://result.eolinker.com/iYXEPGn4e9c6dafce6e5cdd23287d2bb136ee7e9194d3e9?uri=evaluation";

        OkHttp3Utils.doGet(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.i("xxx", string.toString());
                Message message = handler.obtainMessage(1, string);
                message.sendToTarget();
            }
        });
    }

    //全选的监听
    @Override
    public void onClick(View v) {
        if (((CheckBox) v).isChecked()) {
            List<PhonesInfo.DataInfo> data = phonesInfo.getData();
            for (int i = 0; i < data.size(); i++) {
                Log.i("xxx", data.get(i).isAllCheck() + "");
                PhonesInfo.DataInfo groupDatas = phonesInfo.getData().get(i);
                groupDatas.setAllCheck(true);
                List<PhonesInfo.DataInfo.DatasInfo> datas = data.get(i).getDatas();
                for (int j = 0; j < datas.size(); j++) {
                    Log.i("xxx", datas.get(j).isItemCheck() + "");
                    List<PhonesInfo.DataInfo.DatasInfo> childDatas = groupDatas.getDatas();
                    for (PhonesInfo.DataInfo.DatasInfo childData : childDatas) {
                        childData.setItemCheck(true);
                    }
                }
            }
            //刷新界面
            notifyCheckAdapter();
        } else {
            List<PhonesInfo.DataInfo> data = phonesInfo.getData();
            for (int i = 0; i < data.size(); i++) {
                Log.i("xxx", data.get(i).isAllCheck() + "");
                PhonesInfo.DataInfo groupDatas = phonesInfo.getData().get(i);
                groupDatas.setAllCheck(false);
                List<PhonesInfo.DataInfo.DatasInfo> datas = data.get(i).getDatas();
                for (int j = 0; j < datas.size(); j++) {
                    Log.i("xxx", datas.get(j).isItemCheck() + "");
                    List<PhonesInfo.DataInfo.DatasInfo> childDatas = groupDatas.getDatas();
                    for (PhonesInfo.DataInfo.DatasInfo childData : childDatas) {
                        childData.setItemCheck(false);
                    }
                }
            }
            //刷新界面
            notifyCheckAdapter();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

//适配器
private class PhoneAdapter implements ExpandableListAdapter {

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return phonesInfo.getData().size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return phonesInfo.getData().get(groupPosition).getDatas().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return phonesInfo.getData().get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return phonesInfo.getData().get(groupPosition).getDatas().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    //一级
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = View.inflate(MainActivity.this, R.layout.item_parent, null);
        CheckBox cb_parent = (CheckBox) view.findViewById(R.id.cb_parent);
        TextView tv_number = (TextView) view.findViewById(R.id.tv_number);
        tv_number.setText(phonesInfo.getData().get(groupPosition).getTitle());
        if (phonesInfo.getData().get(groupPosition).isAllCheck()) {
            cb_parent.setChecked(true);
        } else {
            cb_parent.setChecked(false);
        }
        //一级监听
        cb_parent.setOnClickListener(new OnGroupClickListener(groupPosition, cb_parent));

        return view;
    }

    //二级
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = View.inflate(MainActivity.this, R.layout.item_child, null);
        TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
        TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
        TextView tv_pri = (TextView) view.findViewById(R.id.tv_pri);
        TextView tv_msg = (TextView) view.findViewById(R.id.tv_msg);
        CheckBox cb_child = (CheckBox) view.findViewById(R.id.cb_child);
        tv_pri.setText("￥"+phonesInfo.getData().get(groupPosition).getDatas().get(childPosition).getPrice());
        tv_content.setText(phonesInfo.getData().get(groupPosition).getDatas().get(childPosition).getType_name());
        tv_time.setText(phonesInfo.getData().get(groupPosition).getDatas().get(childPosition).getAdd_time());
        tv_msg.setText(phonesInfo.getData().get(groupPosition).getDatas().get(childPosition).getMsg());
        if (phonesInfo.getData().get(groupPosition).getDatas().get(childPosition).isItemCheck()) {
            cb_child.setChecked(true);

        } else {
            cb_child.setChecked(false);
        }
        cb_child.setOnClickListener(new OnChildCheckListener(groupPosition, childPosition, cb_child));
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }
}

//一级监听
private class OnGroupClickListener implements View.OnClickListener {
    int groupPosition;
    CheckBox cb_parent;

    public OnGroupClickListener(int groupPosition, CheckBox cb_parent) {
        this.cb_parent = cb_parent;
        this.groupPosition = groupPosition;
    }


    @Override
    public void onClick(View v) {
        if (((CheckBox) v).isChecked()) {
            //一级全选
            setCheck(true);
        } else {
            //取消全选
            setCheck(false);
            checkBox.setChecked(false);
        }
        notifyCheckAdapter();
    }

    public void setCheck(boolean checkFlag) {
        PhonesInfo.DataInfo groupDatas = phonesInfo.getData().get(groupPosition);
        List<PhonesInfo.DataInfo> data = phonesInfo.getData();
        //一级状态
        groupDatas.setAllCheck(checkFlag);

        //全选状态判断
        int num = 0;
        for (int i = 0; i < data.size(); i++) {
            boolean allCheck = data.get(i).isAllCheck();
            if (!allCheck) {
                num++;
            }
        }
        if (num == 0) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }

        //二级状态
        List<PhonesInfo.DataInfo.DatasInfo> childDatas = groupDatas.getDatas();
        for (PhonesInfo.DataInfo.DatasInfo childData : childDatas) {
            //二级状态
            childData.setItemCheck(checkFlag);
        }
    }
}

//二级监听
private class OnChildCheckListener implements View.OnClickListener {
    int groupPosition;
    int childPosition;
    CheckBox cb_child;

    public OnChildCheckListener(int groupPosition, int childPosition, CheckBox cb_child) {
        this.cb_child = cb_child;
        this.groupPosition = groupPosition;
        this.childPosition = childPosition;
    }

    @Override
    public void onClick(View v) {
        if (((CheckBox) v).isChecked()) {
            //子选中
            phonesInfo.getData().get(groupPosition).getDatas().get(childPosition).setItemCheck(true);
        } else {
            //子未选中
            phonesInfo.getData().get(groupPosition).getDatas().get(childPosition).setItemCheck(false);

        }
        //二级联动一级状态
        setParentCheckFlag();

        //检测状态 二级联动全选
        int num = 0;
        for (int i = 0; i < phonesInfo.getData().size(); i++) {
            boolean allCheck = phonesInfo.getData().get(i).isAllCheck();
            if (!allCheck) {
                num++;
            }
        }
        if (num == 0) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
    }

    //二级联动一级状态
    private void setParentCheckFlag() {

        PhonesInfo.DataInfo dataInfo = phonesInfo.getData().get(groupPosition);
        List<PhonesInfo.DataInfo.DatasInfo> datasInfos = dataInfo.getDatas();
        for (int i = 0; i < datasInfos.size(); i++) {
            if (!datasInfos.get(i).isItemCheck()) {
                //子未选中 父取消选中
                dataInfo.setAllCheck(false);
                notifyCheckAdapter();
                return;
            }
            if (i == datasInfos.size() - 1) {
                //子选中 父选中
                dataInfo.setAllCheck(true);
                notifyCheckAdapter();

                return;
            }
        }
        //没出现全选或者取消全选的时候执行的
        sum();
    }
}

    //统计数量和价格
    private void sum() {
        int num = 0;
        int price = 0;

        List<PhonesInfo.DataInfo> data = phonesInfo.getData();
        for (PhonesInfo.DataInfo parentData : data) {
            for (PhonesInfo.DataInfo.DatasInfo child : parentData.getDatas()) {
                if (child.isItemCheck()) {
                    num++;
                    price += child.getPrice();
                }
            }
        }
        tv_num.setText("结算(" + num + ")");
        tv_price.setText("¥" + price);
    }

    //刷新适配器界面
    private void notifyCheckAdapter() {
        sum();
        celv.setAdapter(adapter);
        int count = celv.getCount();
        for (int i = 0; i < count; i++) {
            celv.expandGroup(i);
        }
    }
}