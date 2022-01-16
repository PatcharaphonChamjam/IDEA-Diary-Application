package com.diary.deardiary.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.Etloguser) EditText _Edtuser;
    @Bind(R.id.Etlogpass) EditText _Edtpass;
    @Bind(R.id.Btnlog) Button _Btnlog;
    @Bind(R.id.Txtlognext) TextView _next;
    @Bind(R.id.Showpass) CheckBox _check;
    private ProgressDialog pdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        ButterKnife.bind(this);

        _check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    _Edtpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    _Edtpass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });


        _next.setPaintFlags(_next.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        _next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisteruActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        _Btnlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Login1","OK");
                login();
            }
        });
    }

    public void login(){
    if (Validate() == false){
        Log.e("Login2","OK");
        OnloginFailed();

        return;
    }
      pdialog = new ProgressDialog(LoginActivity.this,R.style.AppTheme);
        pdialog.setIndeterminate(true);
        pdialog.setMessage("Login Loading");
        pdialog.setCancelable(false);
        pdialog.show();
        loginByserver();
    }

    public void OnloginFailed(){
        Toast.makeText(getBaseContext(),"Login Failed",Toast.LENGTH_LONG).show();
        _Btnlog.setEnabled(false);


    }

    public boolean Validate(){

        boolean valid = true;

        String username = _Edtuser.getText().toString();
        String password = _Edtpass.getText().toString();

        if (username.isEmpty() ){
            _Edtuser.setError("Please enter Username");
            requeseFocus(_Edtuser);
            valid=false;
        }else {
            _Edtuser.setError(null);
        }if (password.isEmpty()){
            _Edtpass.setError("Please enter Password");
            requeseFocus(_Edtpass);
            valid=false;
        }else {
            _Edtpass.setError(null);
        }
        return valid;
    }

    public void loginByserver() {

        String username = _Edtuser.getText().toString();
        String password = _Edtpass.getText().toString();

        APIService service = ApiClient.getClient().create(APIService.class);
        service.userLogin(username, password)
                .asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Utils.getInstance().defaultSubscribeScheduler())
                .unsubscribeOn(Utils.getInstance().defaultSubscribeScheduler())
                .subscribe(new Action1<RSM>() {
                    @Override
                    public void call(RSM response) {
                        if (response.getStatus().getSuccess() == 1) {
//                    sharePreference();
                            pdialog.dismiss();
                            Log.e( "Run Login Activity : UserID", response.getStatus().getUserID().toString());

                            String userid = response.getStatus().getUserID().toString();
                            String username = _Edtuser.getText().toString();

                            SharedPreferences sp = getBaseContext().getSharedPreferences("NICE_LOGIN", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();

//                            String userid = sp.getString("UserID","Null");

                            editor.putInt("KEY_LOGIN", 1);
                            editor.putString("Username", username);
                            editor.putString("UserID", userid);
                            editor.apply();

                            Intent intent = new Intent(LoginActivity.this, HamburgerMenu.class);
                            startActivity(intent);
                            finish();


                        } else {
                            pdialog.dismiss();
                            Toast.makeText(LoginActivity.this, "" + response.getStatus().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        pdialog.dismiss();
                        Log.e("Login Activity onFailure", throwable.toString());
//                        Utils.getInstance().onHoneyToast(throwable.getLocalizedMessage());
                    }
                });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void requeseFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

}
