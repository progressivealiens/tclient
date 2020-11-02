package com.techsalt.tclient.modelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class clientMappedModel {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("siteDetails")
    @Expose
    private List<SiteDetail> siteDetails = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<SiteDetail> getSiteDetails() {
        return siteDetails;
    }

    public void setSiteDetails(List<SiteDetail> siteDetails) {
        this.siteDetails = siteDetails;
    }


}
