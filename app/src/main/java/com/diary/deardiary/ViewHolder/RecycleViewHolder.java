package com.diary.deardiary.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diary.deardiary.R;

/**
 * Created by USER on 18/8/2560.
 */

public class RecycleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public LinearLayout mClick;
    public TextView mNumday;
    public TextView mTextday;
    public TextView mTitle;
    public TextView mDate;
    public TextView mIDreview;
    public ImageView mImage;
    public TextView mIDpic;

    public RecycleViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mClick = (LinearLayout) itemView.findViewById(R.id.layclick);
        mNumday = (TextView) itemView.findViewById(R.id.numdayCal);
        mTextday = (TextView) itemView.findViewById(R.id.textdayCal);
        mTitle = (TextView) itemView.findViewById(R.id.titleCal);
        mDate = (TextView) itemView.findViewById(R.id.dateCal);
        mIDreview = (TextView) itemView.findViewById(R.id.idreviewCal);
        mImage = (ImageView) itemView.findViewById(R.id.picdi);
        mIDpic = (TextView) itemView.findViewById(R.id.idpicCal);

    }

    @Override
    public void onClick(View view) {

    }
}
