package com.diary.deardiary.Network;

import com.diary.deardiary.Model.Changepass;
import com.diary.deardiary.Model.Deleteclick;
import com.diary.deardiary.Model.Diarymy;
import com.diary.deardiary.Model.Editreview;
import com.diary.deardiary.Model.Insert;
import com.diary.deardiary.Model.InsertDi;
import com.diary.deardiary.Model.Regis;
import com.diary.deardiary.Model.Showall;
import com.diary.deardiary.Model.Showper;
import com.diary.deardiary.Model.Status;
import com.diary.deardiary.Model.Updatediary;
import com.diary.deardiary.Model.Updateme;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by USER on 28/7/2560.
 */

public class RSM {
    @SerializedName("Status")
    @Expose
    private Status status;

    @SerializedName("Regis")
    @Expose
    private Regis regis;

    @SerializedName("Diarymy")
    @Expose
    private Diarymy diarymy;

    @SerializedName("Insert")
    @Expose
    private Insert insert;

    @SerializedName("InsertDi")
    @Expose
    private InsertDi insertdi;

    @SerializedName("Deleteclick")
    @Expose
    private Deleteclick deleteclick;

    @SerializedName("Changepass")
    @Expose
    private Changepass changepass;

    @SerializedName("Showall")
    @Expose
    private List<Showall> showall = null;

    @SerializedName("Showper")
    @Expose
    private List<Showper> showper = null;


    @SerializedName("Editreview")
    @Expose
    private List<Editreview> editreview = null;

    @SerializedName("Updateme")
    @Expose
    private Updateme updateme;

    @SerializedName("Updatediary")
    @Expose
    private Updatediary updatediary;

    public Updateme getUpdateme() {
        return updateme;
    }

    public void setUpdateme(Updateme updateme) {
        this.updateme = updateme;
    }

    public Updatediary getUpdatediary() {
        return updatediary;
    }

    public void setUpdatediary(Updatediary updatediary) {
        this.updatediary = updatediary;
    }

    public Deleteclick getDeleteclick() {
        return deleteclick;
    }

    public InsertDi getInsertdi() {
        return insertdi;
    }

    public void setInsertdi(InsertDi insertdi) {
        this.insertdi = insertdi;
    }

    public void setDeleteclick(Deleteclick deleteclick) {
        this.deleteclick = deleteclick;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Regis getRegis() {
        return regis;
    }

    public void setRegis(Regis regis) {
        this.regis = regis;
    }

    public Diarymy getDiarymy() {return diarymy;}

    public void setDiarymy(Diarymy diarymy) {
        this.diarymy = diarymy;
    }

    public Insert getInsert() {
        return insert;
    }

    public void setInsert(Insert insert) {
        this.insert = insert;
    }

    public List<Showall> getShowall() {
        return showall;
    }

    public void setShowall(List<Showall> showall) {
        this.showall = showall;
    }

    public List<Showper> getShowper() {
        return showper;
    }

    public void setShowper(List<Showper> showper) {
        this.showper = showper;
    }

    public List<Editreview> getEditreview() {
        return editreview;
    }

    public void setEditreview(List<Editreview> editreview) {
        this.editreview = editreview;
    }

    public Changepass getChangepass() {
        return changepass;
    }

    public void setChangepass(Changepass changepass) {
        this.changepass = changepass;
    }
}
