package com.diary.deardiary.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmModel;
import io.realm.RealmObject;

/**
 * Created by USER on 25/9/2560.
 */

public class Editreview extends RealmObject implements RealmModel {

    @SerializedName("diaryID")
    @Expose
    private String diaryID;
    @SerializedName("userID")
    @Expose
    private String userID;
    @SerializedName("titleDiary")
    @Expose
    private String titleDiary;
    @SerializedName("storyDiary")
    @Expose
    private String storyDiary;
    @SerializedName("Datetime")
    @Expose
    private String datetime;
    @SerializedName("picDiary")
    @Expose
    private String picDiary;

    public String getDiaryID() {
        return diaryID;
    }

    public void setDiaryID(String diaryID) {
        this.diaryID = diaryID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTitleDiary() {
        return titleDiary;
    }

    public void setTitleDiary(String titleDiary) {
        this.titleDiary = titleDiary;
    }

    public String getStoryDiary() {
        return storyDiary;
    }

    public void setStoryDiary(String storyDiary) {
        this.storyDiary = storyDiary;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getPicDiary() {
        return picDiary;
    }

    public void setPicDiary(String picDiary) {
        this.picDiary = picDiary;
    }
}
