package com.dhw.jiajian;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private JiaJianQi adv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
    private void initView() {
        adv = (JiaJianQi) findViewById(R.id.adv_main);
        adv.OnJiaJianClickListener(new JiaJianQi.OnJiaJianClickListener() {
            @Override
            public void onAddClick(View v) {
                Log.i(TAG, "onAddClick: 执行");
                int origin = adv.getNumber();
                // if(origin<=4){
                origin++;
                //Toast.makeText(MainActivity.this, "最多不能多于5个", Toast.LENGTH_SHORT).show();
                adv.setNumber(origin);
                //}
            }
            @Override
            public void onDelClick(View v) {
                int origin = adv.getNumber();
                //if(origin==1){
                origin--;
                Toast.makeText(MainActivity.this, "最少不能小于1个", Toast.LENGTH_SHORT).show();
                adv.setNumber(origin);
                //}
            }
        });
    }
}
