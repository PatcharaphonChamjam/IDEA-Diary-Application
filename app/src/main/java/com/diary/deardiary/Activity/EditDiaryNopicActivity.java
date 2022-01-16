package com.diary.deardiary.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.diary.deardiary.Network.APIService;
import com.diary.deardiary.Network.ApiClient;
import com.diary.deardiary.Network.RSM;
import com.diary.deardiary.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class EditDiaryNopicActivity extends AppCompatActivity {

    @Bind(R.id.EdtID2)TextView _idreview;
    @Bind(R.id.EdtpicEdtitle2)EditText _Edttitle;
    @Bind(R.id.EdtpicEdDiary2) EditText _Edtdiary;
    @Bind(R.id.EdtuserID2) TextView _userid;
    private ProgressDialog pdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_diary_nopic);
        ButterKnife.bind(this);

        SharedPreferences sp = getSharedPreferences("NICE_LOGIN", Context.MODE_PRIVATE);
//        String username = sp.getString("Username","Null");
        String id = sp.getString("UserID","Null");
        _userid.setText(id);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null) {

            String idreview = (String) bundle.get("id");
            String title = (String) bundle.get("title");
            String diary = (String) bundle.get("story");


            String idreview2 = (String) bundle.get("id2");
            String title2 = (String) bundle.get("title2");
            String diary2 = (String) bundle.get("story2");



            if (diary !=null && idreview!=null && title!=null)
            {
                _idreview.setText(idreview);
                _Edttitle.setText(title);
                _Edtdiary.setText(diary);


            }else {

                _idreview.setText(idreview2);
                _Edttitle.setText(title2);
                _Edtdiary.setText(diary2);

            }


        }

        _Edtdiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditDiaryNopicActivity.this, WriteNopicActivity.class);

                intent.putExtra("id", _idreview.getText().toString());
                intent.putExtra("story", _Edtdiary.getText().toString());
                intent.putExtra("title", _Edttitle.getText().toString());


                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nopic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nopic:
//                item.setTitle("UPDATE");
                diary();
                return true;
//            case R.id.menudelete:
//                delete();
//                return true;

        }return false;

    }

    private void diary(){
//
        if (validate() == false){
            OnwriteDiaryFailed();
            return ;
        }
        Log.e( "diary", "OK1");
        pdialog = new ProgressDialog(EditDiaryNopicActivity.this, R.style.AppTheme);
        pdialog.setIndeterminate(true);
        pdialog.setMessage("Loading....");
        pdialog.setCancelable(false);
        pdialog.show();

        edit();
    }

    private void OnwriteDiaryFailed(){

        Toast.makeText(EditDiaryNopicActivity.this,"Update diary failed", Toast.LENGTH_LONG).show();
    }

    private boolean validate(){
        boolean valid = true;

        String titlediary = _Edttitle.getText().toString();
        String storydiary = _Edtdiary.getText().toString();

        if (titlediary.isEmpty() || titlediary.length()<4){


            _Edttitle.setError("Title Diary must be at least 4 characters");
            valid=false;
        }else {
            _Edttitle.setError(null);
        }

        if (storydiary.isEmpty() || storydiary.length()<20){
            _Edtdiary.setError("Story Diary must be at least 20 characters");
            valid=false;
        }else {
            _Edtdiary.setError(null);
        }
        return valid;
    }


    public void edit() {

        String titlediary = _Edttitle.getText().toString();
        String storydiary = _Edtdiary.getText().toString();
        String userid = _userid.getText().toString();
        String idreview = _idreview.getText().toString();
        Log.e("edit",titlediary);
        Log.e("edit",storydiary);
        Log.e("edit",userid);
        Log.e("edit",idreview);



            APIService service = ApiClient.getClient().create(APIService.class);
            service.updateme(userid,idreview,titlediary,storydiary)
                    .asObservable()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Utils.getInstance().defaultSubscribeScheduler())
                    .unsubscribeOn(Utils.getInstance().defaultSubscribeScheduler())
                    .subscribe(new Action1<RSM>() {
                        @Override
                        public void call(RSM response) {
                            if (response.getUpdateme().getSuccess() == 1) {
//                    sharePreference();
                                pdialog.dismiss();
//                                Log.e( "UserID", response.getStatus().getUserID().toString());
//
//                                String userid = response.getStatus().getUserID().toString();
//                                String username = _Edtuser.getText().toString();
//
//                                SharedPreferences sp = getBaseContext().getSharedPreferences("NICE_LOGIN", Context.MODE_PRIVATE);
//                                SharedPreferences.Editor editor = sp.edit();
//
////                            String userid = sp.getString("UserID","Null");
//
//                                editor.putInt("KEY_LOGIN", 1);
//                                editor.putString("Username", username);
//                                editor.putString("UserID", userid);
//                                editor.apply();

                                Intent intent = new Intent(EditDiaryNopicActivity.this, HamburgerMenu.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(EditDiaryNopicActivity.this, "" + response.getUpdateme().getMessage(), Toast.LENGTH_SHORT).show();


                            } else {
                                pdialog.dismiss();
                                Toast.makeText(EditDiaryNopicActivity.this, "" + response.getUpdateme().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            pdialog.dismiss();
                            Log.e("onFailure", throwable.toString());
//                        Utils.getInstance().onHoneyToast(throwable.getLocalizedMessage());
                        }
                    });




    }

    @Override
    public void onBackPressed() {
        Log.e( "onBackPressed: ","done" );
        AlertDialog.Builder builder = new AlertDialog.Builder(EditDiaryNopicActivity.this);
        builder.setTitle("Unsaved changes!!");
        builder.setMessage("Changes you made will not be saved.");
        builder.setPositiveButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(EditDiaryNopicActivity.this, HamburgerMenu.class);
                startActivity(intent);
                finish();

            }
        });
        builder.setNegativeButton("Keep Editing", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        builder.show();
    }
}
