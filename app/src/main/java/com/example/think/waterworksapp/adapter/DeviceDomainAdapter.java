package com.example.think.waterworksapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.think.waterworksapp.R;
import com.example.think.waterworksapp.bean.DeviceDomainBean;
import com.example.think.waterworksapp.bean.EquipmentBeanTest;

import java.util.ArrayList;
/**
 * Created by Think on 2016/8/15.
 */
public class DeviceDomainAdapter extends ArrayAdapter<DeviceDomainBean> {

    private final int LAYOUT_ID = R.layout.select_staff_time_moban;
    private final int TXT_ID = R.id.mrele_frag2_grid_item_btn;

    private Context context;

    public DeviceDomainAdapter(Context context, ArrayList<DeviceDomainBean> data) {
        super(context, -1, data);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DeviceDomainBean domain = getItem(position);
        ViewHolder holder;
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(LAYOUT_ID,parent,false);
            holder = new ViewHolder();
            holder.title = (TextView)convertView.findViewById(TXT_ID);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        holder.title.setText(domain.getName());
        return convertView;
    }

    private class ViewHolder{
        TextView title;
    }
}
