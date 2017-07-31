package com.example.administrator.quantaproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.astuetz.PagerSlidingTabStrip;
import com.example.administrator.quantaproject.R;
import com.example.administrator.quantaproject.data.PingTai_Config;
import com.example.administrator.quantaproject.tools.Adapter_PagerMovement;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.List;

/**
 * Created by Administrator on 2017/4/16.
 */

public class MovementActivity extends AppCompatActivity {

    private static final String TAG = "MovementActivity";
    private PagerSlidingTabStrip pagerTab;
    private ViewPager viewPager;
    private Toolbar toolbar_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_movement);

        pagerTab = (PagerSlidingTabStrip) findViewById(R.id.pager_tabs_parent);
        viewPager = (ViewPager) findViewById(R.id.viewPager_parent);
//        toolbar_main = (Toolbar) findViewById(R.id.tb_main);

        //设置ViewPager每次预加载3个pager
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new Adapter_PagerMovement(MovementActivity.this,getSupportFragmentManager()));
        pagerTab.setViewPager(viewPager);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<android.support.v4.app.Fragment> fragments = getSupportFragmentManager().getFragments();
        Log.d(TAG,"fragments.SIZE ="+fragments.size());
        Log.d(TAG,"fragments.TAG ="+fragments.get(fragments.size()-4));

        Log.d(TAG,"requestCode ="+requestCode);
        if(requestCode== 123){
            fragments.get(fragments.size()-4).onRequestPermissionsResult(requestCode,permissions,grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<android.support.v4.app.Fragment> fragments = getSupportFragmentManager().getFragments();
        switch (requestCode){
            case PingTai_Config.REQUEST_CODE_ASK_FOR_CROP:
                fragments.get(fragments.size()-4).onActivityResult(requestCode,resultCode,data);
                break;
            default:
                break;
        }
    }

    public ViewPager getViewPager() {
        return viewPager;
    }
}