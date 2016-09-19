package com.example.think.waterworksapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.think.waterworksapp.R;
import com.example.think.waterworksapp.bean.SelectOperationBtnBean;

import java.util.ArrayList;


/**
 * Created by Think on 2016/8/11.
 */
public class SelectOperationGridAdapter extends ArrayAdapter<SelectOperationBtnBean> {

    private final int layoutResourcesId = R.layout.select_operation_grid_item;

    public SelectOperationGridAdapter(Context context, int resource, ArrayList<SelectOperationBtnBean> data) {
        super(context,-1,data);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder hodle;
        SelectOperationBtnBean data = getItem(position);
        if (convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(layoutResourcesId,parent,false);
            hodle = new ViewHolder();
            hodle.icon = (ImageView)convertView.findViewById(R.id.operation_item_btn_img);
            hodle.title = (TextView)convertView.findViewById(R.id.operation_item_btn_title);
            convertView.setTag(hodle);
        }else{
            hodle = (ViewHolder)convertView.getTag();
        }
        hodle.icon.setImageResource(data.getImgResourcesId());
        hodle.title.setText(data.getTitle());
        return convertView;
    }

    private class ViewHolder{
        ImageView icon;
        TextView title;
    }
}
