package com.diary.deardiary.Activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.diary.deardiary.Model.test;
import com.diary.deardiary.Network.APIService;
import com.diary.deardiary.Network.ApiClient;
import com.diary.deardiary.R;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class WriteDiaryWithPic extends Fragment {

    private static final int IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    String mediaPath;
    private ProgressDialog pdialog;
    private Uri filePath;
    @Bind(R.id.Edttitle) EditText _edttitle;
    @Bind(R.id.Btnpicdi) Button _btnpic;
    @Bind(R.id.EdtDiary) EditText _edtdiary;
    @Bind(R.id.ImgDiary)ImageView _imgdiary;
    @Bind(R.id.diID)TextView _userid;
    @Bind(R.id.picname)TextView _picname;
    @Bind(R.id.Btndelete) ImageButton _btndel;

    public WriteDiaryWithPic() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_write_diary_with_pic, container, false);
        ButterKnife.bind(this,view);

        SharedPreferences sp = getActivity().getSharedPreferences("NICE_LOGIN", Context.MODE_PRIVATE);
//        String username = sp.getString("Username","Null");
        String id = sp.getString("UserID","Null");
        _userid.setText(id);
//        Log.e("", id);


        SharedPreferences mess = getContext().getSharedPreferences("Diary",Context.MODE_PRIVATE);
        String title = mess.getString("title","Null");
        String data = mess.getString("data","Null");
        _edttitle.setText(title);
        _edtdiary.setText(data);


        FloatingActionButton save = (FloatingActionButton)view.findViewById(R.id.SaveDiary);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                diary();
//                Toast.makeText(getActivity().getBaseContext(),"Click", Toast.LENGTH_LONG).show();
            }
        });



        _btnpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),IMAGE_REQUEST);
            }
        });
        _btnpic.performClick();

        _btndel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _imgdiary.setImageResource(0);
                _btndel.setVisibility(View.GONE);
                _btnpic.setEnabled(true);

                //test
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment_Diary llf = new Fragment_Diary();
                ft.replace(R.id.container, llf);
                ft.commit();
            }
        });

        return view;


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IMAGE_REQUEST && resultCode==RESULT_OK && data != null)
        {
//            Uri path  = data.getData();
//
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),path);
//                _imgdiary.setImageBitmap(bitmap);
//                _imgdiary.setVisibility(View.VISIBLE);
//                _btnpic.setEnabled(false);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            mediaPath = cursor.getString(columnIndex);
            _picname.setText(mediaPath);
            _imgdiary.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
            _imgdiary.setVisibility(View.VISIBLE);
            _btndel.setVisibility(View.VISIBLE);
            _btnpic.setEnabled(false);
            cursor.close();
        }
        else if (requestCode==IMAGE_REQUEST && resultCode==RESULT_CANCELED && data == null){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment_Diary llf = new Fragment_Diary();
            ft.replace(R.id.container, llf);
            ft.commit();
        }


    }

//
//

    private void diary(){
//
        if (validate() == false){
            OnwriteDiaryFailed();
            return ;
        }
//        Log.e( "", "ProgressDialog");
        pdialog = new ProgressDialog(getActivity(), R.style.AppTheme);
        pdialog.setIndeterminate(true);
        pdialog.setMessage("Loading....");
        pdialog.setCancelable(false);
        pdialog.show();

        saveDiary();
    }

    private void OnwriteDiaryFailed(){

        Toast.makeText(getActivity().getBaseContext(),"Write diary failed", Toast.LENGTH_LONG).show();
    }


    private boolean validate(){
        boolean valid = true;

        String titlediary = _edttitle.getText().toString();
        String storydiary = _edtdiary.getText().toString();

        if (titlediary.isEmpty() || titlediary.length()<4){


            _edttitle.setError("Title Diary must be at least 4 characters");
            valid=false;
        }else {
            _edttitle.setError(null);
        }

        if (storydiary.isEmpty() || storydiary.length()<20){
            _edtdiary.setError("Story Diary must be at least 20 characters");
            valid=false;
        }else {
            _edtdiary.setError(null);
        }
        return valid;
    }

    private void saveDiary(){

        String titlediary = _edttitle.getText().toString();
        String storydiary = _edtdiary.getText().toString();
        String userid = _userid.getText().toString();



        File file = new File(mediaPath);

        // Parsing any Media type file
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part picdiary = MultipartBody.Part.createFormData("picDiary", file.getName(), requestBody);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), file.getName());


        Log.e( "WriteDiaryWithPic", "saveDiary()");
        APIService service = ApiClient.getClient().create(APIService.class);
        service.uploadFile(picdiary,name,userid,titlediary,storydiary)
                .asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Utils.getInstance().defaultSubscribeScheduler())
                .unsubscribeOn(Utils.getInstance().defaultSubscribeScheduler())
                .subscribe(new Action1<test>(){
                    @Override
                    public void call(test response) {
                        if (response.getSuccess() == true){
                            Log.e( "Run WriteDiaryWithPic", "APIService");
                            pdialog.dismiss();
//                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                            ft.replace(R.id.container, new Fragment_Calendar());
//                            ft.commit();

                            FragmentManager fm = getFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            Fragment_Calendar llf = new Fragment_Calendar();
                            ft.replace(R.id.container, llf);
                            ft.commit();

                            Toast.makeText(getContext(),"Insert Diary Sucessfully", Toast.LENGTH_LONG).show();

                        }else {
                            pdialog.dismiss();
                            Toast.makeText(getContext(),"Insert Diary Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        pdialog.dismiss();
                        Log.e("throwable", throwable.toString());
//                        Utils.getInstance().onHoneyToast(throwable.getLocalizedMessage());
                        Toast.makeText(getContext(),throwable.toString(), Toast.LENGTH_LONG).show();
                    }
                });

    }


//    private String imagetoString(){
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
//        byte[] imgByte = byteArrayOutputStream.toByteArray();
//        return Base64.encodeToString(imgByte,Base64.DEFAULT);
//    }



}
