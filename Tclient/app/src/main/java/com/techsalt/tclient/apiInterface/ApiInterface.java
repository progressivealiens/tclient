package com.techsalt.tclient.apiInterface;

import com.techsalt.tclient.modelClass.absenteseDataModel;
import com.techsalt.tclient.modelClass.attandanceDataModel;
import com.techsalt.tclient.modelClass.clientMappedModel;
import com.techsalt.tclient.modelClass.clientProfileModelClass;
import com.techsalt.tclient.modelClass.model;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("clientMobileLogin")
    Call<model> clientMobileLogin(
            @Field("token") String token,
            @Field("companyName") String companyName,
            @Field("clientEmail") String clientEmail,
            @Field("clientCode") String clientCode
    );

    @FormUrlEncoded
    @POST("clientSubmitSiteIssueMail")
    Call<model> clientSubmitSiteIssueMail(
            @Field("token") String token,
            @Field("token") int siteId,
            @Field("issueType") String issueType,
            @Field("subject") String subject,
            @Field("employeeName") String employeeName,
            @Field("issue") String issue,
            @Field("image") String image
    );

    @FormUrlEncoded
    @POST("clientMappedSite")
    Call<clientMappedModel> clientMappedSite(
            @Field("token") String token);

    @FormUrlEncoded
    @POST("clientMobileLogout")
    Call<model> clientMobileLogout(
            @Field("token") String token);

    @GET("clientMobileAbsentAttendance?")
    Call<absenteseDataModel> clientMobileAbsentAttendance(
            @Query("token") String token,
            @Query("siteId") String siteId,
            @Query("dateSearchData") String dateSearchData);


    @GET("clientMobileAttendance")
    Call<attandanceDataModel> clientMobileAttendance(
            @Query("token") String token,
            @Query("siteId") int siteId,
            @Query("dateSearchData") String dateSearchData);

    @GET("clientProfile")
    Call<clientProfileModelClass> clientProfile(
            @Query("token") String token);

}
