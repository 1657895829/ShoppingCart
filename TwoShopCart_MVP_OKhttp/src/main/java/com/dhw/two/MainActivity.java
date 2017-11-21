package com.dhw.two;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import com.dhw.two.bean.ShopBean;
import com.dhw.two.presenter.MainPresenter;
import com.dhw.two.view.MainViewListener;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity implements MainViewListener {
    @Bind(R.id.third_recyclerview)
    RecyclerView thirdRecyclerview;
    @Bind(R.id.third_allselect)
    CheckBox checkBoxAll;
    @Bind(R.id.third_totalprice)
    TextView thirdTotalprice;
    @Bind(R.id.third_totalnum)
    TextView thirdTotalnum;
    private MainPresenter presenter;
    private ShopAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        presenter = new MainPresenter(this);
        presenter.getData();

        adapter = new ShopAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        thirdRecyclerview.setLayoutManager(manager);
        thirdRecyclerview.setAdapter(adapter);

        adapter.setListener(new ShopAdapter.UpdateUiListener() {
            @Override
            public void setTotal(String total, String num, boolean allCheck) {
                checkBoxAll.setChecked(allCheck);
                thirdTotalnum.setText("共计："+num+" 件");
                thirdTotalprice.setText("总价：￥"+total);
            }
        });
    }

    @Override
    public void success(ShopBean bean) {
        adapter.add(bean);
    }

    @Override
    public void failure(Exception e) {

        Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detach();
    }

    @OnClick(R.id.third_allselect)
    public void onViewClicked() {
        adapter.selectAll(checkBoxAll.isChecked());
    }
}
