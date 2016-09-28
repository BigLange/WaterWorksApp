package com.example.think.waterworksapp.bean;


/**
 * Created by Think on 2016/8/23.
 */
public class MonitoringPointDataBean {

    private long nodeId;
    private long kpiCode;
    private int quality;
    private float value;
    private String instance;
    private String arisingTime;
    private String insertTime;
    private String agentId;
    private String notes;
    private String valueStr;
    private String stringValue;
    private float numberValue;

    public long getNodeId() {
        return nodeId;
    }

    public void setNodeId(long nodeId) {
        this.nodeId = nodeId;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public long getKpiCode() {
        return kpiCode;
    }

    public void setKpiCode(long kpiCode) {
        this.kpiCode = kpiCode;
    }


    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public String getArisingTime() {
        return arisingTime;
    }

    public void setArisingTime(String arisingTime) {
        this.arisingTime = arisingTime;
    }

    public String getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(String insertTime) {
        this.insertTime = insertTime;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getValueStr() {
        return valueStr;
    }

    public void setValueStr(String valueStr) {
        this.valueStr = valueStr;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public float getNumberValue() {
        return numberValue;
    }

    public void setNumberValue(float numberValue) {
        this.numberValue = numberValue;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
