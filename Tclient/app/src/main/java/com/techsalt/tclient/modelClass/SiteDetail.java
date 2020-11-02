package com.techsalt.tclient.modelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SiteDetail {


    @SerializedName("siteId")
    @Expose
    private Integer siteId;
    @SerializedName("siteName")
    @Expose
    private String siteName;

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

}
