package com.diary.deardiary.Activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.diary.deardiary.Model.Showall;
import com.diary.deardiary.Model.Showper;
import com.diary.deardiary.Network.APIService;
import com.diary.deardiary.Network.ApiClient;
import com.diary.deardiary.Network.RSM;
import com.diary.deardiary.R;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class Fragment_Calendar extends Fragment {

    private CaldroidFragment mCaldroidFragment;
    RecyclerView recyclerView;
    LinearLayoutManager mLayoutManager;

    @Bind(R.id.caltext)
    TextView _userid;
    public FloatingActionButton fab;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        ButterKnife.bind(this,view);

        SharedPreferences sp = this.getActivity().getSharedPreferences("NICE_LOGIN", Context.MODE_PRIVATE);
//        String username = sp.getString("Username","Null");
        String id = sp.getString("UserID","Null");
        _userid.setText(id);

        fab = (FloatingActionButton) view.findViewById(R.id.FloatBTcal);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.container, new Fragment_Diary());
                ft.commit();

            }
        });



        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"+"%");
        String date = (DateFormat.format("dd-MM-yyyy", new java.util.Date()).toString());
        final SimpleDateFormat form = new SimpleDateFormat("EE", Locale.US); // Day name in week


        mCaldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        Date selectedDate = Calendar.getInstance().getTime();

        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));

        args.putInt(CaldroidFragment.THEME_RESOURCE, R.style.CaldroidDefaultPINK);
        args.putInt( CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.SUNDAY);

        mCaldroidFragment.setArguments( args );

        getActivity().getSupportFragmentManager().beginTransaction().replace( R.id.cal_container , mCaldroidFragment ).commit();
        connect();


        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"+"%");
                final String datetime = formatter.format(date);
                Log.e("Fragment_Calendar datetime", datetime);
                String userid = _userid.getText().toString();
                Log.e("Fragment_Calendar userid", userid);

                APIService service = ApiClient.getClient().create(APIService.class);
                service.showclick(userid,datetime)
                        .asObservable()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Utils.getInstance().defaultSubscribeScheduler())
                        .unsubscribeOn(Utils.getInstance().defaultSubscribeScheduler())
                        .subscribe(new Action1<RSM>() {
                            @Override
                            public void call(RSM response) {

                                Log.e("Fragment_Calendar RealmTest: ", String.valueOf(response.getShowper().size()));

                                Realm realm = Realm.getDefaultInstance();
                                realm.beginTransaction();
                                realm.delete(Showper.class);
                                realm.copyToRealmOrUpdate(response.getShowper());
                                realm.commitTransaction();


                                SharedPreferences sp2 = getActivity().getSharedPreferences("Date", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp2.edit();

//                            String userid = sp.getString("UserID","Null");

                                editor.putInt("KEY_Date", 2);

                                editor.putString("Datetime", datetime);
                                editor.apply();

                                Log.e("Run Fragment_Calendar Click","Done" );
                                Intent intent = new Intent(getActivity(), ShowClickActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                intent.putExtra("Datetime", datetime);
//                                Log.e("intent.datetime", datetime);
                                startActivity(intent);



                            }

                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Toast.makeText(getActivity(), throwable.getMessage(),Toast.LENGTH_SHORT).show();
//                        Utils.getInstance().onHoneyToast(throwable.getLocalizedMessage());
                            }
                        });

            }

            @Override
            public void onChangeMonth(int month, int year) {
//                String text = "month: " + month + " year: " + year;
//                Toast.makeText(getActivity(), text,
//                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClickDate(Date date, View view) {
//                Toast.makeText(getActivity(),
//                        "Long click " + formatter.format(date),
//                        Toast.LENGTH_SHORT).show();
            }

//            @Override
//            public void onCaldroidViewCreated() {
//                Toast.makeText(getActivity(),
//                        "Caldroid view is created",
//                        Toast.LENGTH_SHORT).show();
//            }

        };

        mCaldroidFragment.setCaldroidListener(listener);

        return view;
    }



    private void connect(){


        String userid = _userid.getText().toString();
        Log.e("Fragment_Calendar userid 2", userid);

        APIService service = ApiClient.getClient().create(APIService.class);
        service.showall(userid)
                .asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Utils.getInstance().defaultSubscribeScheduler())
                .unsubscribeOn(Utils.getInstance().defaultSubscribeScheduler())
                .subscribe(new Action1<RSM>() {
                    @Override
                    public void call(RSM response) {
                        ColorDrawable pink = new ColorDrawable(getResources().getColor(R.color.colorBackgtheme));
                        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.blackbgcircle, null);

//                        String dt = "17-08-2017";
//                        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
//                        Date teste = null;
//                        try {
//                            teste = sdf.parse(dt);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                        mCaldroidFragment.setBackgroundDrawableForDate(blue,teste);
                        List<Showall> StudentData = response.getShowall();
                        for(int i=1;i <= StudentData.size();i++) {
                            String dt = StudentData.get(i).getDatetime();
                            Log.e("data", dt);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");

                            Date teste = null;
                            try {
                                teste = sdf.parse(dt);
                                Log.e("Run Fragment_Calendar Show data",teste.toString() );
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            mCaldroidFragment.setBackgroundDrawableForDate(drawable, teste);
                            mCaldroidFragment.refreshView();
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("onFailure", throwable.toString());
                    }
                });
    }




}
