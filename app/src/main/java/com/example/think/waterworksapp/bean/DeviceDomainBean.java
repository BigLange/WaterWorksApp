package com.example.think.waterworksapp.bean;

/**
 * Created by Think on 2016/9/6.
 */
public class DeviceDomainBean {
    private long domainID;
    private long parentID;
    private String domainPath;
    private String name;
    private String description;
    private String domains;
    private int belong;

    public long getDomainID() {
        return domainID;
    }

    public void setDomainID(long domainID) {
        this.domainID = domainID;
    }

    public long getParentID() {
        return parentID;
    }

    public void setParentID(long parentID) {
        this.parentID = parentID;
    }

    public String getDomainPath() {
        return domainPath;
    }

    public void setDomainPath(String domainPath) {
        this.domainPath = domainPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDomains() {
        return domains;
    }

    public void setDomains(String domains) {
        this.domains = domains;
    }

    public int getBelong() {
        return belong;
    }

    public void setBelong(int belong) {
        this.belong = belong;
    }
}
