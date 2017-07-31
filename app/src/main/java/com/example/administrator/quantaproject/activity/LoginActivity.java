package com.example.administrator.quantaproject.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.quantaproject.R;
import com.example.administrator.quantaproject.data.PingTai_Config;
import com.example.administrator.quantaproject.net.Login;

/**
 * Created by Administrator on 2017/4/16.
 */

public class LoginActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etPhoneNum = (EditText)findViewById(R.id.etPhoneNum);
        etPassword = (EditText)findViewById(R.id.etPassword);

        findViewById(R.id.btnLoin_With_Password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(etPhoneNum.getText())){
                    Toast.makeText(LoginActivity.this, R.string.accout_can_not_be_null,Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(etPassword.getText())){
                    Toast.makeText(LoginActivity.this, R.string.password_can_not_be_empty,Toast.LENGTH_SHORT).show();
                    return;
                }

                new Login(etPhoneNum.getText().toString(),etPassword.getText().toString(), new Login.SuccessCallback() {
                    @Override
                    public void onSuccess(String token) {
                        PingTai_Config.cachToken(LoginActivity.this,token);
                        PingTai_Config.cachPhoneNum(LoginActivity.this,etPhoneNum.getText().toString());

                        Intent Turn_to_MovementAty = new Intent(LoginActivity.this,MovementActivity.class);
                        Turn_to_MovementAty.putExtra(PingTai_Config.KEY_TOKEN,token);
                        Turn_to_MovementAty.putExtra(PingTai_Config.KEY_PHONE_NUM,etPhoneNum.getText().toString());

                        startActivity(Turn_to_MovementAty);

                        finish();
                    }
                }, new Login.FailCallback() {
                    @Override
                    public void onFail() {
                        Toast.makeText(LoginActivity.this, R.string.Fail_to_login,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        findViewById(R.id.SignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent TurnToSignUpAty = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivityForResult(TurnToSignUpAty,0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case PingTai_Config.ACTIVITY_RESULT_FINISH:
                finish();
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private EditText etPhoneNum = null;
    private EditText etPassword = null;
}