package com.example.think.waterworksapp.bean;

/**
 * Created by Think on 2016/9/8.
 */
public class AcquisitionSensorDataBean {

    private long domainId;
    private String domainPath;
    private long id;
    private String label;
    private String name;
    private String description;
    private String icon;
    private boolean canEdit;
    private boolean noSave;
    private long uid;
    private long modelId;
    private int granularity;
    private String granularityUnit;
    private String expression;
    private String unit;
    private String triggerTime;
    private int saveInterval;
    private String keepPeriod;
    private String range;
    private String normalRange;
    private int baseKpiId;
    private String calKpiPeriod;
    private int timeDeviation;
    private String displayParam;
    private boolean aggregate;
    private boolean number;
    private boolean kpi;

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    public boolean isNoSave() {
        return noSave;
    }

    public void setNoSave(boolean noSave) {
        this.noSave = noSave;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getModelId() {
        return modelId;
    }

    public void setModelId(long modelId) {
        this.modelId = modelId;
    }

    public int getGranularity() {
        return granularity;
    }

    public void setGranularity(int granularity) {
        this.granularity = granularity;
    }

    public String getGranularityUnit() {
        return granularityUnit;
    }

    public void setGranularityUnit(String granularityUnit) {
        this.granularityUnit = granularityUnit;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getSaveInterval() {
        return saveInterval;
    }

    public void setSaveInterval(int saveInterval) {
        this.saveInterval = saveInterval;
    }

    public String getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(String triggerTime) {
        this.triggerTime = triggerTime;
    }

    public String getKeepPeriod() {
        return keepPeriod;
    }

    public void setKeepPeriod(String keepPeriod) {
        this.keepPeriod = keepPeriod;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getNormalRange() {
        return normalRange;
    }

    public void setNormalRange(String normalRange) {
        this.normalRange = normalRange;
    }

    public int getBaseKpiId() {
        return baseKpiId;
    }

    public void setBaseKpiId(int baseKpiId) {
        this.baseKpiId = baseKpiId;
    }

    public String getCalKpiPeriod() {
        return calKpiPeriod;
    }

    public void setCalKpiPeriod(String calKpiPeriod) {
        this.calKpiPeriod = calKpiPeriod;
    }

    public int getTimeDeviation() {
        return timeDeviation;
    }

    public void setTimeDeviation(int timeDeviation) {
        this.timeDeviation = timeDeviation;
    }

    public String getDisplayParam() {
        return displayParam;
    }

    public void setDisplayParam(String displayParam) {
        this.displayParam = displayParam;
    }

    public boolean isAggregate() {
        return aggregate;
    }

    public void setAggregate(boolean aggregate) {
        this.aggregate = aggregate;
    }

    public boolean isNumber() {
        return number;
    }

    public void setNumber(boolean number) {
        this.number = number;
    }

    public boolean isKpi() {
        return kpi;
    }

    public void setKpi(boolean kpi) {
        this.kpi = kpi;
    }
}
