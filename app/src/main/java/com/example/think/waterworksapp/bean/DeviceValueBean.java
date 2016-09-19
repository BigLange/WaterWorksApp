package com.example.think.waterworksapp.bean;

/**
 * Created by Think on 2016/9/8.
 */
public class DeviceValueBean {

    private String undefined;
    private String manufacturer;
    private double longitude;
    private double latitude;
    private String installationPosition;
    private String standardAddress;

    public String getUndefined() {
        return undefined;
    }

    public void setUndefined(String undefined) {
        this.undefined = undefined;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getInstallationPosition() {
        return installationPosition;
    }

    public void setInstallationPosition(String installationPosition) {
        this.installationPosition = installationPosition;
    }

    public String getStandardAddress() {
        return standardAddress;
    }

    public void setStandardAddress(String standardAddress) {
        this.standardAddress = standardAddress;
    }
}
