package com.example.think.waterworksapp.bean;

/**
 * Created by Think on 2016/8/23.
 */
public class MonitoringPointDataBean {

    private int nodeId;
    private int kpiCode;
    private int quality;
    private int value;
    private String instance;
    private String arisingTime;
    private String insertTime;
    private String agentId;
    private String notes;
    private String valueStr;
    private String stringValue;
    private String numberValue;

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public int getKpiCode() {
        return kpiCode;
    }

    public void setKpiCode(int kpiCode) {
        this.kpiCode = kpiCode;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
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

    public String getNumberValue() {
        return numberValue;
    }

    public void setNumberValue(String numberValue) {
        this.numberValue = numberValue;
    }
}
