package com.example.think.waterworksapp.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.GridView;
import android.widget.ListView;

/**
 * Created by Think on 2016/8/18.
 */
public class SetListViewAndGridViewHeightUtils {

    public static void setListViewHeight(ListView listView){
        // 获取ListView对应的Adapter

        Adapter listAdapter =  listView.getAdapter();

        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }



    public static void setGridViewHeight(GridView gridView){
        Adapter adapter = gridView.getAdapter();
        if (adapter == null) {
            return;
        }
        View itemView = adapter.getView(0,null,gridView);
        itemView.measure(0,0);
        int ItemHeight = itemView.getMeasuredHeight();
        int count = adapter.getCount();
        int lineLength = (count/4)+1;
        int verticalSpacing = gridView.getVerticalSpacing();
        int totaHeight = (ItemHeight+verticalSpacing)*lineLength;
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totaHeight;
        gridView.setLayoutParams(params);
    }
}
