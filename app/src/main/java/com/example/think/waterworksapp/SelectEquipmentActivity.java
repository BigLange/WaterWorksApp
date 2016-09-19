package com.example.think.waterworksapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.example.think.waterworksapp.adapter.DeviceAdapter;
import com.example.think.waterworksapp.adapter.DeviceDomainAdapter;
import com.example.think.waterworksapp.base_intface.GetDeviceDomainView;
import com.example.think.waterworksapp.base_intface.GetEquipmentView;
import com.example.think.waterworksapp.bean.DeviceDomainBean;
import com.example.think.waterworksapp.bean.EquipmentBeanTest;
import com.example.think.waterworksapp.bean.EquipmentMsgBean;
import com.example.think.waterworksapp.custom_view.UpperActivity;
import com.example.think.waterworksapp.dialog.LoginOutDialog;
import com.example.think.waterworksapp.dialog.SimperDialog;
import com.example.think.waterworksapp.model.GetDeviceDomainModel;
import com.example.think.waterworksapp.model.GetEquipmentModel;
import com.example.think.waterworksapp.model.GetMonitoringPointDataModel;
import com.example.think.waterworksapp.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SelectEquipmentActivity extends UpperActivity implements AdapterView.OnItemClickListener
        ,GetEquipmentView,GetDeviceDomainView{

    public static final String DEVICE_ID = "nodeIds";
    public static final String MODEL_ID = "modelId";

    private ListView equipmentAreaListView;
    private ListView equipmentListView;
    private View currentEquipmentItem;
    private SimperDialog dialog;
//    private DialogManager dialogManager;



    private ArrayList<EquipmentMsgBean> equipmentMsgData;
    private ArrayList<DeviceDomainBean> deviceDomainData;
    private HashMap<String,ArrayList<EquipmentMsgBean>> deviceMap;

    private DeviceAdapter equipmentAdapter;
    private DeviceDomainAdapter equipmentAreaAdapter;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initValue();
    }

    private void initAdapter() {
        equipmentAreaAdapter = new DeviceDomainAdapter(this,deviceDomainData);
        equipmentAreaListView.setAdapter(equipmentAreaAdapter);
        dialog.hideDialog();
    }

    private void initValue() {
        dialog = new SimperDialog(this);
        dialog.setContent("正在获取设备").showDialog();
        GetEquipmentModel model = new GetEquipmentModel(this,this);
        model.getEquipmentMsg();
        GetDeviceDomainModel model2 = new GetDeviceDomainModel(this, this);
        model2.getDeviceDomain();
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
            String domainPath = deviceDomainData.get(position).getDomainID()+"";
            equipmentMsgData = deviceMap.get(domainPath);
            equipmentAdapter = new DeviceAdapter(this,equipmentMsgData);
            equipmentListView.setAdapter(equipmentAdapter);
        } else if (viewId == R.id.class_equipment_list) {
            EquipmentMsgBean equipmentMsgBean = equipmentMsgData.get(position);
            Intent intent = new Intent(this, EquipmentInformationActivity.class);
//            intent.putExtra(GetMonitoringPointDataModel.NODE_IDS_KEY,"");
//            intent.putExtra(GetMonitoringPointDataModel.KPI_CODES_key,"");
//            intent.putExtra(GetMonitoringPointDataModel.NODE_IDS_KEY,"");
            intent.putExtra(DEVICE_ID,equipmentMsgBean.getId());
            intent.putExtra(MODEL_ID,equipmentMsgBean.getModelId());
            startActivity(intent);
        }
    }

    @Override
    public void LoadOver(List<EquipmentMsgBean> data) {
        //在此做界面处理
        equipmentMsgData = (ArrayList<EquipmentMsgBean>) data;
        Log.e("equipment",data.size()+"......");
        analysisDevice();
    }

    @Override
    public void LoadOver() {

    }

    @Override
    public void LoadError(String errorMsg) {
//        if (dialogManager==null)
//            dialogManager = new DialogManager(this);
//        dialogManager.showRecordingDialog(errorMsg);
        dialog.hideDialog();
        ToastUtils.showToast(this,errorMsg);
    }

    @Override
    public void getDeviceDomainSuccess(ArrayList<DeviceDomainBean> data) {
        deviceDomainData = data;
        Log.e("equipment",data.size()+"");
        analysisDevice();
    }

    @Override
    public void getDeviceDomainFail(String errorMsg) {
        dialog.hideDialog();
        ToastUtils.showToast(this,errorMsg);
    }


    private void analysisDevice(){
        if (equipmentMsgData==null || deviceDomainData==null)
            return;
        deviceMap = new HashMap<>();
        for (int i=0;i<equipmentMsgData.size();i++){
            EquipmentMsgBean equipmentMsgBean = equipmentMsgData.get(i);
            String deviceDomainPath = equipmentMsgBean.getDomainPath();
            String[] deviceDomainPaths = deviceDomainPath.split("/");
            String parentPath = deviceDomainPaths[deviceDomainPaths.length-2];
            ArrayList<EquipmentMsgBean> devices = deviceMap.get(parentPath);
            if (devices ==null) {
                devices = new ArrayList();
                devices.add(equipmentMsgBean);
                deviceMap.put(parentPath, devices);
            }else {
                devices.add(equipmentMsgBean);
                Log.e("suibianxiediansm",devices.size()+"");
            }
        }
        Log.e("equipment",deviceMap.size()+"");
        ArrayList<DeviceDomainBean> deviceDomainBeans = new ArrayList<>();
        for (DeviceDomainBean deviceDomainBean:deviceDomainData){
            String deviceDomainPath = deviceDomainBean.getDomainID()+"";
            if (deviceMap.get(deviceDomainPath)!=null){
                deviceDomainBeans.add(deviceDomainBean);
            }
        }
        deviceDomainData = deviceDomainBeans;
        Log.e("equipment",deviceDomainData.size()+":"+deviceMap.size());
        initAdapter();
    }

    @Override
    public void loginOut() {
        dialog.hideDialog();
        LoginOutDialog dialog = LoginOutDialog.getLoginOutDialog(this);
        dialog.showDialog();
    }
}
