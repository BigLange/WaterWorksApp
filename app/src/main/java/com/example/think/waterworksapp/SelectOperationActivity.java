package com.example.think.waterworksapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.think.waterworksapp.adapter.SelectOperationGridAdapter;
import com.example.think.waterworksapp.base_intface.GetTeamMsgView;
import com.example.think.waterworksapp.base_intface.LoginOutView;
import com.example.think.waterworksapp.base_view.PictureCarouselView;
import com.example.think.waterworksapp.bean.SelectOperationBtnBean;
import com.example.think.waterworksapp.custom_view.UpperActivity;
import com.example.think.waterworksapp.dialog.DialogManager;
import com.example.think.waterworksapp.dialog.LoginOutDialog;
import com.example.think.waterworksapp.dialog.SimperDialog;
import com.example.think.waterworksapp.model.GetTeamModel;
import com.example.think.waterworksapp.model.LoginOutModel;
import com.example.think.waterworksapp.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class SelectOperationActivity extends UpperActivity implements AdapterView.OnItemClickListener
        ,View.OnClickListener,GetTeamMsgView{

    private PictureCarouselView pictureCarouselView;
    private GridView operationBtnGrid;
    private ArrayList<SelectOperationBtnBean> data;
    private String[] titles = {"设备巡检","传感检测","智能报警","保安巡检","生产通知","行政通知","浦沅信息","登出"};
    private int[] icons = {R.drawable.select_operation_inspection_icon,R.drawable.select_operation_sensing_detection_icon,
            R.drawable.select_operation_warning_icon,R.drawable.select_operation_security_staff_inspection,
            R.drawable.select_operation_notification_icon,R.drawable.select_operation_administrative_notice_icon,
            R.drawable.select_operation_puyuan_msg_icon,R.drawable.select_operation_login_out_icon,};
    private ArrayList<Integer> imgResourcesId;
    private boolean isStopActivity = false;
    private SimperDialog dialog;
//    private DialogManager dialogManager;



    @Override
    protected void initView() {
        pictureCarouselView = findView(R.id.picture_carouse_view);
        operationBtnGrid = findView(R.id.operation_btn_gridView);
        dialog = new SimperDialog(this);
        dialog.setContent("正在获取班组信息").showDialog();
        getTeamMsg();
    }

    private void setValue(){
        data = new ArrayList<>();
        for (int i=0;i<titles.length;i++){
            SelectOperationBtnBean bean = new SelectOperationBtnBean();
            bean.setTitle(titles[i]);
            bean.setImgResourcesId(icons[i]);
            data.add(bean);
        }
        imgResourcesId = new ArrayList<>();
        imgResourcesId.add(R.drawable.notice_1);
        imgResourcesId.add(R.drawable.notice_2);
        imgResourcesId.add(R.drawable.notice_3);
        imgResourcesId.add(R.drawable.notice_4);
        pictureCarouselView.setImgResourcesId(imgResourcesId);
    }

    @Override
    protected void initEvent() {
        operationBtnGrid.setOnItemClickListener(this);
        pictureCarouselView.setListener(this);
    }

    private void getTeamMsg(){
        GetTeamModel getTeamModel = new GetTeamModel(this,this);
        getTeamModel.getTeamMsg();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_operation;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i){
            case 0:
                startActivity(SelectEquipmentActivity.class);
                break;
            case 7:
                activeLoginOut();
                break;
            default:
                DialogManager dm = new DialogManager(this);
                dm.showRecordingDialog("该功能暂未开放，敬请期待！");
                break;
        }

    }

    private void activeLoginOut(){
        LoginOutModel loginOutModel = new LoginOutModel(this, new LoginOutView() {
            @Override
            public void LoadOver(List<String> data) {
            }

            @Override
            public void LoadOver() {
            }

            @Override
            public void LoadError(String errorMsg) {
            }
        });
        loginOutModel.loginOut();
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        if (isStopActivity)
            pictureCarouselView.startTimer();
        super.onStart();
    }

    @Override
    protected void onStop() {
        pictureCarouselView.stopTimer();
        isStopActivity = true;
        super.onStop();
    }

    private void startActivity(Class<? extends Activity> activityClass) {
        Intent intent = new Intent(this,activityClass);
        startActivity(intent);
    }



    @Override
    public void onClick(View view) {
        int position = pictureCarouselView.getPosition();
        Log.e("position",position+"");
    }


    @Override
    public void LoadOver(List data) {

    }

    @Override
    public void LoadOver() {
        setValue();
        SelectOperationGridAdapter adapter = new SelectOperationGridAdapter(this,-1,data);
        operationBtnGrid.setAdapter(adapter);
        dialog.hideDialog();
    }

    @Override
    public void LoadError(String errorMsg) {
//        if (dialogManager==null)
//            dialogManager = new DialogManager(this);
//        dialogManager.showRecordingDialog(errorMsg);
        ToastUtils.showToast(this,errorMsg);
        dialog.hideDialog();
        finish();
    }

    @Override
    public void loginOut() {
        dialog.hideDialog();
        LoginOutDialog dialog = LoginOutDialog.getLoginOutDialog(this);
        dialog.showDialog();
    }
}
