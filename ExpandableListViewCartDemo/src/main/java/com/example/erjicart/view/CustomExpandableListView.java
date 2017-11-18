package com.example.erjicart.view;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

//二级列表页面
public class CustomExpandableListView extends ExpandableListView{
    public CustomExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

   //Integer.MAX_VALUE >>2,MeasureSpec.AT_MOST：解决ScrollView嵌套ListView和GridView冲突的方法
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /*
         最大模式（MeasureSpec.AT_MOST）
         就是父组件，能够给出的最大的空间，当前组件的长或宽最大只能为这么大，当然也可以比这个小。
        */
        int measureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, measureSpec);
    }
}