package com.diary.deardiary.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.diary.deardiary.Model.Edit;
import com.diary.deardiary.Network.APIService;
import com.diary.deardiary.Network.ApiClient;
import com.diary.deardiary.Network.RSM;
import com.diary.deardiary.R;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class EditDiaryPicActivity extends AppCompatActivity {

    String mediaPath;
    @Bind(R.id.ImgpicDiary)ImageView _imgdiary;
    @Bind(R.id.EdtID)TextView _idreview;
    @Bind(R.id.Txtpicname)TextView _picname;
    @Bind(R.id.EdtpicEdtitle)EditText _Edttitle;
    @Bind(R.id.EdtpicEdDiary) EditText _Edtdiary;
    @Bind(R.id.EdtuserID) TextView _userid;
    private Bitmap bitmap;
    private Uri filePath;
    private static final int IMAGE_REQUEST = 1;
    private ProgressDialog pdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_diary_pic);
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
            String pic = (String) bundle.get("picname");

            String idreview2 = (String) bundle.get("id2");
            String title2 = (String) bundle.get("title2");
            String diary2 = (String) bundle.get("story2");
            String pic2 = (String) bundle.get("pic2");


            if (diary !=null && idreview!=null && title!=null && pic!=null)
            {
                _idreview.setText(idreview);
                _Edttitle.setText(title);
                _Edtdiary.setText(diary);
                _picname.setText(pic);

                Glide.with(EditDiaryPicActivity.this)
                        .load(pic)
                        .into(_imgdiary);

            }else {

                _idreview.setText(idreview2);
                _Edttitle.setText(title2);
                _Edtdiary.setText(diary2);
                _picname.setText(pic2);

                Glide.with(EditDiaryPicActivity.this)
                        .load(pic2)
                        .into(_imgdiary);
            }


        }

        _imgdiary.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             AlertDialog.Builder builder = new AlertDialog.Builder(EditDiaryPicActivity.this);
                                             builder.setMessage("Do you want to replace new Picture?");
                                             builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                 public void onClick(DialogInterface dialog, int id) {
                                                     Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                                     startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_REQUEST);
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
                                     });



        _Edtdiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(EditDiaryPicActivity.this, WritePicActivity.class);

                intent.putExtra("id", _idreview.getText().toString());
                intent.putExtra("story", _Edtdiary.getText().toString());
                intent.putExtra("title", _Edttitle.getText().toString());
                intent.putExtra("pic", _picname.getText().toString());

                startActivity(intent);
                finish();


            }
        });



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
//
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mediaPath = cursor.getString(columnIndex);
                _picname.setText(mediaPath);
                _imgdiary.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
//                _imgdiary.setVisibility(View.VISIBLE);
//                _btndel.setVisibility(View.VISIBLE);
//                _btnpic.setEnabled(false);
                cursor.close();
            }
            else if (requestCode==IMAGE_REQUEST && resultCode==RESULT_CANCELED && data == null){
//                FragmentManager fm = getFragmentManager();
//                FragmentTransaction ft = fm.beginTransaction();
//                Fragment_Diary llf = new Fragment_Diary();
//                ft.replace(R.id.container, llf);
//                ft.commit();

                Toast.makeText(EditDiaryPicActivity.this,"You haven't change Image", Toast.LENGTH_LONG).show();
            }
        }catch(Exception e) {
            Toast.makeText(EditDiaryPicActivity.this,"Somthing went wrong", Toast.LENGTH_LONG).show();

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.update, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update:
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


        pdialog = new ProgressDialog(EditDiaryPicActivity.this, R.style.AppTheme);
        pdialog.setIndeterminate(true);
        pdialog.setMessage("Loading....");
        pdialog.setCancelable(false);
        pdialog.show();

        edit();
    }

    private void OnwriteDiaryFailed(){

        Toast.makeText(EditDiaryPicActivity.this,"Write diary failed", Toast.LENGTH_LONG).show();
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


        if ( mediaPath!= null ) {

            File file = new File(mediaPath);

            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
            MultipartBody.Part picdiary = MultipartBody.Part.createFormData("picDiary", file.getName(), requestBody);
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), file.getName());


            Log.e("diary", "OK2");
            APIService service = ApiClient.getClient().create(APIService.class);
            service.updatediary(picdiary, name, userid, idreview, titlediary, storydiary)
                    .asObservable()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Utils.getInstance().defaultSubscribeScheduler())
                    .unsubscribeOn(Utils.getInstance().defaultSubscribeScheduler())
                    .subscribe(new Action1<Edit>() {
                        @Override
                        public void call(Edit response) {
                            if (response.getSuccess() == true) {
                                Log.e("diary", "OK3");
                                pdialog.dismiss();

                                Intent intent = new Intent(EditDiaryPicActivity.this, HamburgerMenu.class);
                                startActivity(intent);
                                finish();


                                Toast.makeText(EditDiaryPicActivity.this, response.getMessage(), Toast.LENGTH_LONG).show();

                            } else {
                                pdialog.dismiss();
                                Toast.makeText(EditDiaryPicActivity.this, response.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            pdialog.dismiss();
                            Log.e("throwable", throwable.toString());
//                        Utils.getInstance().onHoneyToast(throwable.getLocalizedMessage());
                            Toast.makeText(EditDiaryPicActivity.this, throwable.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
        }else if(mediaPath == null){

//            pdialog.dismiss();

            APIService service = ApiClient.getClient().create(APIService.class);
            service.updateno(userid,idreview,titlediary,storydiary)
                    .asObservable()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Utils.getInstance().defaultSubscribeScheduler())
                    .unsubscribeOn(Utils.getInstance().defaultSubscribeScheduler())
                    .subscribe(new Action1<RSM>() {
                        @Override
                        public void call(RSM response) {
                            if (response.getUpdatediary().getSuccess() == 1) {
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

                                Intent intent = new Intent(EditDiaryPicActivity.this, HamburgerMenu.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(EditDiaryPicActivity.this, "" + response.getUpdatediary().getMessage(), Toast.LENGTH_SHORT).show();


                            } else {
                                pdialog.dismiss();
                                Toast.makeText(EditDiaryPicActivity.this, "" + response.getUpdatediary().getMessage(), Toast.LENGTH_SHORT).show();
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



    }
    @Override
    public void onBackPressed() {
        Log.e( "onBackPressed: ","done" );
        AlertDialog.Builder builder = new AlertDialog.Builder(EditDiaryPicActivity.this);
        builder.setTitle("Unsaved changes!!");
        builder.setMessage("Changes you made will not be saved.");
        builder.setPositiveButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(EditDiaryPicActivity.this, HamburgerMenu.class);
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
