package com.diary.deardiary.Activity;

import android.content.Context;

/**
 * Created by USER on 28/7/2560.
 */

public class Contextor {
    private static Contextor instance;
    private Context mConext;

    public Contextor(){}

    public  static Contextor getInstance(){
        if(instance == null){
            instance = new Contextor();
        }
        return instance;
    }

    public void init(Context context){
        mConext = context;
    }

    public  Context getContext(){
        return mConext;
    }
}
