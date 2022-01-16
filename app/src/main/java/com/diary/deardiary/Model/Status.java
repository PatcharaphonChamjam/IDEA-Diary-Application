package com.diary.deardiary.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmModel;
import io.realm.RealmObject;

/**
 * Created by USER on 28/7/2560.
 */

public class Status extends RealmObject implements RealmModel {

    @SerializedName("success")
    @Expose
    private Integer success;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("userID")
    @Expose
    private String userID;

    public Integer getSuccess() {
        return success;
    }


    @SerializedName("Showper")
    @Expose
    private String Showper;

    public String getShowper() {
        return Showper;
    }

    public void setShowper(String showper) {
        Showper = showper;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
