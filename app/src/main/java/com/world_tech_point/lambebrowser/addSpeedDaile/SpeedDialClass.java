package com.world_tech_point.lambebrowser.addSpeedDaile;

public class SpeedDialClass {


    private String name;
    private String imageURL;
    private String siteURL;

    public SpeedDialClass() {
    }

    public SpeedDialClass(String name, String imageURL, String siteURL) {
        this.name = name;
        this.imageURL = imageURL;
        this.siteURL = siteURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getSiteURL() {
        return siteURL;
    }

    public void setSiteURL(String siteURL) {
        this.siteURL = siteURL;
    }
}
