package com.diary.deardiary.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.diary.deardiary.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WriteNopicActivity extends AppCompatActivity {

    @Bind(R.id.TxtWritenopicID)TextView _idreview;
    @Bind(R.id.TxtWritenopicTitle)TextView _title;
    @Bind(R.id.EdtNopic)
    EditText _Edtnopic;
    @Bind(R.id.TxtWritenopicOldtext)TextView _oldtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_nopic);
//        getSupportActionBar().setTitle("Hello");
        ButterKnife.bind(this);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {

            String id = (String) bundle.get("id");
            String story = (String) bundle.get("story");
            String title = (String) bundle.get("title");


            _Edtnopic.setText(story);
            _Edtnopic.setSelection(_Edtnopic.getText().length());

            _idreview.setText(id);
            _title.setText(title);

            _oldtext.setText(story);


        }




    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.post:
                item.setTitle("POST");
                post();
                return true;
//            case R.id.menudelete:
//                delete();
//                return true;

        }return false;

    }


    private void post(){

        Intent intent = new Intent(WriteNopicActivity.this, EditDiaryNopicActivity.class);
        intent.putExtra("story2", _Edtnopic.getText().toString());
        intent.putExtra("id2", _idreview.getText().toString());
        intent.putExtra("title2", _title.getText().toString());

        startActivity(intent);
        finish();
    }

    public void Backpressed(){

        Intent intent = new Intent(WriteNopicActivity.this, EditDiaryNopicActivity.class);
        intent.putExtra("story2", _oldtext.getText().toString());
        intent.putExtra("id2", _idreview.getText().toString());
        intent.putExtra("title2", _title.getText().toString());

        startActivity(intent);
        finish();

    }


    @Override
    public void onBackPressed() {
        Log.e( "onBackPressed: ","done" );
        Backpressed();

    }

}
