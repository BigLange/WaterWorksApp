package com.example.think.waterworksapp.bean;

/**
 * Created by Think on 2016/8/22.
 */
public class EquipmentMsgBean {
    private String domainPath;
    private int id;
    private String label;
    private int gatewayId;
    private String externalDevId;
    private int modelId;
    private String createTime;
    private String updateTime;
    private String managedStatus;
    private String operationStatus;
    private String activeTime;
    private RfIdBean rfIdBean;

    public String getDomainPath() {
        return domainPath;
    }

    public void setDomainPath(String domainPath) {
        this.domainPath = domainPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(int gatewayId) {
        this.gatewayId = gatewayId;
    }

    public String getExternalDevId() {
        return externalDevId;
    }

    public void setExternalDevId(String externalDevId) {
        this.externalDevId = externalDevId;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getManagedStatus() {
        return managedStatus;
    }

    public void setManagedStatus(String managedStatus) {
        this.managedStatus = managedStatus;
    }

    public String getOperationStatus() {
        return operationStatus;
    }

    public void setOperationStatus(String operationStatus) {
        this.operationStatus = operationStatus;
    }

    public String getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(String activeTime) {
        this.activeTime = activeTime;
    }

    public RfIdBean getRfIdBean() {
        return rfIdBean;
    }

    public void setRfIdBean(RfIdBean rfIdBean) {
        this.rfIdBean = rfIdBean;
    }
}
