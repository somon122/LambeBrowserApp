package com.world_tech_point.lambebrowser.Database;

public class LinkClass {

    private String link;
    private String title;
    private String logo;

    public LinkClass() {
    }

    public LinkClass(String link, String title, String logo) {
        this.link = link;
        this.title = title;
        this.logo = logo;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
