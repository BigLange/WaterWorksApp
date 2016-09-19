package com.example.think.waterworksapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.think.waterworksapp.R;
import com.example.think.waterworksapp.bean.AcquisitionSensorDataBean;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Think on 2016/9/18.
 */

public class MonitorPointGridAdapter extends ArrayAdapter<AcquisitionSensorDataBean>{
    private final int LAYOUT_ID = R.layout.acquisition_sensor_data_item;

    private LayoutInflater inflater;
    private HashMap<Long,String> colorMap;

    public MonitorPointGridAdapter(Context context, ArrayList<AcquisitionSensorDataBean> data, HashMap<Long,String> colorMap) {
        super(context, -1,data);
        inflater = LayoutInflater.from(context);
        this.colorMap = colorMap;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AcquisitionSensorDataBean dataBean = getItem(position);
        ViewHolder holder = null;
        if (convertView==null){
            holder = new ViewHolder();
            convertView = inflater.inflate(LAYOUT_ID,parent,false);
            holder.colorView = convertView.findViewById(R.id.acquisition_sensor_data_color_view);
            holder.titleTxt = (TextView)convertView.findViewById(R.id.acquisition_sensor_data_title_txt);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.titleTxt.setText(dataBean.getLabel());
        String colorStr = colorMap.get(dataBean.getId());
        holder.colorView.setBackgroundColor(Color.parseColor(colorStr));
        return convertView;
    }

    private class ViewHolder{
        View colorView;
        TextView titleTxt;
    }
}
