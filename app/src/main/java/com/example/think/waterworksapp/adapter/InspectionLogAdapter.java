package com.example.think.waterworksapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.think.waterworksapp.R;
import com.example.think.waterworksapp.bean.InspectionLogBean;
import com.example.think.waterworksapp.utils.DateFormatUtlis;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Think on 2016/9/26.
 */

public class InspectionLogAdapter extends ArrayAdapter<InspectionLogBean> {
    private final int LAYOUT_ID = R.layout.inspection_log_list_item;
    private final String IP = "http://36.110.36.118:8081/";
    private Context context;
    public InspectionLogAdapter(Context context, ArrayList<InspectionLogBean> date) {
        super(context, -1,date);
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        InspectionLogBean inspectionLogBean = getItem(position);
        if (convertView==null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(LAYOUT_ID,parent,false);
            holder.deviceImg = (ImageView)convertView.findViewById(R.id.inspection_device_img);
            holder.deviceNameTxt = (TextView)convertView.findViewById(R.id.inspection_device_txt);
            holder.inspectionManNameTxt = (TextView)convertView.findViewById(R.id.inspection_person_value);
            holder.inspectionTimeTxt = (TextView)convertView.findViewById(R.id.inspection_time);
            holder.inspectionContent = (TextView)convertView.findViewById(R.id.inspection_content_value);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        String imgUrl = IP + inspectionLogBean.getImageUrl();
        Picasso.with(context).load(imgUrl)
                .resize(80,80)
                .placeholder(R.drawable.start_load_img_logo)
                .error(R.drawable.error_load_img_logo)
                .into(holder.deviceImg);
        holder.deviceNameTxt.setText(inspectionLogBean.getDeviceName());
        holder.inspectionManNameTxt.setText(inspectionLogBean.getUserName());
        holder.inspectionTimeTxt.setText(DateFormatUtlis.format(inspectionLogBean.getInspectionDate()));
        holder.inspectionContent.setText(inspectionLogBean.getInspectionContext());
        return convertView;
    }

    private class ViewHolder{
        ImageView deviceImg;
        TextView deviceNameTxt;
        TextView inspectionManNameTxt;
        TextView inspectionTimeTxt;
        TextView inspectionContent;
    }
}
