package com.example.think.waterworksapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.think.waterworksapp.R;
import com.example.think.waterworksapp.bean.DeviceDomainBean;
import com.example.think.waterworksapp.bean.EquipmentMsgBean;

import java.util.ArrayList;

/**
 * Created by Think on 2016/9/7.
 */
public class DeviceAdapter extends ArrayAdapter<EquipmentMsgBean> {

    private final int LAYOUT_ID = R.layout.select_staff_time_moban;
    private final int TXT_ID = R.id.mrele_frag2_grid_item_btn;
    private Context context;

    public DeviceAdapter(Context context,ArrayList<EquipmentMsgBean> data) {
        super(context, -1,data);
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EquipmentMsgBean equipmentMsgBean = getItem(position);
        ViewViewHolder holder;
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(LAYOUT_ID,parent,false);
            holder = new ViewViewHolder();
            holder.title = (TextView)convertView.findViewById(TXT_ID);
            convertView.setTag(holder);
        }else{
            holder = (ViewViewHolder)convertView.getTag();
        }
        holder.title.setText(equipmentMsgBean.getLabel());
        return convertView;
    }

    private class ViewViewHolder{
        TextView title;
    }
}
