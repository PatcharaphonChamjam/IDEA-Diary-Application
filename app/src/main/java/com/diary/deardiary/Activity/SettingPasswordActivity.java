package com.diary.deardiary.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
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

public class SettingPasswordActivity extends AppCompatActivity {

    @Bind(R.id.Etcurrpass) EditText _Edtcurrpass;
    @Bind(R.id.Etnewpass) EditText _Edtnewpass;
    @Bind(R.id.Etrenewpass) EditText _Edtrepass;
    @Bind(R.id.Btnchange)Button _Btnchange;
    @Bind(R.id.TxtChangeID) TextView _Txtid;
    @Bind(R.id.Showpass) CheckBox _Showpass;
    private ProgressDialog pdialog;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_password);
        ButterKnife.bind(this);

        sp = getSharedPreferences("NICE_LOGIN", Context.MODE_PRIVATE);
        String userid = sp.getString("UserID", " ");

        _Txtid.setText(userid);

        _Showpass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    _Edtcurrpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    _Edtrepass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    _Edtnewpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    _Edtcurrpass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    _Edtrepass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    _Edtnewpass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        _Btnchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Change();
            }
        });

    }

    public void Change() {
        if (Validate() == false) {
            Log.e("Validate2: ","done");
            OnChangeFailed();
            return;
        }
        Log.e( "Connect1: ","done" );
        pdialog = new ProgressDialog(SettingPasswordActivity.this, R.style.AppTheme);
        pdialog.setIndeterminate(true);
        pdialog.setMessage("Changing password...");
        pdialog.setCancelable(false);
        pdialog.show();
        ChangeServer();
    }

    public void OnChangeFailed(){
        Toast.makeText(getBaseContext(), "Change Password Failed", Toast.LENGTH_LONG).show();
        _Btnchange.setEnabled(false);

    }

    private void ChangeServer() {


        String password = _Edtnewpass.getText().toString().trim();
        String userid = _Txtid.getText().toString();

        Log.e( "Connect2: ","done" );
        APIService service = ApiClient.getClient().create(APIService.class);
        service.changepass(userid,password)
                .asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Utils.getInstance().defaultSubscribeScheduler())
                .unsubscribeOn(Utils.getInstance().defaultSubscribeScheduler())
                .subscribe(new Action1<RSM>() {
                    @Override
                    public void call(RSM response) {

                        if (response.getChangepass().getSuccess() == 1) {
                            Log.e( "Connect3: ","done" );
                            Intent intent = new Intent(SettingPasswordActivity.this, LoginActivity.class);
                            startActivity(intent);
                            Toast.makeText(SettingPasswordActivity.this, "" + response.getRegis().getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            Log.e( "Disconnect: ","done" );
                            pdialog.dismiss();
                            Toast.makeText(SettingPasswordActivity.this, "" + response.getRegis().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        pdialog.dismiss();
                        Log.e("onFailure", throwable.toString());
//                        Utils.getInstance().onHoneyToast(throwable.getLocalizedMessage());
                    }
                } );


    }


    public boolean Validate(){

        boolean valid = true;
        Log.e( "Validate: ","done" );

        String currpass = _Edtcurrpass.getText().toString().trim();
        String newpass = _Edtnewpass.getText().toString().trim();
        String renewpass = _Edtrepass.getText().toString().trim();

        if (currpass.isEmpty()  || currpass.length() < 4 || currpass.length() > 10){
            _Edtcurrpass.setError("Please enter your Current Password");
            valid=false;
        }else {_Edtcurrpass.setError(null);}

        if (newpass.isEmpty() || newpass.length() < 4 || newpass.length() > 10){
            _Edtnewpass.setError("New password must be between 4 and 10 characters");
            valid=false;
        }else {_Edtnewpass.setError(null);}

        if (renewpass.isEmpty() || renewpass.length() < 4 || renewpass.length() > 10){
            _Edtrepass.setError("Confirm password must be between 4 and 10 characters");
            valid=false;
        }else {_Edtrepass.setError(null);}

        if(!renewpass.equals(newpass)){
            _Edtrepass.setError("Confirm password password doesn't match New password");
            valid=false;
        }
        return valid;
    }

}
