package com.example.think.waterworksapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.example.think.waterworksapp.adapter.EquipmentAreaAdapter;
import com.example.think.waterworksapp.base_intface.GetEquipmentView;
import com.example.think.waterworksapp.base_intface.GetAcquisitionPointView;
import com.example.think.waterworksapp.bean.EquipmentBeanTest;
import com.example.think.waterworksapp.bean.EquipmentMsgBean;
import com.example.think.waterworksapp.custom_view.UpperActivity;
import com.example.think.waterworksapp.dialog.DialogManager;
import com.example.think.waterworksapp.model.GetAcquisitionPointModel;
import com.example.think.waterworksapp.model.GetEquipmentModel;
import com.example.think.waterworksapp.model.GetMonitoringPointDataModel;
import com.example.think.waterworksapp.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class SelectEquipmentActivity extends UpperActivity implements AdapterView.OnItemClickListener,GetEquipmentView{

    private ListView equipmentAreaListView;
    private ListView equipmentListView;
    private View currentEquipmentItem;
//    private DialogManager dialogManager;



    private ArrayList<EquipmentBeanTest> equipments;
    private ArrayList<EquipmentMsgBean> equipmentMsgData;
    private ArrayList<EquipmentMsgBean> acquisitionPointData;

    private ArrayAdapter<String> equipmentAdapter;
    private EquipmentAreaAdapter equipmentAreaAdapter;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAdapter();
    }

    private void initAdapter() {
        initValue();
        equipmentAreaAdapter = new EquipmentAreaAdapter(this,equipments);
        equipmentAreaListView.setAdapter(equipmentAreaAdapter);
    }

    private void initValue() {
        GetEquipmentModel model = new GetEquipmentModel(this,this);
//        model.getEquipmentMsg();
        equipments = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            EquipmentBeanTest equipmentBean = new EquipmentBeanTest();
            equipmentBean.setTeamName("机房" + i);
            ArrayList<String> timeArr = new ArrayList<>();
            timeArr.add("设备" + i);
            timeArr.add("设备" + i);
            timeArr.add("设备" + i);
            timeArr.add("设备" + i);
            timeArr.add("设备" + i);
            equipmentBean.setStaffName(timeArr);
            equipments.add(equipmentBean);

        }
    }

    @Override
    protected void initView() {
        equipmentAreaListView = (ListView) findViewById(R.id.class_equipment_area_list);
        equipmentListView = (ListView) findViewById(R.id.class_equipment_list);
    }

    protected void initEvent() {
        equipmentAreaListView.setOnItemClickListener(this);
        equipmentListView.setOnItemClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_equipment_select;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int viewId = parent.getId();
        if (viewId == R.id.class_equipment_area_list) {
            if (currentEquipmentItem != null) {
                currentEquipmentItem.setBackgroundColor(Color.parseColor("#e0e0e0"));
            }
            currentEquipmentItem = view;
            view.setBackgroundColor(Color.parseColor("#dbdbdb"));
            ArrayList<String> staff = equipments.get(position).getStaffName();
            equipmentAdapter = new ArrayAdapter<String>(this, R.layout.select_staff_time_moban, R.id.mrele_frag2_grid_item_btn, staff);
            equipmentListView.setAdapter(equipmentAdapter);
        } else if (viewId == R.id.class_equipment_list) {
            Intent intent = new Intent(this, EquipmentInformationActivity.class);
            intent.putExtra(GetMonitoringPointDataModel.NODE_IDS_KEY,"");
            intent.putExtra(GetMonitoringPointDataModel.KPI_CODES_key,"");
            intent.putExtra(GetMonitoringPointDataModel.NODE_IDS_KEY,"");
            startActivity(intent);
        }
    }

    @Override
    public void LoadOver(List<EquipmentMsgBean> data) {
        //在此做界面处理
        equipmentMsgData = (ArrayList<EquipmentMsgBean>) data;
        GetAcquisitionPointModel model = new GetAcquisitionPointModel(this, new GetAcquisitionPointView() {
            @Override
            public void LoadOver(List<EquipmentMsgBean> data) {
                SelectEquipmentActivity.this.acquisitionPointData = (ArrayList<EquipmentMsgBean>)data;

            }

            @Override
            public void LoadOver() {

            }

            @Override
            public void LoadError(String errorMsg) {
                SelectEquipmentActivity.this.LoadError(errorMsg);
            }
        });
    }

    @Override
    public void LoadOver() {

    }

    @Override
    public void LoadError(String errorMsg) {
//        if (dialogManager==null)
//            dialogManager = new DialogManager(this);
//        dialogManager.showRecordingDialog(errorMsg);
        ToastUtils.showToast(this,errorMsg);
    }
}
