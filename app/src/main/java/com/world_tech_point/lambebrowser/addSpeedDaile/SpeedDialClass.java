package com.world_tech_point.lambebrowser.addSpeedDaile;

public class SpeedDialClass {


    private int id;
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

    public SpeedDialClass(int id, String name, String imageURL, String siteURL) {
        this.id = id;
        this.name = name;
        this.imageURL = imageURL;
        this.siteURL = siteURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
