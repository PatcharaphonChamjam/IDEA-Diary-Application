package com.diary.deardiary.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.diary.deardiary.Network.APIService;
import com.diary.deardiary.Network.ApiClient;
import com.diary.deardiary.Network.RSM;
import com.diary.deardiary.R;

import java.text.ParseException;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class ShowDiaryPicActivity extends AppCompatActivity {


    @Bind(R.id.ImgDiary3)ImageView _imgdiary;
    @Bind(R.id.diID4)TextView _idreview;
    @Bind(R.id.picname4)TextView _picname;
    @Bind(R.id.TxtStory3) TextView _txttitle;
    @Bind(R.id.TxtDate) TextView _txtdate;
    @Bind(R.id.TxtTime2) TextView _txttime;
    @Bind(R.id.Txtdiary3) TextView _txtdiary;
    @Bind(R.id.ip) TextView _ipdiary;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_diary_pic);
        ButterKnife.bind(this);

        Intent intent = this.getIntent();

        Bundle bundle = intent.getExtras();

        if(bundle != null) {
            String idreview = (String) bundle.get("id") ;
            String title = (String) bundle.get("title");
            String story = (String) bundle.get("story");
            String picture = (String) bundle.get("image");
            String datet = (String) bundle.get("Datetime");
            String time = (String) bundle.get("Datetime") ;

            _txttitle.setText(title);
            _txtdiary.setText(story);
            _txtdate.setText(convertDate(time));
           _txttime.setText(convertTime(datet));
            _idreview.setText(idreview);
            _picname.setText(picture);

            Glide.with(ShowDiaryPicActivity.this)
                    .load(picture)
                    .into(_imgdiary);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editmenu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuedit:
                edit();
                Log.e("Run ShowDiaryPicActivity", "onOptionsItemSelected:clickedit " );
                return true;
            case R.id.menudelete:
                delete();
                Log.e("Run ShowDiaryPicActivity", "onOptionsItemSelected:clickdelete " );
                return true;

        }return false;

    }

    private void edit(){

        Intent intent = new Intent(ShowDiaryPicActivity.this, EditDiaryPicActivity.class);
        intent.putExtra("id", _idreview.getText().toString());
        intent.putExtra("title", _txttitle.getText().toString());
        intent.putExtra("story", _txtdiary.getText().toString());
        intent.putExtra("picname", _picname.getText().toString());

        startActivity(intent);
        finish();



    }

    private void delete(){

        final String idreview = _idreview.getText().toString();
        Log.e("delete",idreview);

        AlertDialog.Builder builder = new AlertDialog.Builder(ShowDiaryPicActivity.this);
        builder.setTitle("Delete Diary?");
        builder.setMessage("This can't be undone and it will be removed from your diary");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                APIService service = ApiClient.getClient().create(APIService.class);
                service.deletediary(idreview)
                        .asObservable()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Utils.getInstance().defaultSubscribeScheduler())
                        .unsubscribeOn(Utils.getInstance().defaultSubscribeScheduler())
                        .subscribe(new Action1<RSM>() {
                            @Override
                            public void call(RSM response) {
                                if (response.getDeleteclick().getSuccess() == 1) {

                                    Log.e("Run ShowDiaryPicActivity deletePic","Success");
                                    Intent intent = new Intent(ShowDiaryPicActivity.this, HamburgerMenu.class);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(ShowDiaryPicActivity.this, "" + response.getDeleteclick().getMessage(), Toast.LENGTH_SHORT).show();


                                } else {
//                            pdialog.dismiss();
                                    Log.e("ShowDiaryPicActivity","delete3");
                                    Toast.makeText(ShowDiaryPicActivity.this, "" + response.getStatus().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
//                        pdialog.dismiss();
                                Log.e("onFailure", throwable.toString());
//                        Utils.getInstance().onHoneyToast(throwable.getLocalizedMessage());
                            }
                        });


            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog.dismiss();
            }
        });
        builder.show();


    }

    private String convertTime(String time) {

        java.text.SimpleDateFormat numday = new java.text.SimpleDateFormat("HH:mm:ss");
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        java.util.Date date = null;

        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String convertTime = numday.format(date);

        return convertTime;
    }

    private String convertDate(String time) {

        java.text.SimpleDateFormat textdate = new java.text.SimpleDateFormat("dd MMM yyyy");
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        java.util.Date date = null;

        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String convertedDate = textdate.format(date);

        return convertedDate;
    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed(); // this can go before or after your stuff below
        // do your stuff when the back button is pressed
        Intent intent = new Intent(getApplicationContext(), ShowClickActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Log.e("onBackPressed","ShowDiaryPicActivity" );
        startActivity(intent);
        // super.onBackPressed(); calls finish(); for you

        // clear your SharedPreferences
//        getSharedPreferences("preferenceName",0).edit().clear().commit();
    }
}
