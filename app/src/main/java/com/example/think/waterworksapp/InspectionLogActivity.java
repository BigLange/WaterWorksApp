package com.example.think.waterworksapp;


import com.example.think.waterworksapp.adapter.InspectionLogAdapter;
import com.example.think.waterworksapp.base_intface.InspectionLogView;
import com.example.think.waterworksapp.bean.InspectionLogBean;
import com.example.think.waterworksapp.custom_view.UpperActivity;
import com.example.think.waterworksapp.dialog.LoginOutDialog;
import com.example.think.waterworksapp.model.InspectionLogModel;
import com.example.think.waterworksapp.utils.ToastUtils;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.PullableListView;

import java.util.ArrayList;
import java.util.List;

public class InspectionLogActivity extends UpperActivity implements PullToRefreshLayout.OnPullListener,InspectionLogView{

    private PullToRefreshLayout pullToRefreshLayout;
    private PullableListView pullableListView;
    private InspectionLogAdapter inspectionLogAdapter;

    private boolean ifLoadOver = false;
    private boolean ifRefresh = false;
    private ArrayList<InspectionLogBean> inspectionLogBeanArr;
    private InspectionLogModel inspectionLogModel;


    @Override
    protected void initView() {
        pullToRefreshLayout = findView(R.id.inspection_pull_to_refresh_layout);
        pullableListView = findView(R.id.inspection_pull_to_refresh_list);
    }

    @Override
    protected void initEvent() {
        pullToRefreshLayout.setOnPullListener(this);
        pullToRefreshLayout.autoRefresh();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_inspection_log;
    }

    private void handlerRefresh(ArrayList<InspectionLogBean> date){
        ifRefresh = false;
        inspectionLogBeanArr = date;
        inspectionLogAdapter = new InspectionLogAdapter(this,date);
        pullableListView.setAdapter(inspectionLogAdapter);
        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        ifRefresh = true;
        if (inspectionLogModel==null)
            inspectionLogModel = new InspectionLogModel(this,this);
        inspectionLogModel.getInspectionLog(0);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        if (ifLoadOver){
            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
            ToastUtils.showToast(this,"没有更多内容啦！");
            return;
        }
        inspectionLogModel.getInspectionLog();
    }

    @Override
    public void loginOut() {
        LoginOutDialog dialog = LoginOutDialog.getLoginOutDialog(this);
        dialog.showDialog();
    }

    @Override
    public void LoadOver(List<InspectionLogBean> data) {
        if (inspectionLogAdapter==null&&inspectionLogBeanArr==null){
            handlerRefresh((ArrayList<InspectionLogBean>) data);
            return;
        }
        if (ifRefresh){
            handlerRefresh((ArrayList<InspectionLogBean>) data);
            return;
        }
        if (data.size()<inspectionLogModel.ROWS) {
            ifLoadOver = true;
            ToastUtils.showToast(this,"没有更多内容啦！");
        }
        inspectionLogBeanArr.addAll(data);
        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        inspectionLogAdapter.notifyDataSetChanged();
    }

    @Override
    public void LoadOver() {

    }

    @Override
    public void LoadError(String errorMsg) {
        if (inspectionLogAdapter==null&&inspectionLogBeanArr==null) {
            ifRefresh = false;
            pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
        }else if (ifRefresh) {
            ifRefresh = false;
            pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
        }else {
            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
        }
        ToastUtils.showToast(this,errorMsg);
    }
}
