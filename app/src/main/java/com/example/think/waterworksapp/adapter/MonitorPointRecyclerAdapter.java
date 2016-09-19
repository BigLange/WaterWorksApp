package com.example.think.waterworksapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.think.waterworksapp.R;
import com.example.think.waterworksapp.base_view.ChartView;
import com.example.think.waterworksapp.bean.AcquisitionSensorBean;
import com.example.think.waterworksapp.bean.AcquisitionSensorDataBean;
import com.example.think.waterworksapp.bean.MonitoringPointDataBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Think on 2016/9/14.
 */

public class MonitorPointRecyclerAdapter extends RecyclerView.Adapter<MonitorPointRecyclerAdapter.MonitorPointDataHolder> {

    private final int LAYOUT_ID = R.layout.monitoring_point_data_show_item;

    private Context context;
    private ArrayList<AcquisitionSensorBean> acquisitionSensors;
    private HashMap<Long,ArrayList<AcquisitionSensorDataBean>> acquisitionSensorDatas;
    private HashMap<Long,HashMap<Long,ArrayList<MonitoringPointDataBean>>> monitoringPointDatas;

    public MonitorPointRecyclerAdapter(Context context, ArrayList<AcquisitionSensorBean> acquisitionSensors
            , HashMap<Long,ArrayList<AcquisitionSensorDataBean>> acquisitionSensorDatas
            , HashMap<Long,HashMap<Long,ArrayList<MonitoringPointDataBean>>> monitoringPointDatas){
        this.context = context;
        this.acquisitionSensors = acquisitionSensors;
        this.acquisitionSensorDatas = acquisitionSensorDatas;
        this.monitoringPointDatas = monitoringPointDatas;
    }

    @Override
    public MonitorPointDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(context).inflate(LAYOUT_ID,parent,false);
        return new MonitorPointDataHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(MonitorPointDataHolder holder, int position) {
        holder.notDataText.setVisibility(View.GONE);
        holder.showDataLayout.setVisibility(View.VISIBLE);
        AcquisitionSensorBean acquisitionSensorBean = acquisitionSensors.get(position);
        String titleValue = acquisitionSensorBean.getLabel();
        holder.title.setText(titleValue);
        ArrayList<AcquisitionSensorDataBean> acquisitionSensorDataArr = acquisitionSensorDatas.get(acquisitionSensorBean.getModelId());
        if (acquisitionSensorDataArr==null||acquisitionSensorDataArr.size()<=0){
            setNotDataHandler(holder);
            return;
        }
        HashMap<Long,ArrayList<MonitoringPointDataBean>> monitorPointMap = monitoringPointDatas.get(acquisitionSensorBean.getId());
        if (monitorPointMap==null||monitorPointMap.size()<=0){
            setNotDataHandler(holder);
            return;
        }
        HashMap<Long,String> colorMap = getColorMap(acquisitionSensorDataArr);
        holder.dataItem.setAdapter(new MonitorPointGridAdapter(context,acquisitionSensorDataArr,colorMap));
        holder.chartView.setData(colorMap,monitorPointMap);

    }

    @Override
    public int getItemCount() {
        return acquisitionSensors.size();
    }


    public String getRandom(){
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int randomInt = random.nextInt(99);
        if (randomInt <10){
            sb.append("0");
        }
        sb.append(randomInt);
        return sb.toString();
    }

    private HashMap<Long,String> getColorMap(ArrayList<AcquisitionSensorDataBean> acquisitionSensorDataArr){
        HashMap<Long,String> colorMap = new HashMap<>();
        for (AcquisitionSensorDataBean acquisitionSensorDataBean:acquisitionSensorDataArr) {
            StringBuilder sb = new StringBuilder();
            sb.append("#");
            sb.append(getRandom());
            sb.append(getRandom());
            sb.append(getRandom());
            colorMap.put(acquisitionSensorDataBean.getId(),sb.toString());
        }
        return colorMap;
    }

    private void setNotDataHandler(MonitorPointDataHolder holder){
        holder.notDataText.setVisibility(View.VISIBLE);
        holder.showDataLayout.setVisibility(View.GONE);
    }

    public class MonitorPointDataHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private GridView dataItem;
        private ChartView chartView;
        private TextView notDataText;
        private LinearLayout showDataLayout;
        public MonitorPointDataHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.point_data_title);
            dataItem = (GridView) itemView.findViewById(R.id.point_data_grid);
            chartView = (ChartView) itemView.findViewById(R.id.point_data_line_chart);
            notDataText = (TextView) itemView.findViewById(R.id.point_data_not_data_txt);
            showDataLayout = (LinearLayout) itemView.findViewById(R.id.point_data_show_layout);
        }

    }
}
