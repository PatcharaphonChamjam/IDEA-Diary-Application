package com.diary.deardiary.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
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

public class RegisteruActivity extends AppCompatActivity {

    @Bind(R.id.Etregisuser) EditText _edtuser;
    @Bind(R.id.Etregisemail) EditText _edtemail;
    @Bind(R.id.Etregispass) EditText _edtpass;
    @Bind(R.id.Etregiscon) EditText _edtpasscon;
    @Bind(R.id.RegisShowpass) CheckBox _check;
    @Bind(R.id.Txtregis) TextView _Txtregis;
    @Bind(R.id.Btnregis) Button _btnregis;
    @Bind(R.id.Txtregisnext) TextView _next;
    private ProgressDialog pdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeru);
        getSupportActionBar().hide();
        ButterKnife.bind(this);

        _btnregis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        _check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    _edtpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    _edtpasscon.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    _edtpass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    _edtpasscon.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });


        _next.setPaintFlags(_next.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        _next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisteruActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });

    }

    public void register() {
        if (validate() == false) {
            OnregisterFailed();

            return;
        }
        pdialog = new ProgressDialog(RegisteruActivity.this, R.style.AppTheme);
        pdialog.setIndeterminate(true);
        pdialog.setMessage("Create Account...");
        pdialog.setCancelable(false);
        pdialog.show();
        registerServer();
    }

    public void OnregisterFailed() {
        Toast.makeText(getBaseContext(), "Register Failed", Toast.LENGTH_LONG).show();
        _btnregis.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String username = _edtuser.getText().toString().trim();
        String email = _edtemail.getText().toString().trim();
        String password = _edtpass.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String repass = _edtpasscon.getText().toString().trim();

        if (username.isEmpty() || username.length() < 4) {
            _edtuser.setError("username must be at least 4 characters");
            valid = false;
        } else {
            _edtuser.setError(null);
        }

        if (email.isEmpty() || !email.matches(emailPattern)) {
            _edtemail.setError("Please enter a valid email address");
            valid = false;
        } else {
            _edtemail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _edtpass.setError("Password must be between 4 and 10 characters");
            valid = false;
        } else {
            _edtpass.setError(null);
        }

        if(!password.equals(repass)){
            _edtpasscon.setError("Confirm password doesn't match New password");
            valid=false;
        } else {
            _edtpasscon.setError(null);
        }

        return valid;
    }


    public void registerServer() {

        String username = _edtuser.getText().toString().trim();
        String email = _edtemail.getText().toString().trim();
        String password = _edtpass.getText().toString().trim();


        APIService service = ApiClient.getClient().create(APIService.class);
        service.userRegister(username, email, password)
                .asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Utils.getInstance().defaultSubscribeScheduler())
                .unsubscribeOn(Utils.getInstance().defaultSubscribeScheduler())
                .subscribe(new Action1<RSM>() {
                    @Override
                    public void call(RSM response) {
                        if (response.getRegis().getSuccess() == 1) {
                            Intent intent = new Intent(RegisteruActivity.this, LoginActivity.class);
                            startActivity(intent);
                            Toast.makeText(RegisteruActivity.this, "" + response.getRegis().getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            pdialog.dismiss();
                            Toast.makeText(RegisteruActivity.this, "" + response.getRegis().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }
}
