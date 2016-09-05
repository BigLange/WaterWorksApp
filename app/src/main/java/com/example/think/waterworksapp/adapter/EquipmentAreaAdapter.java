package com.example.think.waterworksapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.think.waterworksapp.R;
import com.example.think.waterworksapp.bean.EquipmentBeanTest;

import java.util.ArrayList;
/**
 * Created by Think on 2016/8/15.
 */
public class EquipmentAreaAdapter extends ArrayAdapter<EquipmentBeanTest> {

    private final int LAYOUT_ID = R.layout.select_staff_time_moban;
    private final int TXT_ID = R.id.mrele_frag2_grid_item_btn;

    private Context context;

    public EquipmentAreaAdapter(Context context, ArrayList<EquipmentBeanTest> data) {
        super(context, -1, data);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EquipmentBeanTest staffBean = getItem(position);
        ViewHodel hodel;
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(LAYOUT_ID,parent,false);
            hodel = new ViewHodel();
            hodel.title = (TextView)convertView.findViewById(TXT_ID);
            convertView.setTag(hodel);
        }else{
            hodel = (ViewHodel)convertView.getTag();
        }
        hodel.title.setText(staffBean.getTeamName());
        return convertView;
    }

    private class ViewHodel{
        TextView title;
    }
}
