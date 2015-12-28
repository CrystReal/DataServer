package com.updg.SCDataServer.JSONObjects.out;

/**
 * Created by Alex
 * Date: 04.02.14  1:39
 */
public class APIProject {
    private int id;
    private String name;
    private String displayName;
    private String siteUrl;
    private String serverUrl;

    public APIProject() {
        this.id = 0;
        this.name = "ERR";
        this.displayName = "ERR";
        this.siteUrl = "ERR";
        this.serverUrl = "ERR";
    }

    public APIProject(int id, String name, String displayName, String siteUrl, String serverUrl) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.siteUrl = siteUrl;
        this.serverUrl = serverUrl;
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
