package com.example.think.waterworksapp.bean;

/**
 * Created by Think on 2016/9/8.
 */
public class AcquisitionSensorBean {

    private long domainId;
    private String domainPath;
    private long id;
    private String label;
    private long gatewayId;
    private String externalDevId;
    private long modelId;
    private String manufacturer;
    private long physicalDeviceId;
    private String createTime;
    private String updateTime;
    private String category;
    private DeviceValueBean values;
    private String categoryValues;
    private int maxInstanceNo;
    private String managedStatus;
    private String operationStatus;
    private String activeTime;

    public long getDomainId() {
        return domainId;
    }

    public void setDomainId(long domainId) {
        this.domainId = domainId;
    }

    public String getDomainPath() {
        return domainPath;
    }

    public void setDomainPath(String domainPath) {
        this.domainPath = domainPath;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public long getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(long gatewayId) {
        this.gatewayId = gatewayId;
    }

    public String getExternalDevId() {
        return externalDevId;
    }

    public void setExternalDevId(String externalDevId) {
        this.externalDevId = externalDevId;
    }

    public long getModelId() {
        return modelId;
    }

    public void setModelId(long modelId) {
        this.modelId = modelId;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public long getPhysicalDeviceId() {
        return physicalDeviceId;
    }

    public void setPhysicalDeviceId(long physicalDeviceId) {
        this.physicalDeviceId = physicalDeviceId;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public DeviceValueBean getValues() {
        return values;
    }

    public void setValues(DeviceValueBean values) {
        this.values = values;
    }

    public String getCategoryValues() {
        return categoryValues;
    }

    public void setCategoryValues(String categoryValues) {
        this.categoryValues = categoryValues;
    }

    public int getMaxInstanceNo() {
        return maxInstanceNo;
    }

    public void setMaxInstanceNo(int maxInstanceNo) {
        this.maxInstanceNo = maxInstanceNo;
    }

    public String getOperationStatus() {
        return operationStatus;
    }

    public void setOperationStatus(String operationStatus) {
        this.operationStatus = operationStatus;
    }

    public String getManagedStatus() {
        return managedStatus;
    }

    public void setManagedStatus(String managedStatus) {
        this.managedStatus = managedStatus;
    }

    public String getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(String activeTime) {
        this.activeTime = activeTime;
    }
}
