package com.example.think.waterworksapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.think.waterworksapp.R;
import com.example.think.waterworksapp.bean.InspectionItemBean;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Think on 2016/8/16.
 */
public class EquipmentSubmitAdapter extends RecyclerView.Adapter<EquipmentSubmitAdapter.MyHolder> {

    private HashMap<String,Boolean> submitValue = new HashMap<>();

    private final int LAYOUT_ID = R.layout.equipment_submit_recycler_item;
    private final int RADIO_BUTTON_TRUE = R.id.equipment_run_state_submit_item_radio_true;
    private final int RADIO_BUTTON_FALSE = R.id.equipment_run_state_submit_item_radio_false;
    private ArrayList<InspectionItemBean> data;
    private Context context;

    public EquipmentSubmitAdapter(Context context, ArrayList<InspectionItemBean> data){
        this.context = context;
        this.data = data;
        for (InspectionItemBean inspectionItemBean:data){//初始化数据
            submitValue.put(inspectionItemBean.getItem(),true);
        }
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(LAYOUT_ID,parent,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        InspectionItemBean inspectionItem = data.get(position);
        holder.radioButton.setOnCheckedChangeListener(new MyCheckedChangeListener(position));
        holder.titleTxt.setText(inspectionItem.getStandardContext());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public HashMap<String,Boolean> getSubmitValue(){
        return submitValue;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView titleTxt;
        RadioGroup radioButton;

        public MyHolder(View itemView) {
            super(itemView);
            titleTxt = (TextView)itemView.findViewById(R.id.equipment_run_state_submit_item_title);
            radioButton = (RadioGroup)itemView.findViewById(R.id.equipment_run_state_submit_item_radioGroup);

        }
    }

    class MyCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        private int position;

        public MyCheckedChangeListener(int position){
            this.position = position;
        }


        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            InspectionItemBean inspectionItemBean = data.get(position);
            String inspectionItem = inspectionItemBean.getItem();
            if (i == RADIO_BUTTON_TRUE){
                submitValue.put(inspectionItem,true);
            }else if (i == RADIO_BUTTON_FALSE){
                submitValue.put(inspectionItem+"",false);
            }
        }
    }
}
