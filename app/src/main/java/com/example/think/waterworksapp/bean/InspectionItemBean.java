package com.example.think.waterworksapp.bean;

/**
 * Created by Think on 2016/8/22.
 */
public class InspectionItemBean {

    private int id;
    private String position;
    private String item;//巡检项目
    private String context;//巡检内容
    private String spotCheckMethod;//点检方法
    private String spotCheckTool;//点检工具
    private String spotCheckCategory;//点检类别
    private String standardContext;//标准内容
    private int modelId;//设备模板ID

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
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

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }
}
