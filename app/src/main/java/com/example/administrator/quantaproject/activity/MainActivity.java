package com.example.administrator.quantaproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.quantaproject.R;
import com.example.administrator.quantaproject.data.PingTai_Config;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String token = PingTai_Config.getCachedToken(this);
        String phoneNum = PingTai_Config.getCachedPhoneNum(this);

        if(token!=null&&phoneNum!=null){
            Intent intent = new Intent(this,MovementActivity.class);
            intent.putExtra(PingTai_Config.KEY_TOKEN,token);
            intent.putExtra(PingTai_Config.KEY_PHONE_NUM,phoneNum);
            startActivity(intent);
        }else{
            startActivity(new Intent(this,LoginActivity.class));
        }

        finish();
    }
}