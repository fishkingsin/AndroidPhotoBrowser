package com.creedon.androidphotobrowser.common.data.models;

/*
 * Created by troy379 on 10.03.17.
 */
public class CustomImage {

    private String url;
    private String description;

    public CustomImage(String url, String description) {
        this.url = url;
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}