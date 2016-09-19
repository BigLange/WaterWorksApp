package com.example.think.waterworksapp.bean;

/**
 * Created by Think on 2016/8/22.
 */
public class InspectionItemBean {

    private long id;
    private String domainPath;
    private String position;
    private String item;//巡检项目
    private String spotCheckMethod;//点检方法
    private String spotCheckTool;//点检工具
    private String spotCheckCategory;//点检类别
    private String standardContext;//标准内容
    private long modelId;//设备模板ID



    public String getDomainPath() {
        return domainPath;
    }

    public void setDomainPath(String domainPath) {
        this.domainPath = domainPath;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getSpotCheckMethod() {
        return spotCheckMethod;
    }

    public void setSpotCheckMethod(String spotCheckMethod) {
        this.spotCheckMethod = spotCheckMethod;
    }

    public String getSpotCheckTool() {
        return spotCheckTool;
    }

    public void setSpotCheckTool(String spotCheckTool) {
        this.spotCheckTool = spotCheckTool;
    }

    public String getSpotCheckCategory() {
        return spotCheckCategory;
    }

    public void setSpotCheckCategory(String spotCheckCategory) {
        this.spotCheckCategory = spotCheckCategory;
    }

    public String getStandardContext() {
        return standardContext;
    }

    public void setStandardContext(String standardContext) {
        this.standardContext = standardContext;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getModelId() {
        return modelId;
    }

    public void setModelId(long modelId) {
        this.modelId = modelId;
    }
}
