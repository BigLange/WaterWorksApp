package com.example.think.waterworksapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.think.waterworksapp.adapter.DividerItemDecoration;
import com.example.think.waterworksapp.adapter.EquipmentSubmitAdapter;
import com.example.think.waterworksapp.adapter.ImgUpLoadAdapter;
import com.example.think.waterworksapp.adapter.MonitorPointRecyclerAdapter;
import com.example.think.waterworksapp.base_intface.EquipmentMsgSubmitView;
import com.example.think.waterworksapp.base_intface.GetAcquisitionSensorDataView;
import com.example.think.waterworksapp.base_intface.GetAcquisitionSensorView;
import com.example.think.waterworksapp.base_intface.GetInspectionItemView;
import com.example.think.waterworksapp.base_intface.GetMonitoringPointDataView;
import com.example.think.waterworksapp.base_view.FullyLinearLayoutManager;
import com.example.think.waterworksapp.bean.AcquisitionSensorBean;
import com.example.think.waterworksapp.bean.AcquisitionSensorDataBean;
import com.example.think.waterworksapp.bean.InspectionItemBean;
import com.example.think.waterworksapp.bean.MonitoringPointDataBean;
import com.example.think.waterworksapp.custom_view.UpperActivity;
import com.example.think.waterworksapp.dialog.DialogManager;
import com.example.think.waterworksapp.dialog.LoginOutDialog;
import com.example.think.waterworksapp.dialog.SimperDialog;
import com.example.think.waterworksapp.model.EquipmentMsgSubmitModel;
import com.example.think.waterworksapp.model.GetAcquisitionSensorDataModel;
import com.example.think.waterworksapp.model.GetAcquisitionSensorModel;
import com.example.think.waterworksapp.model.GetInspectionItemModel;
import com.example.think.waterworksapp.model.GetMonitoringPointDataModel;
import com.example.think.waterworksapp.popup_window.AddImagePopupWindow;
import com.example.think.waterworksapp.utils.ActivityPreservationUtils;
import com.example.think.waterworksapp.utils.NfcUtils;
import com.example.think.waterworksapp.utils.SetListViewAndGridViewHeightUtils;
import com.example.think.waterworksapp.utils.ToastUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class EquipmentInformationActivity extends UpperActivity
        implements ImgUpLoadAdapter.AddImageViewBtnClickListener,AdapterView.OnItemClickListener
                    ,AddImagePopupWindow.AddImageItemClickListener ,GetInspectionItemView,GetMonitoringPointDataView
                        ,View.OnClickListener,EquipmentMsgSubmitView,NfcUtils.GetDeviceIdListener,DialogInterface.OnDismissListener
                        ,GetAcquisitionSensorView,GetAcquisitionSensorDataView{

    public static final String CURRENT_IMAGE_LIST_LENGTH = "current image list size";

    //进入相机请求码
    private final int CAPTURECODE = 111;
    //进入相册请求码
    private final int ALBUMCODE = 222;
    //进入查看大图的请求码
    private final int IMGPRE = 333;

    private RecyclerView equipment_run_state_submit_recycler;
    private RecyclerView equipment_data_show_recycler;
//    private TextView equipmentRunStateTxt;
    private LineChartView line_chart_view;
    private EditText errorMsgSubmitEdit;
    private GridView errorImgSubmitGrid;
    private Button submitBtn;
    private View popupWindowBottom;
//    private DialogManager dialogManager;
    private SimperDialog dialog;

    private ArrayList<AcquisitionSensorBean> acquisitionSensorArr = new ArrayList<>();
    private HashMap<Long,ArrayList<AcquisitionSensorDataBean>> acquisitionSensorDataMap = new HashMap<>();
    private HashMap<Long,HashMap<Long,ArrayList<MonitoringPointDataBean>>> monitorPointDataMap = new HashMap<>();
    private ArrayList<InspectionItemBean> inspectionItems;
    private EquipmentSubmitAdapter equipmentSubmitAdapter;
    private ArrayAdapter<String> imgUpLoadAdapter;
    private AddImagePopupWindow addImagePopupWindow;
    private ArrayList<String> imgUrls = new ArrayList<>();
    private HashMap<String,Object> bodyMap;
    private NfcUtils nfcUtils;



    private boolean ifFinsh = false;
    private boolean ifGetDevice = false;
    private boolean ifContactNfc = false;
    private boolean ifReadDeviceIdSuccess = false;
    private long deviceId;
    private long modelId;
    private int acquisitionSensorSize = -1;
    private int currentMonitorindex = 0;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nfcUtils = new NfcUtils(this,this);
        if (dialog == null)
            dialog = new SimperDialog(this);
        dialog.setContent("正在获取设备ID").showDialog();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            if (ifContactNfc)
                return;
            ifContactNfc = true;
            nfcUtils.processNfcConnectedIntent(intent);
            return;
        }
    }

    @Override
    protected void onDestroy() {
        ifFinsh = true;
        if (dialog!=null)
            dialog.hideDialog();
        super.onDestroy();
    }

    private void initValue(){
        bodyMap = new HashMap<>();
        Intent intent = getIntent();
        deviceId = intent.getLongExtra(SelectEquipmentActivity.DEVICE_ID,-1);
        modelId = intent.getLongExtra(SelectEquipmentActivity.MODEL_ID,-1);
    }

    private void getData(){
        GetAcquisitionSensorModel getAcquisitionSensorModel = new GetAcquisitionSensorModel(this,this);
        getAcquisitionSensorModel.getAcquisitionSensor(deviceId);

        GetInspectionItemModel getInspectionItemModel = new GetInspectionItemModel(this,this);
        getInspectionItemModel.getInspectionItem(modelId);

//        getMonitoringPointDataModel.getMonitoringPointData(bodyMap);
    }



    @Override
    protected void initView() {
//        initRecycler();
//        initChart();
        initValue();
        initImgUpLoad();
        initPopupWindow();
        errorMsgSubmitEdit = findView(R.id.equipment_error_msg_submit_edit);
//        equipmentRunStateTxt = findView(R.id.equipment_run_state_value);
        submitBtn = findView(R.id.equipment_information_submit_btn);
        popupWindowBottom = findView(R.id.equipment_information_popupWindow_bottom);
    }

    private void initPopupWindow() {
        addImagePopupWindow = new AddImagePopupWindow(this,this);
        addImagePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //当popupwindow消失的时候，设置屏幕蒙版消失
                lightOn();
            }
        });
    }


    //下面的菜单去掉的时候，界面变亮
    private void lightOn() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1.0f;
        getWindow().setAttributes(lp);
    }

    //下面的菜单弹出来的时候，界面变暗
    private void lightOff() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.3f;
        getWindow().setAttributes(lp);
    }

    private void showPopupWindow(){
        addImagePopupWindow.setAnimationStyle(R.style.dir_popupwindow_anim);
        addImagePopupWindow.showAsDropDown(popupWindowBottom,0,0);
        lightOff();
    }

    /**
     * 初始化图片加载
     */
    private void initImgUpLoad() {
        errorImgSubmitGrid = findView(R.id.equipment_error_img_submit_grid);
        imgUpLoadAdapter = new ImgUpLoadAdapter(this,imgUrls,this);
        errorImgSubmitGrid.setAdapter(imgUpLoadAdapter);
        SetListViewAndGridViewHeightUtils.setGridViewHeight(errorImgSubmitGrid);
    }

    private void initRecycler(){
        equipment_run_state_submit_recycler = findView(R.id.equipment_run_state_submit);
        //设置布局管理器
        equipment_run_state_submit_recycler.setLayoutManager(new LinearLayoutManager(this));
        //添加分割线
        equipment_run_state_submit_recycler.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));


        equipment_data_show_recycler = findView(R.id.equipment_data_show_recycler);
        equipment_data_show_recycler.setLayoutManager(new FullyLinearLayoutManager(this));
//        equipment_data_show_recycler.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
    }



    @Override
    protected void initEvent() {
        errorImgSubmitGrid.setOnItemClickListener(this);
        submitBtn.setOnClickListener(this);
    }






    @Override
    protected int getLayoutId() {
        return R.layout.activity_equipment_information;
    }

    @Override
    public void onAddImgClick() {
        showPopupWindow();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, ImagePreview.class);
        Bundle bundle = new Bundle();
            bundle.putSerializable("dirPaths", imgUrls);
        bundle.putInt("index", i);
        intent.putExtras(bundle);
        startActivityForResult(intent, IMGPRE);
        overridePendingTransition(R.anim.img_pre_start, R.anim.img_pre_close);
    }

    /**
     * 点击拍照的回调
     */
    @Override
    public void toPhotograph() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAPTURECODE);
    }

    /**
     * 点击相册选择的回调
     */
    @Override
    public void toAlbum() {
        Intent intent = new Intent(this, ImageSelectActivity.class);
        intent.putExtra(CURRENT_IMAGE_LIST_LENGTH,imgUrls.size());
        startActivityForResult(intent, ALBUMCODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == CAPTURECODE) {
                Bitmap bm = (Bitmap) data.getExtras().get("data");
                setPathToSet(bm);
            } else if (requestCode == ALBUMCODE) {
                Bundle bundle = data.getExtras();
                HashSet<String> albumPath = (HashSet<String>) bundle.getSerializable("dirPaths");
                imgUrls.addAll(albumPath);
            } else if (requestCode == IMGPRE) {
                int index = data.getIntExtra("index", -1);
                imgUrls.remove(imgUrls.remove(index));
//                addimgFragment.setmDatas(mDirPaths);
            }
            imgUpLoadAdapter.notifyDataSetChanged();
            SetListViewAndGridViewHeightUtils.setGridViewHeight(errorImgSubmitGrid);
        }
    }

    public void setPathToSet(Bitmap bm) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(EquipmentInformationActivity.this, "SD卡不能使用", Toast.LENGTH_SHORT).show();
            return;
        }
        File root = Environment.getExternalStorageDirectory();
        File file = new File(root, "WaterWorksErrorImg");
        if (!file.exists()) {
            file.mkdir();
        }
        FileOutputStream fos = null;
        String imgName = System.currentTimeMillis() + ".jpg";
        File imgFile = new File(file, imgName);
        try {
            fos = new FileOutputStream(imgFile);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        imgUrls.add(imgFile.getAbsolutePath());
    }


    @Override
    public void getInspectionItemSuccess(ArrayList<InspectionItemBean> data) {
        inspectionItems = data;
        if (data==null) {
            getInspectionItemFail("数据错误");
            return;
        }
        Log.e("equipmentSubmitAdapter",data.size()+"");
        equipmentSubmitAdapter = new EquipmentSubmitAdapter(this,inspectionItems);
        Log.e("equipmentSubmitAdapter",equipmentSubmitAdapter+"");
        equipment_run_state_submit_recycler.setAdapter(equipmentSubmitAdapter);
    }

    @Override
    public void getInspectionItemFail(String errorMsg) {
//        if (dialogManager==null)
//            dialogManager = new DialogManager(this);
//        dialogManager.showRecordingDialog(errorMsg);
//        ToastUtils.showToast(this,errorMsg);
        Log.e("getInspectionItem",errorMsg);
        ToastUtils.showToast(this,errorMsg);
        failHandle();
    }

    @Override
    public AcquisitionSensorBean getNodeIds(Long modelId) {
        for (AcquisitionSensorBean acquisitionSensorBean:acquisitionSensorArr){
            if (acquisitionSensorBean.getModelId()==modelId)
                return acquisitionSensorBean;
        }
        return null;
    }

    @Override
    public ArrayList<AcquisitionSensorDataBean> getKpiCodes(Long modelId) {
        return acquisitionSensorDataMap.get(modelId);
    }

    @Override
    public void getMonitoringPointDataSuccess(ArrayList<MonitoringPointDataBean> data) {
        if (data==null){
            getMonitoringPointDataFail("数据错误");
            return;
        }
        //做数据处理
        currentMonitorindex++;
        long nodeid = -1;
        HashMap<Long,ArrayList<MonitoringPointDataBean>> dataMap = new HashMap<>();
        for (MonitoringPointDataBean monitoringPointDataBean:data){
            nodeid = monitoringPointDataBean.getNodeId();
            long kpiCode = monitoringPointDataBean.getKpiCode();
            ArrayList<MonitoringPointDataBean> dataArrByCpiCode = dataMap.get(kpiCode);
            if (dataArrByCpiCode!=null) {
                dataArrByCpiCode.add(monitoringPointDataBean);
            }else {
             dataArrByCpiCode = new ArrayList<>();
                dataArrByCpiCode.add(monitoringPointDataBean);
                dataMap.put(kpiCode,dataArrByCpiCode);
            }

        }
        if (nodeid!=-1)
            monitorPointDataMap.put(nodeid,dataMap);
        if (currentMonitorindex==acquisitionSensorSize){
            MonitorPointRecyclerAdapter adapter
                    = new MonitorPointRecyclerAdapter(this,acquisitionSensorArr,acquisitionSensorDataMap,monitorPointDataMap);
            equipment_data_show_recycler.setAdapter(adapter);

        }
        ifGetDevice = true;
        dialog.hideDialog();
    }

    @Override
    public void getMonitoringPointDataFail(String errorMsg) {
//        if (dialogManager==null)
//            dialogManager = new DialogManager(this);
//        dialogManager.showRecordingDialog(errorMsg);
        if (ifFinsh)
            return;
        ToastUtils.showToast(this,errorMsg);
        failHandle();
    }

    @Override
    public void onClick(View view) {
//        if (!ifGetDevice)
//            return;
        EquipmentMsgSubmitModel equipmentMsgSubmitModel = new EquipmentMsgSubmitModel(this,this);
        equipmentMsgSubmitModel.submitMsg();
        if (dialog==null)
            dialog = new SimperDialog(this);
        dialog.setContent("设备信息提交中...").showDialog();
    }

    @Override
    public void getDeviceFail() {
        ToastUtils.showToast(this,"读卡失败");
        finish();
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        if (!ifReadDeviceIdSuccess)
            getDeviceFail();
    }



    @Override
    public Map<String, Boolean> getEquipmentInformation() {
        return equipmentSubmitAdapter.getSubmitValue();
    }

    @Override
    public String getEquipmentDetailedInformation() {
        return errorMsgSubmitEdit.getText().toString();
    }

    @Override
    public ArrayList<String> getEquipmentImageInformation() {
        return imgUrls;
    }


    @Override
    public long getDeviceID() {
        return deviceId;
    }



    @Override
    public void submitSuccess() {
        if (ifFinsh)
            return;
//        dialog.hideDialog();
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        ActivityPreservationUtils.finshAllActivity();
        ToastUtils.showToast(this,"信息提交成功");
        finish();
    }

    @Override
    public void submitFail(String errorMsg) {
        if (ifFinsh)
            return;
        dialog.hideDialog();
//        if (dialogManager==null)
//            dialogManager = new DialogManager(this);
//        dialogManager.showRecordingDialog(errorMsg);
        ToastUtils.showToast(this,errorMsg);
    }

    @Override
    public void getDeviceSuccess(String deviceID) {
        if (ifFinsh)
            return;
        dialog.hideDialog();
        if (!deviceID.equals(deviceID)) {
            ToastUtils.showToast(this, "请选择相同设备！");
            finish();
        }
        dialog.setContent("正在获取服务器数据....").showDialog();
        initRecycler();
        getData();
//        initChart();
        ifReadDeviceIdSuccess = true;
        ToastUtils.showToast(this, deviceID);
    }


    @Override
    public void getAcquisitionSensorSuccess(ArrayList<AcquisitionSensorBean> data) {
        if (ifFinsh)
            return;
        if (data==null){
            getAcquisitionSensorFail("数据错误");
            return;
        }
        acquisitionSensorSize = data.size();
        for (AcquisitionSensorBean dataBean:data){
            GetAcquisitionSensorDataModel getAcquisitionSensorDataModel = new GetAcquisitionSensorDataModel(this,this);
            getAcquisitionSensorDataModel.getAcquisitionData(dataBean.getModelId());
        }
        acquisitionSensorArr = data;
    }

    @Override
    public void getAcquisitionSensorFail(String errorMsg) {
        if (ifFinsh)
            return;
        Log.e("getAcquisitionSensorFail",errorMsg);
        ToastUtils.showToast(this,errorMsg);
        failHandle();
    }

    @Override
    public void getAcquisitionSensorDataSuccess(ArrayList<AcquisitionSensorDataBean> data) {
        if (ifFinsh)
            return;
        if (data==null){
            getAcquisitionSensorDataFail("数据错误");
            return;
        }
        Log.e("getAcquisitionSensorDataSuccess",data+"");
        if (data.size()>0){
            GetMonitoringPointDataModel getMonitoringPointDataModel = new GetMonitoringPointDataModel(this,this);
            AcquisitionSensorDataBean dataBean = data.get(0);
            acquisitionSensorDataMap.put(dataBean.getModelId(),data);
            getMonitoringPointDataModel.getMonitoringPointData(dataBean.getModelId());
        }
    }

    @Override
    public void getAcquisitionSensorDataFail(String errorMsg) {
        if (ifFinsh)
            return;
        Log.e("getAcquisitionSensorDataFail",errorMsg);
        ToastUtils.showToast(this,errorMsg);
        failHandle();
    }

    @Override
    public void loginOut() {
        if (ifFinsh)
            return;
        if (dialog!=null)
            dialog.hideDialog();
        LoginOutDialog dialog = LoginOutDialog.getLoginOutDialog(this);
        dialog.showDialog();
    }


    private void failHandle(){
        dialog.hideDialog();
        finish();
    }
}
