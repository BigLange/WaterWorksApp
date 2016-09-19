package com.example.think.waterworksapp.bean;

import java.util.ArrayList;

/**
 * Created by Think on 2016/8/22.
 */
public class RequestMonitoringPointBean {

    private ArrayList<Long> nodeIds = new ArrayList<>();
    private ArrayList<Long> kpiCodes = new ArrayList<>();
    private boolean isRealTimeData = true;
    private long timePeriod = 604800000;

    public ArrayList<Long> getNodeIds() {
        return nodeIds;
    }

    public void setNodeIds(long nodeIds) {
        this.nodeIds.add(nodeIds);
    }

    public ArrayList<Long> getKpiCodes() {
        return kpiCodes;
    }

    public void setKpiCodes(long kpiCodes) {
        this.kpiCodes.add(kpiCodes);
    }

    public boolean isRealTimeData() {
        return isRealTimeData;
    }

    public long getTimePeriod() {
        return timePeriod;
    }

}
