package com.diary.deardiary.Network;

import com.diary.deardiary.Model.Regis;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by USER on 8/8/2560.
 */

public class MMS {

    @SerializedName("Regis")
    @Expose
    private Regis regis;

    public Regis getRegis() {
        return regis;
    }

    public void setRegis(Regis regis) {
        this.regis = regis;
    }
}
