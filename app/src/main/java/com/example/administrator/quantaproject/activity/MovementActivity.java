package com.example.administrator.quantaproject.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.astuetz.PagerSlidingTabStrip;
import com.example.administrator.quantaproject.R;
import com.example.administrator.quantaproject.tools.Adapter_PagerMovement;
import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by Administrator on 2017/4/16.
 */

public class MovementActivity extends AppCompatActivity {

    private PagerSlidingTabStrip pagerTab;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_movement);

        pagerTab = (PagerSlidingTabStrip) findViewById(R.id.pager_tabs_parent);
        viewPager = (ViewPager) findViewById(R.id.viewPager_parent);

        //设置ViewPager每次预加载3个pager
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new Adapter_PagerMovement(MovementActivity.this,getSupportFragmentManager()));
        pagerTab.setViewPager(viewPager);
    }

    public ViewPager getViewPager() {
        return viewPager;
    }
}