package com.diary.deardiary.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.diary.deardiary.Activity.ShowDiaryNopicActivity;
import com.diary.deardiary.Activity.ShowDiaryPicActivity;
import com.diary.deardiary.Activity.Utils;
import com.diary.deardiary.Model.Showper;
import com.diary.deardiary.Network.APIService;
import com.diary.deardiary.Network.ApiClient;
import com.diary.deardiary.Network.RSM;
import com.diary.deardiary.R;
import com.diary.deardiary.ViewHolder.RecycleViewHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by USER on 18/8/2560.
 */

public class RecycleViewAdapter extends RealmRecyclerViewAdapter<Showper,RecyclerView.ViewHolder> {
    private List<Showper> data;
    private Context mcontext;

    public RecycleViewAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<Showper> data, boolean autoUpdate) {
        super(context, data, autoUpdate);

        this.mcontext=context;
        this.data = data;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_calendar,null);
        RecycleViewHolder recycleViewHolder = new RecycleViewHolder(view);
        return recycleViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final Showper get = getData().get(position);
        final RecycleViewHolder itemHolder = (RecycleViewHolder) holder;

        Calendar cal = Calendar.getInstance();

        String dt = get.getDatetime();
        Log.e("dt", dt.toString());


        SimpleDateFormat numday = new SimpleDateFormat("dd");
        SimpleDateFormat textday = new SimpleDateFormat("EE");
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");

//        PicassoClient.loadImage(mcontext,get.getPicDiary(),itemHolder.mImage);

//        Picasso.with(mcontext)
//                .load(get.getPicDiary())
//                .into(itemHolder.mImage);

//        String url = "http://192.168.43.54/" + get.getPicDiary();
//        Log.e("Recycle",url);
        Glide.with(mcontext)

                .load(get.getPicDiary())
                .into(itemHolder.mImage);

        itemHolder.mNumday.setText(convertTime(dt));
        itemHolder.mTextday.setText(convertDate(dt));
        itemHolder.mTitle.setText(get.getTitleDiary());
        itemHolder.mDate.setText(convertClock(dt));
        itemHolder.mIDreview.setText(get.getDiaryID());
        itemHolder.mIDpic.setText((get.getPicDiary()));
//        itemHolder.mImage.setImageDrawable(mcontext.getResources().getDrawable(Integer.parseInt(get.getPicDiary())));
//                itemHolder.mImage.setImageResource(Integer.parseInt(get.getPicDiary()));

//


        itemHolder.mClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String idreview = itemHolder.mIDreview.getText().toString();
                final String idpic = itemHolder.mIDpic.getText().toString();
                Log.e("idpic", idpic );
                Log.e("idreview", idreview );

                APIService sevice = ApiClient.getClient().create(APIService.class);
                sevice.editreview(idreview)
                        .asObservable()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Utils.getInstance().defaultSubscribeScheduler())
                        .unsubscribeOn(Utils.getInstance().defaultSubscribeScheduler())
                        .subscribe(new Action1<RSM>() {
                            @Override
                            public void call(RSM response) {
                                if (get.getPicDiary() != null && get.getPicDiary().length() > 0) {

//                                    Log.e("RecyclePIC", "NOPIC");

//                                }else {

                                    Log.e("editreview", "editreview");
                                    Log.e("RecycleIDPIC", idpic);
                                    Log.e("RecycleIDRE", idreview);
                                    Intent intent = new Intent(mcontext, ShowDiaryPicActivity.class);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


                                    intent.putExtra("title", get.getTitleDiary());
                                    intent.putExtra("story", get.getStoryDiary());
                                    intent.putExtra("id", get.getDiaryID());
                                    intent.putExtra("Datetime", get.getDatetime());
                                    intent.putExtra("image", get.getPicDiary());
//
//
//                                Log.e("call:1 ", response.getEditreview().get(position).getTitleDiary());
//                                Log.e("call:2 ", response.getEditreview().get(position).getStoryDiary());
//                                Log.e("call:3 ", idpic);
//                                    Log.e("call:4 ", response.getEditreview().get(position).getDiaryID());
//                                Log.e("call:5 ", response.getEditreview().get(position).getDatetime());

                                    Log.e("Run RecycleViewAdapter PIC", "Done");
                                    //Toast.makeText(mcontext, "Run RecycleViewAdapter PIC", Toast.LENGTH_SHORT).show();


                                    mcontext.startActivity(intent);
                                }else {
                                    Intent intent = new Intent(mcontext, ShowDiaryNopicActivity.class);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


                                    intent.putExtra("title", get.getTitleDiary());
                                    intent.putExtra("story", get.getStoryDiary());
                                    intent.putExtra("id", get.getDiaryID());
                                    intent.putExtra("Datetime", get.getDatetime());

                                    Log.e("Run RecycleViewAdapter NO PIC", "Done");
                                    //Toast.makeText(mcontext, "Run RecycleViewAdapter NO PIC", Toast.LENGTH_SHORT).show();
                                    mcontext.startActivity(intent);
                                }




                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Log.e("throwable", throwable.getMessage());
                                Toast.makeText(mcontext,throwable.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });


//        itemHolder.mClick.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//
//                PopupMenu popupMenu = new PopupMenu(mcontext,itemHolder.mClick);
//                popupMenu.inflate(R.menu.editmenu);
//                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        switch (item.getItemId()) {
//                            case R.id.menuedit:
//                                Log.e("test", "onMenuItemClick: 1");
//
//                                final String idreview = itemHolder.mIDreview.getText().toString();
//                                final String idpic = itemHolder.mIDpic.getText().toString();
//                                Log.e("idpic", idpic );
//                                Log.e("ireview", idreview );
//
//                                APIService sevice = ApiClient.getClient().create(APIService.class);
//                                sevice.editreview(idreview)
//                                        .asObservable()
//                                        .observeOn(AndroidSchedulers.mainThread())
//                                        .subscribeOn(Utils.getInstance().defaultSubscribeScheduler())
//                                        .unsubscribeOn(Utils.getInstance().defaultSubscribeScheduler())
//                                        .subscribe(new Action1<RSM>() {
//                                            @Override
//                                            public void call(RSM response) {
//
//
//                                                Log.e("editreview", "editreview");
//                                                Log.e("idpic2", idpic);
//                                                Intent intent = new Intent(mcontext, ShowDiaryPicActivity.class);
//                                                intent.putExtra("title",response.getEditreview().get(position).getTitleDiary());
//                                                intent.putExtra("story",response.getEditreview().get(position).getStoryDiary());
//                                                intent.putExtra("id",response.getEditreview().get(position).getDiaryID());
//                                                intent.putExtra("Datetime",response.getEditreview().get(position).getDatetime());
//                                                intent.putExtra("image",idpic);
//
//
//                                                Log.e("call:1 ", response.getEditreview().get(position).getTitleDiary());
//                                                Log.e("call:2 ", response.getEditreview().get(position).getStoryDiary());
//                                                Log.e("call:3 ", idpic);
//                                                Log.e("call:4 ", response.getEditreview().get(position).getDiaryID());
//                                                Log.e("call:5 ", response.getEditreview().get(position).getDatetime());
//                                                Toast.makeText(mcontext, "Clicked Laugh Vote", Toast.LENGTH_SHORT).show();
//                                                mcontext.startActivity(intent);
//
//                                            }
//                                        }, new Action1<Throwable>() {
//                                            @Override
//                                            public void call(Throwable throwable) {
//                                                Log.e("throwable", "onLongclick");
//                                            }
//                                        });
//                                //handle menu1 click
//                                break;
//                            case R.id.menudelete:
//                                Log.e("test2", "onMenuItemClick:2 ");
//                                //handle menu2 click
//                                break;
//
//                        }
//                        return false;
//                    }
//                });
//                popupMenu.show();
//                return false;
//            }
//        });

    }
    private void edit(){



    }
    private String convertTime(String time) {

        SimpleDateFormat numday = new SimpleDateFormat("dd");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        java.util.Date date = null;

        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String convertedDate = numday.format(date);

        return convertedDate;
    }

    private String convertDate(String time) {

        SimpleDateFormat textday = new SimpleDateFormat("EE",Locale.US);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date = null;

        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String convertedDate = textday.format(date);

        return convertedDate;
    }

    private String convertClock(String time) {

        SimpleDateFormat timee = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date = null;

        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String convertedDate = timee.format(date);

        return convertedDate;
    }



    @Override
    public int getItemCount() {

        return data.size();
    }


}
