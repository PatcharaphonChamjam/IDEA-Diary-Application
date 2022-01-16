package com.diary.deardiary.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.diary.deardiary.Adapter.RecycleViewAdapter;
import com.diary.deardiary.Model.Showper;
import com.diary.deardiary.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class ShowClickActivity extends AppCompatActivity {

    @Bind(R.id.TextDateSC)
    TextView _date;
    @Bind(R.id.TextUserSC)
    TextView _userid;
    LinearLayoutManager mLayoutManager;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_click);
        ButterKnife.bind(this);

        SharedPreferences sp = getSharedPreferences("NICE_LOGIN",MODE_PRIVATE);
//        String username = sp.getString("Username","Null");
        String id = sp.getString("UserID","Null");
         _userid.setText(id);

        SharedPreferences sp2 = getSharedPreferences("Date",MODE_PRIVATE);
//        String username = sp.getString("Username","Null");
        String date = sp2.getString("Datetime","Null");
        _date.setText(date);

        RealmResults<Showper> showper = Realm.getDefaultInstance().where(Showper.class).findAll();
//
        Log.e("onCreateView: ", String.valueOf(showper.size()));

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            String datetime = (String) bundle.get("Datetime") ;
            _date.setText(datetime);
        }

        recyclerView = (RecyclerView)findViewById(R.id.Showcal);
        mLayoutManager = new LinearLayoutManager(ShowClickActivity.this);
        Log.e("Run ShowClickActivity recyclerView: ", String.valueOf(recyclerView));
        recyclerView.setLayoutManager(mLayoutManager);

        RecycleViewAdapter Adapter = new RecycleViewAdapter(getApplicationContext(),showper,true);
        recyclerView.setAdapter(Adapter);

//        if (showper.size()> 0){
//
//        }



    }
}
