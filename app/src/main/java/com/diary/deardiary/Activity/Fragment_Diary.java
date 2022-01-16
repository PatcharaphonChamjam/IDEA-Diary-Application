package com.diary.deardiary.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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


public class Fragment_Diary extends Fragment {

//    private ProgressDialog pdialog;

//    private static final int IMAGE_REQUEST = 1;
//    private Bitmap bitmap;
    private ProgressDialog pdialog;
    @Bind(R.id.EdtStoryName2)EditText _edttitle;
    @Bind(R.id.BtnStoryPic2)Button _btnpic;
    @Bind(R.id.EdtStoryData2)EditText _edtdata;
    @Bind(R.id.userid)TextView _userid;
    @Bind(R.id.Txtda)TextView _date;



    public Fragment_Diary() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_diary, container, false);
        ButterKnife.bind(this,view);


        SharedPreferences sp = getActivity().getSharedPreferences("NICE_LOGIN", Context.MODE_PRIVATE);
//        String username = sp.getString("Username","Null");
        String id = sp.getString("UserID","Null");
        _userid.setText(id);
        Log.e("userid", id);

//        String edtnopic = getArguments().getString("diary");
//        _edtdata.setText(edtnopic);

        FloatingActionButton save = (FloatingActionButton) view.findViewById(R.id.SaveStory2);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert();
                Log.e("Fragment_Diary saveDiary: ", "0");
            }
        });

        _btnpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String title = _edttitle.getText().toString();
                String data = _edtdata.getText().toString();

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                WriteDiaryWithPic llf = new WriteDiaryWithPic();


                SharedPreferences.Editor editor = getContext().getSharedPreferences("Diary",Context.MODE_PRIVATE).edit();
                editor.putString("title",title);
                editor.putString("data",data);
                editor.apply();

                ft.replace(R.id.container, llf);
                ft.commit();
            }
        });

//        _edtdata.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), WriteNopicActivity.class);
//
//
//                startActivity(intent);
//
//            }
//        });




        return view;
    }



//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode==IMAGE_REQUEST && resultCode==RESULT_OK && data != null)
//        {
//            Uri path = data.getData();
//
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),path);
//                _img.setImageBitmap(bitmap);
//                _img.setVisibility(View.VISIBLE);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    private void insert(){

        if (validate() == false){
            OnwriteDiaryFailed();
            return ;
        }

        pdialog = new ProgressDialog(getActivity(), R.style.AppTheme);
        pdialog.setIndeterminate(true);
        pdialog.setMessage("Loading....");
        pdialog.setCancelable(false);
        pdialog.show();
        Log.e("Fragment_Diary saveDiary: ", "0.1");
        saveDiary();
    }

    public void OnwriteDiaryFailed(){

        Toast.makeText(getActivity().getBaseContext(),"Write diary failed", Toast.LENGTH_LONG).show();
    }


    public boolean validate(){
        boolean valid = true;

        String titlediary = _edttitle.getText().toString();
        String storydiary = _edtdata.getText().toString();

        if (titlediary.isEmpty() || titlediary.length()<4){
            _edttitle.setError("Title Diary must be at least 4 characters");
            valid=false;
        }else {
            _edttitle.setError(null);
        }

        if (storydiary.isEmpty() || storydiary.length()<20){
            _edtdata.setError("Story Diary must be at least 20 characters");
            valid=false;
        }else {
            _edtdata.setError(null);
        }
        return valid;
    }

    public void saveDiary(){

        String titlediary = _edttitle.getText().toString();
        String storydiary = _edtdata.getText().toString();
        String userid = _userid.getText().toString();
        String datetime = _date.getText().toString();



        Log.e("Fragment_Diary saveDiary: ", "1");
        APIService service = ApiClient.getClient().create(APIService.class);
        service.insertDiary(userid,titlediary,storydiary)
                .asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Utils.getInstance().defaultSubscribeScheduler())
                .unsubscribeOn(Utils.getInstance().defaultSubscribeScheduler())
                .subscribe(new Action1<RSM>() {
                    @Override
                    public void call(RSM response) {
                        if (response.getInsert().getSuccess() == true) {
                            pdialog.dismiss();
                            Log.e("Run Fragment_Diary saveDiary: ", "2");
                            FragmentManager fm = getFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            Fragment_Calendar llf = new Fragment_Calendar();
                            ft.replace(R.id.container, llf);
                            ft.commit();

                            Toast.makeText(getContext(),"Insert Diary Sucessfully", Toast.LENGTH_LONG).show();

                        } else {
                            pdialog.dismiss();
                            Toast.makeText(getContext(),"Insert Diary Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        pdialog.dismiss();
                        Log.e("saveDiary: ", throwable.toString());
                        Toast.makeText(getContext(),throwable.toString(), Toast.LENGTH_LONG).show();
                    }
                });

    }



}
